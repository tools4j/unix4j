# CodeDiscussion #

```
public interface Grep<S> extends CommandInterface<S> {
	String NAME = "grep";
	S grep(String matchString);
	S grep(String matchString, Option... options);
	enum Option {
		i, ignoreCase, 
		v, invert;
	}
}

public interface CommandBuilder extends Ls<CommandBuilder>, Grep<CommandBuilder>, Echo<CommandBuilder>, Sort<CommandBuilder>, Xargs<CommandBuilder> {
	org.unix4j.Command<?, Void> build();
	void execute(Input input, Output output);
}

public class CommandBuilderTest {

	private CommandBuilder builder;
	
	@Before
	public void beforeEach() {
		builder = new CommandBuilderImpl();
	}
	@After
	public void afterEach() {
		Command<?, ?> cmd = builder.build();
		System.out.println(">>> " + cmd);
		cmd.execute(new NullInput(), new StdOutput());
	}

	@Test
	public void testLs() {
		builder.ls();
	}
	@Test
	public void testLsFile() {
		builder.ls(new File("src"));
	}
	@Test
	public void testLsSort() {
		builder.ls().sort();
	}
	@Test
	public void testLsSortDesc() {
		builder.ls().sort(Sort.Option.descending);
	}
	@Test
	public void testEcho() {
		builder.echo("Hello world");
	}
	@Test
	public void testEcho2() {
		builder.echo("Hello", "world");
	}
	@Test
	public void testEchoGrepMatch() {
		builder.echo("Hello world").grep("world");
	}
	@Test
	public void testEchoGrepNoMatch() {
		builder.echo("Hello WORLD").grep("world");
	}
	@Test
	public void testEchoGrepMatchIgnoreCase() {
		builder.echo("Hello WORLD").grep("world", Grep.Option.ignoreCase);
	}
	@Test
	public void testLsXargsEcho() {
		builder.ls().xargs().echo("XARGS OUTPUT:", Xargs.XARG);
	}
}

```