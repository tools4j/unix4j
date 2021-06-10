package org.unix4j.unix.xargs;

import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

/**
 * Output object passed to invoked commands. Forwards line output to a delegate
 * output object but swallows the finish call as we only want to finish output
 * after all invocations.
 */
class XargsOutput implements LineProcessor {
	
	private final LineProcessor delegate;
	
	public XargsOutput(LineProcessor delegate) {
		this.delegate = delegate;
	}
	@Override
	public boolean processLine(Line line) {
		return delegate.processLine(line);
	}
	@Override
	public void finish() {
		//do nothing here, forward finish call in finishAll() 
	}
	public void finishAll() {
		delegate.finish();
	}
}
