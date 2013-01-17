package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;
import org.unix4j.unix.FileTest.MatchMode;

/**
 * Unit test for {@code Sort} reading the input from test files.
 */
public class LsFileTest {
	
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
	public void ls() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls());
	}
	@Test
	public void ls_a() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.a));
	}
	@Test
	public void ls_ar() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.a.r));
	}
	@Test
	public void ls_la() {
		final FileTest tester = new FileTest(this.getClass(), MatchMode.Regex);
		final Config config = new Config(tester);
        Unix4j.use(config).ls(Ls.Options.l.a).toStdOut();
		tester.run(Unix4j.use(config).ls(Ls.Options.l.a));
	}
	@Test
	public void ls_la_TotalLine() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.l.a).head(1));
	}
	@Test
	public void ls_la_LineCount() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.l.a).wc(Wc.Options.l));
	}
	@Test
	public void ls_la_LastLine() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.l.a).tail(1).grep(Grep.Options.c, "\\smydir$"));//last line should end with "mydir" file
	}
	@Test
	public void ls_lar() {
		final FileTest tester = new FileTest(this.getClass(), MatchMode.Regex);
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.l.a.r));
	}
	@Test
	public void ls_lar_LastLine() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.l.a.r).tail(1).grep(Grep.Options.c, "\\s\\.$"));//last line should end with "." file
	}
	@Test
	public void ls_lah() {
		final FileTest tester = new FileTest(this.getClass(), MatchMode.Regex);
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.l.a.h));
	}
	@Test
	public void ls_lah_TotalLine() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.l.a.h).head(1));
	}

	@Test
	public void ls_R() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		tester.run(Unix4j.use(config).ls(Ls.Options.R));
	}
	@Test
	public void ls_Ra() {
		final FileTest tester = new FileTest(this.getClass());
		final Config config = new Config(tester);
		Unix4j.use(config).ls(Ls.Options.R.a).toStdOut();
		tester.run(Unix4j.use(config).ls(Ls.Options.R.a));
	}
}
