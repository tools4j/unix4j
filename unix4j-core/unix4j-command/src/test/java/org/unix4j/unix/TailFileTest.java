package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;

/**
 * Unit test for {@code Sort} reading the input from test files.
 */
public class TailFileTest {
	
	private static final class Config implements ExecutionContextFactory {
		private final CommandFileTest tester;
		public Config(CommandFileTest tester) {
			this.tester = tester;
		}
		@Override
		public ExecutionContext createExecutionContext() {
			final DefaultExecutionContext context = new DefaultExecutionContext();
			context.setCurrentDirectory(tester.getTestDir());
			return context;
		}
	};

	@Test
	public void tail_simple_countFromEnd() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		final Config config = new Config(tester);
		tester.runAndAssert(Unix4j.use(config).tail(8, tester.getInputFile()));
	}

    @Test
    public void tail_byChars_countFromEnd() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).tail(Tail.Options.c, 100, tester.getInputFile()));
    }

    @Test
    public void tail_multipleFiles_countFromEnd() {
        final CommandFileTest tester = new CommandFileTest(this.getClass(), 2);
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).tail(10, tester.getInputFiles()));
        tester.runAndAssert(Unix4j.use(config).tail(10, tester.getInputFileNames()));
        tester.runAndAssert(Unix4j.use(config).tail(tester.getInputFiles())); //By default, tail returns top 10 lines
        tester.runAndAssert(Unix4j.use(config).tail(combine(arr("--count", "10", "--paths"), tester.getInputFileNames())));
        tester.runAndAssert(Unix4j.use(config).tail(combine(arr("--paths"), tester.getInputFileNames())));
    }

    @Test
    public void tail_simple_countFromStart() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).tail(Tail.Options.countFromStart, 8, tester.getInputFile()));
    }

    @Test
    public void tail_byChars_countFromStart() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).tail(Tail.Options.c.countFromStart, 100, tester.getInputFile()));
    }

    @Test
    public void tail_multipleFiles_countFromStart() {
        final CommandFileTest tester = new CommandFileTest(this.getClass(), 2);
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).tail(10, tester.getInputFiles()));
        tester.runAndAssert(Unix4j.use(config).tail(10, tester.getInputFileNames()));
        tester.runAndAssert(Unix4j.use(config).tail(tester.getInputFiles())); //By default, tail returns top 10 lines
        tester.runAndAssert(Unix4j.use(config).tail(combine(arr("-s", "--count", "10", "--paths"), tester.getInputFileNames())));
        tester.runAndAssert(Unix4j.use(config).tail(combine(arr("-s", "--paths"), tester.getInputFileNames())));
    }


    @Test
    public void tail_byChars_specifyMoreCharsThanInInput() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).tail(Tail.Options.c, 1000000, tester.getInputFile()));
    }


    private static String[] arr(String... s) {
        return s;
    }

    private static String[] combine(String[] a, String... b) {
        final String[] merged = new String[a.length + b.length];
        System.arraycopy(a, 0, merged, 0, a.length);
        System.arraycopy(b, 0, merged, a.length, b.length);
        return merged;
    }
}
