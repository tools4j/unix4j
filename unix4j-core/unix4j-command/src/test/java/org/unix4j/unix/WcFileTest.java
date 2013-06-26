package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;

/**
 * Unit test for {@code Sort} reading the input from test files.
 */
public class WcFileTest {
	
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
	public void wc_singleFile() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		final Config config = new Config(tester);
		tester.runAndAssert(Unix4j.use(config).wc(tester.getInputFile()));
        tester.runAndAssert(Unix4j.use(config).wc(tester.getInputFile().getPath()));
	}

    @Test
    public void wc_singleFile_withRelativePath() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).wc(tester.getInputFile().getName()));
    }

    @Test
    public void wc_multipleFiles() {
        final CommandFileTest tester = new CommandFileTest(this.getClass(), 4);
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).wc(tester.getInputFiles()));
        tester.runAndAssert(Unix4j.use(config).wc(tester.getInputFileNames()));
    }
}
