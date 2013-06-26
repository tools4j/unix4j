package org.unix4j.unix;

import java.io.File;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;

//@Ignore
public class FindFileTimeDependentTest {
	
	/**
	 * Name of the file that is created during the test.
	 */
	private static final String FILE_TO_CREATE = "new-file1.txt";

	/**
	 * Name of the file that is updated during the test.
	 */
	private static final String FILE_TO_UPDATE = "existing-file1.txt";

	private static final class Config implements ExecutionContextFactory {
		private final CommandFileTest tester;
        private final File currentDirectory;

        public Config(final CommandFileTest tester, final File currentDirectory) {
            this.tester = tester;
            this.currentDirectory = currentDirectory;
        }
		@Override
		public ExecutionContext createExecutionContext() {
			final DefaultExecutionContext context = new DefaultExecutionContext();
            if(currentDirectory != null){
                context.setCurrentDirectory(currentDirectory);
            } else {
                context.setCurrentDirectory(tester.getInputFile());
            }
			return context;
		}
	};
	
	@BeforeClass
	public static void beforeClass() {
        final CommandFileTest tester = new CommandFileTest(FindFileTimeDependentTest.class);
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final File fileCreatedAfterTime = new File(currentDirectory.getPath() + "/" + FILE_TO_CREATE);
        if (fileCreatedAfterTime.exists()) {
        	fileCreatedAfterTime.delete();
        }
	}

    @Test
    public void find_fileCreatedBeforeNow(){
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final Config config = new Config(tester, currentDirectory);
        tester.runAndAssertIgnoringOrder(Unix4j.use(config).find(Find.Options.typeFile.timeOlder.timeCreate, ".", new Date()));
    }

    @Test
    public void find_fileCreatedAfterTime() throws InterruptedException {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final Config config = new Config(tester, currentDirectory);
        final Date timeBeforeCreate = new Date();
        Thread.sleep(1000);
        Unix4j.echo("blah").toFile(currentDirectory.getPath() + "/" + FILE_TO_CREATE);
        tester.runAndAssertIgnoringOrder(Unix4j.use(config).find(Find.Options.typeFile.timeNewer.timeCreate, ".", timeBeforeCreate));
    }

    @Test
    public void find_fileCreatedAfterNow() throws InterruptedException{
        Thread.sleep(1000);
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final Config config = new Config(tester, currentDirectory);
        tester.runAndAssertIgnoringOrder(Unix4j.use(config).find(Find.Options.typeFile.timeNewer.timeCreate, ".", new Date(System.currentTimeMillis() + 1)));
    }

    @Test
    public void find_fileCreatedBeforeAVeryLongTimeAgo(){
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final Config config = new Config(tester, currentDirectory);
        tester.runAndAssertIgnoringOrder(Unix4j.use(config).find(Find.Options.typeFile.timeOlder.timeCreate, ".", new Date(0)));
    }

    @Test
    public void find_fileUpdated() throws InterruptedException {
        Thread.sleep(1000);
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final Config config = new Config(tester, currentDirectory);
        final Date timeBeforeUpdate = new Date();
        Thread.sleep(1000);
        Unix4j.echo("blah").toFile(new File(currentDirectory, FILE_TO_UPDATE));
        tester.runAndAssertIgnoringOrder(Unix4j.use(config).find(Find.Options.typeFile.timeNewer.timeModified, ".", timeBeforeUpdate));
    }
}
