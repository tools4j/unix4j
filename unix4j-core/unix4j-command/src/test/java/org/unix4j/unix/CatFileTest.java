package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;
import org.unix4j.variable.Arg;

/**
 * Unit test for {@code Cat} reading the input from test files.
 */
public class CatFileTest {
	
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
	public void cat_simple() {
		final CommandFileTest tester = new CommandFileTest(this.getClass());
		final Config config = new Config(tester);
		tester.runAndAssert(Unix4j.use(config).cat(tester.getInputFile()));
	}

    @Test
    public void cat_usingArgs() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).echo(tester.getInputFileName()).xargs().cat(Arg.$0));
    }

    @Test
    public void cat_withLineNumbers(){
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).cat(Cat.Options.numberLines, tester.getInputFile()));
    }

    @Test
    public void cat_numberNonBlankLines(){
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).cat(Cat.Options.numberNonBlankLines, tester.getInputFile()));
    }

    @Test
    public void cat_squeezeEmptyLines(){
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).cat(Cat.Options.squeezeEmptyLines, tester.getInputFile()));
    }

    @Test
    public void cat_multipleFiles() {
        final CommandFileTest tester = new CommandFileTest(this.getClass(), 2);
		final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).cat(tester.getInputFiles()));
        tester.runAndAssert(Unix4j.use(config).cat(tester.getInputFileNames()));
        tester.runAndAssert(Unix4j.use(config).cat(tester.getInputFiles()));
        tester.runAndAssert(Unix4j.use(config).cat(combine(arr("--paths"), tester.getInputFileNames())));
        tester.runAndAssert(Unix4j.use(config).cat(Cat.Options.squeezeEmptyLines, tester.getInputFiles()));
    }

    @Test
    public void cat_multipleFiles_squeezeEmptyLines() {
        final CommandFileTest tester = new CommandFileTest(this.getClass(), 2);
        final Config config = new Config(tester);
        tester.runAndAssert(Unix4j.use(config).cat(Cat.Options.squeezeEmptyLines, tester.getInputFiles()));
        tester.runAndAssert(Unix4j.use(config).cat(Cat.Options.s, tester.getInputFiles()));
        tester.runAndAssert(Unix4j.use(config).cat(Cat.Options.squeezeEmptyLines, tester.getInputFileNames()));
        tester.runAndAssert(Unix4j.use(config).cat(Cat.Options.squeezeEmptyLines, tester.getInputFiles()));
        tester.runAndAssert(Unix4j.use(config).cat(combine(arr("--squeezeEmptyLines", "--paths"), tester.getInputFileNames())));
        tester.runAndAssert(Unix4j.use(config).cat(combine(arr("-s", "--paths"), tester.getInputFileNames())));
        tester.runAndAssert(Unix4j.use(config).cat(Cat.Options.squeezeEmptyLines, tester.getInputFiles()));
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
