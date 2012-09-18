package org.unix4j.unix.cut;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Cut;
import org.unix4j.util.Range;

/**
 * Implementation of the {@code Cut cut} command.
 */
class CutCommand extends AbstractCommand<CutArguments> {

	public CutCommand(CutArguments arguments) {
		super(Cut.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		//If indexes were specified as an int array, convert to a proper range object
		if(getArguments().isIndexesSet() && getArguments().isRangeSet()){
			throw new IllegalArgumentException("Only one of indexes or range can be set");
		} else if(getArguments().isIndexesSet()){
			getArguments().setRange(Range.of(getArguments().getIndexes()));
		}

		//Validate options
		if(getArguments().isDelimiterSet() && !getArguments().isFields()){
			throw new IllegalArgumentException("Delimiter can only be specified if cutting by fields");
		}
		if(getArguments().isOutputDelimiterSet() && !getArguments().isFields()){
			throw new IllegalArgumentException("Output delimiter can only be specified if cutting by fields");
		}

		return new LineProcessor() {
			@Override
			public boolean processLine(Line line) {
				if (getArguments().isChars()) {
					return cutByChars(line, output);
				} else if(getArguments().isFields()){
					return cutByFields(line, output);
				} else {
					throw new UnsupportedOperationException("Currently cut only supports cutting by chars or fields...");
				}
			}

			@Override
			public void finish() {
				output.finish();
			}

			private boolean cutByFields(Line line, LineProcessor output) {
				final String inputDelim = getArguments().isDelimiterSet() ? getArguments().getDelimiter(): "\\t";
				final char outputDelim = getArguments().isOutputDelimiterSet() ? getArguments().getOutputDelimiter(): inputDelim.charAt(0);
				final Range range = getArguments().getRange();
				//Passing -1 to the split function will cause it to not strip trailing empty strings
				final String[] fields = line.getContent().split(inputDelim, -1);
				final StringBuilder sb = new StringBuilder();

				boolean firstDone=false;
				for(int i=0; i<fields.length; i++){
					if(range.isWithinRange(i+1)){
						if(firstDone) sb.append(outputDelim);
						sb.append(fields[i]);
						firstDone=true;
					}
				}
				return output.processLine(new SimpleLine(sb, line.getLineEnding()));
			}

			private boolean cutByChars(Line line, LineProcessor output) {
				final Range range = getArguments().getRange();
				final char[] chars = line.getContent().toCharArray();
				final StringBuilder sb = new StringBuilder();

				for(int i=0; i<chars.length; i++){
					if(range.isWithinRange(i+1)){
						sb.append(chars[i]);
					}
				}
				return output.processLine(new SimpleLine(sb, line.getLineEnding()));
			}
		};
	}
}
