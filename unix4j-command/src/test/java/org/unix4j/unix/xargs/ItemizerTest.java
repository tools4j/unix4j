package org.unix4j.unix.xargs;

import junit.framework.Assert;
import org.junit.Test;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;

import java.util.ArrayList;
import java.util.List;

public class ItemizerTest {
	
	private static final Line LINE_0 = new SimpleLine("hello world");
	private static final Line LINE_1 = new SimpleLine("  line one");
	private static final Line LINE_2 = new SimpleLine("  \ttabs\t\t and\tspaces    \t");
	private static final Line LINE_3 = new SimpleLine("");
	private static final Line LINE_4 = new SimpleLine("CR", Line.CR);
	private static final Line LINE_5 = new SimpleLine("LF", Line.LF);
	private static final Line LINE_6 = new SimpleLine("Z E\n \r R\r\nO", Line.ZERO);
	private static final Line LINE_7 = new SimpleLine("A\0B\0C\0\0E");
	
	private class TestItemStorage implements ItemStorage {
		private final List<String> items = new ArrayList<String>();
		private int lineCount;
		@Override
		public void storeItem(String item) {
			items.add(item);
		}
		@Override
		public void incrementLineCount() {
			lineCount++;
		}
		public void clear() {
			items.clear();
			lineCount = 0;
		}
	};
	private final TestItemStorage storage = new TestItemStorage();
	
	@Test
	public void testWhitespaceItemizer() {
		final Itemizer itemizer = new WhitespaceItemizer();
		itemizeAndExpect(itemizer, LINE_0, 1, "hello", "world");
		itemizeAndExpect(itemizer, LINE_1, 1, "line", "one");
		itemizeAndExpect(itemizer, LINE_2, 0, "tabs", "and", "spaces");
		itemizeAndExpect(itemizer, LINE_3, 1);
		itemizeAndExpect(itemizer, LINE_4, 1, "CR");
		itemizeAndExpect(itemizer, LINE_5, 1, "LF");
		itemizeAndExpect(itemizer, LINE_6, 1, "Z", "E", "R", "O\0");
		itemizeAndExpect(itemizer, LINE_7, 1, LINE_7.getContent());

		//two lines
		storage.clear();
		itemizer.itemizeLine(LINE_0, storage);
		itemizer.itemizeLine(LINE_1, storage);
		itemizer.finish(storage);
		expect(itemizer, 2, "hello", "world", "line", "one");

		//two lines, first line with trailing spaces
		//--> indicates that the two lines should be counted as a single line
		storage.clear();
		itemizer.itemizeLine(LINE_2, storage);
		itemizer.itemizeLine(LINE_4, storage);
		itemizer.finish(storage);
		expect(itemizer, 1, "tabs", "and", "spaces", "CR");
	}

	@Test
	public void testDelimiter0Itemizer() {
		final Itemizer itemizer = new CharDelimitedItemizer(Line.ZERO);

		itemizeAndExpect(itemizer, LINE_0, 1, LINE_0.toString());
		itemizeAndExpect(itemizer, LINE_1, 1, LINE_1.toString());
		itemizeAndExpect(itemizer, LINE_2, 1, LINE_2.toString());
		itemizeAndExpect(itemizer, LINE_3, 1, LINE_3.toString());
		itemizeAndExpect(itemizer, LINE_4, 1, LINE_4.toString());
		itemizeAndExpect(itemizer, LINE_5, 1, LINE_5.toString());
		itemizeAndExpect(itemizer, LINE_6, 1, LINE_6.getContent());
		itemizeAndExpect(itemizer, LINE_7, 1, "A", "B", "C", "", "E" + LINE_7.getLineEnding());
		
		//item that spans two lines now
		storage.clear();
		itemizer.itemizeLine(LINE_7, storage);
		itemizer.itemizeLine(LINE_7, storage);
		itemizer.finish(storage);
		expect(itemizer, 2, "A", "B", "C", "", "E" + LINE_7.getLineEnding() + "A", "B", "C", "", "E" + LINE_7.getLineEnding());
	}

	private void itemizeAndExpect(Itemizer itemizer, Line line, int expectedLineCountBeforeFinish, String... expectedItems) {
		storage.clear();
		itemizer.itemizeLine(line, storage);
		Assert.assertEquals("line count", expectedLineCountBeforeFinish, storage.lineCount);
		itemizer.finish(storage);
		expect(itemizer, 1, expectedItems);
	}
	private void expect(Itemizer itemizer, int expectedLineCount, String... expectedItems) {
		Assert.assertEquals("line count", expectedLineCount, storage.lineCount);
		Assert.assertEquals("number of items", expectedItems.length, storage.items.size());
		for (int i = 0; i < expectedItems.length; i++) {
			Assert.assertEquals("item[" + i + "]", expectedItems[i], storage.items.get(i));
		}
	}
}
