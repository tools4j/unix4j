package org.unix4j.unix;

import org.junit.Ignore;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;

/**
 * Unit test for {@code Sort} reading the input from test files.
 */
public class HeadFileTest {
	
	private static final class Config implements ExecutionContextFactory {
		private final FileTest tester;
		public Config(FileTest tester) {
			this.tester = tester;
		}
		@Override
		public ExecutionContext createExecutionContext() {
			final DefaultExecutionContext context = new DefaultExecutionContext();
			context.setCurrentDirectory(tester.getInputFile());
			return context;
		}
	};

	@Test
	public void head_simple() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).head(8, tester.getInputFile()));
	}

    @Test
    public void head_byChars() {
        final FileTest tester = new FileTest(this.getClass());
        final Config config = new Config(tester);
        tester.run(Unix4j.use(config).head(Head.Options.c, 100, tester.getInputFile()));
    }

    @Test
    public void head_byChars_specifyMoreCharsThanInInput() {
        final FileTest tester = new FileTest(this.getClass());
        final Config config = new Config(tester);
        tester.run(Unix4j.use(config).head(Head.Options.c, 1000000, tester.getInputFile()));
    }

    @Test
    public void head_multipleFiles() {
        final FileTest tester = new FileTest(this.getClass(), FileTest.MatchMode.Regex, 2);
        tester.run(Unix4j.head(10, tester.getInputFiles()));
        tester.run(Unix4j.head(10, tester.getInputFileNames()));
        tester.run(Unix4j.head(tester.getInputFiles())); //By default, head returns top 10 lines
        tester.run(Unix4j.builder().head(combine(arr("--count", "10", "--paths"), tester.getInputFileNames())));
        tester.run(Unix4j.builder().head(combine(arr("--paths"), tester.getInputFileNames())));
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
