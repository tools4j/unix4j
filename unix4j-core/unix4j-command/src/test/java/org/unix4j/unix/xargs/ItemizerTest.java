package org.unix4j.unix.xargs;

import junit.framework.Assert;

import org.junit.Test;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.variable.DefaultVariableContext;
import org.unix4j.variable.VariableContext;

public class ItemizerTest {
	
	private static final Line LINE_0 = new SimpleLine("hello world");
	private static final Line LINE_1 = new SimpleLine("  line one");
	private static final Line LINE_2 = new SimpleLine("  \ttabs\t\t and\tspaces    \t");
	private static final Line LINE_3 = new SimpleLine("");
	private static final Line LINE_4 = new SimpleLine("CR", Line.CR);
	private static final Line LINE_5 = new SimpleLine("LF", Line.LF);
	private static final Line LINE_6 = new SimpleLine("Z E\n \r R\r\nO", Line.ZERO);
	
	private final VariableContext context = new DefaultVariableContext();
	private final VariableContextItemStorage storage = new VariableContextItemStorage(context);
	
	@Test
	public void testWhitespaceItemizer() {
		final WhitespaceItemizer itemizer = new WhitespaceItemizer(new XargsArguments());
		expect(itemizer, LINE_0, "hello", "world");
		expect(itemizer, LINE_1, "line", "one");
		expect(itemizer, LINE_2, "tabs", "and", "spaces");
		expect(itemizer, LINE_3);
		expect(itemizer, LINE_4, "CR");
		expect(itemizer, LINE_5, "LF");
		expect(itemizer, LINE_6, "Z", "E", "R", "O\0");
	}

	private void expect(Itemizer itemizer, Line line, String... expectedItems) {
		expect(itemizer, line, false, expectedItems);
	}
	private void expect(Itemizer itemizer, Line line, boolean eof, String... expectedItems) {
		storage.reset();
		itemizer.itemizeLine(line, eof, storage);
		Assert.assertEquals("number of items", expectedItems.length, storage.size());
		for (int i = 0; i < expectedItems.length; i++) {
			Assert.assertEquals("item[" + i + "]", expectedItems[i], context.getValue(Xarg.arg(i)));
		}
	}
}
