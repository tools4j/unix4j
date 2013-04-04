package org.unix4j.unix.sed;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;

final class SubstituteProcessor extends AbstractSedProcessor {
	
	private static final int[] EMPTY_OCCURRENCE = new int[0];

	private final String regexp;
	private final String replacement;
	private final int[] occurrances;
	private final boolean isGlobal;

	public SubstituteProcessor(SedArguments args, LineProcessor output) {
		super(args, output);
		this.regexp = getRegexp(args);
		this.replacement = getReplacement(args);
		this.occurrances = args.isOccurrenceSet() ? args.getOccurrence() : EMPTY_OCCURRENCE;
		this.isGlobal = occurrances.length == 0 && args.isGlobal();
	}
	public SubstituteProcessor(String regexp, String replacement, int occurrence, boolean isGlobal, SedArguments args, LineProcessor output) {
		super(args, output);
		this.regexp = regexp;
		this.replacement = replacement;
		this.occurrances = occurrence == -1 ? EMPTY_OCCURRENCE : new int[] {occurrence};
		this.isGlobal = occurrances.length == 0 && isGlobal;
	}

	@Override
	public boolean processLine(Line line) {
		final String content = line.getContent();//or should sed operate on line with ending?
		final String changed;
		if (isGlobal) {
			changed = content.replaceAll(regexp, replacement);
		} else {
			changed = content.replaceFirst(regexp, replacement);
		}
		final Line l = changed == content ? line : new SimpleLine(changed, line.getLineEnding());
		return output.processLine(l);
	}
}