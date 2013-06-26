package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;

/**
 * Unit test for {@code Head} reading the input from test files.
 */
public class HeadFileTest {
	
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
	public void head_simple() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		final Config config = new Config(tester);
		tester.runAndAssert(Unix4j.use(config).head(8, tester.getInputFile()));
	}

    @Test
    public void head_byChars() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).head(Head.Options.c, 100, tester.getInputFile()));
    }

    @Test
    public void head_byChars_specifyMoreCharsThanInInput() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).head(Head.Options.c, 1000000, tester.getInputFile()));
    }

    @Test
    public void head_multipleFiles() {
        final CommandFileTest tester = new CommandFileTest(this.getClass(), 2);
		final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).head(10, tester.getInputFiles()));
        tester.runAndAssert(Unix4j.use(config).head(10, tester.getInputFileNames()));
        tester.runAndAssert(Unix4j.use(config).head(tester.getInputFiles())); //By default, head returns top 10 lines
        tester.runAndAssert(Unix4j.use(config).head(combine(arr("--count", "10", "--paths"), tester.getInputFileNames())));
        tester.runAndAssert(Unix4j.use(config).head(combine(arr("--paths"), tester.getInputFileNames())));
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
