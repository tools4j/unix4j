package org.unix4j.unix;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;

//@Ignore
public class FindFileTimeDependentTest {

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

    @Test
    public void find_fileCreatedBeforeNow(){
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final File fileCreatedAfterTime = new File(currentDirectory.getPath() + "/new-file1.txt");
        if (fileCreatedAfterTime.exists()) {
        	fileCreatedAfterTime.delete();
        }
        final Config config = new Config(tester, currentDirectory);
        tester.run(Unix4j.use(config).find(Find.Options.typeFile.timeOlder.timeCreate, currentDirectory.getPath(), new Date()));
    }

    @Test
    public void find_fileCreatedAfterTime() throws InterruptedException {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final Config config = new Config(tester, currentDirectory);
        final Date timeBeforeCreate = new Date();
        Thread.sleep(1000);
        Unix4j.echo("blah").toFile(currentDirectory.getPath() + "/new-file1.txt");
        tester.run(Unix4j.use(config).find(Find.Options.typeFile.timeNewer.timeCreate, currentDirectory.getPath(), timeBeforeCreate));
    }

    @Test
    public void find_fileCreatedAfterNow(){
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final Config config = new Config(tester, currentDirectory);
        tester.run(Unix4j.use(config).find(Find.Options.typeFile.timeNewer.timeCreate, currentDirectory.getPath(), new Date()));
    }

    @Test
    public void find_fileCreatedBeforeAVeryLongTimeAgo(){
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final Config config = new Config(tester, currentDirectory);
        tester.run(Unix4j.use(config).find(Find.Options.typeFile.timeOlder.timeCreate, currentDirectory.getPath(), new Date(0)));
    }

    @Test
    public void find_fileUpdated() throws InterruptedException {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final File currentDirectory = new File(tester.getInputFile().getParentFile().getPath() + "/default.input");
        final Config config = new Config(tester, currentDirectory);
        final Date timeBeforeUpdate = new Date();
        Thread.sleep(1000);
        Unix4j.echo("blah").toFile(currentDirectory.getPath() + "/existing-file1.txt");
        tester.run(Unix4j.use(config).find(Find.Options.typeFile.timeNewer.timeModified, currentDirectory.getPath(), timeBeforeUpdate));
    }
}
