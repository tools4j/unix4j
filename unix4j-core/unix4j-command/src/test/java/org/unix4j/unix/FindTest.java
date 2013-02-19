package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;

public class FindTest {
    private static final class Config implements ExecutionContextFactory {
        private final CommandFileTest tester;
        public Config(CommandFileTest tester) {
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
    public void testFind() {
        final CommandFileTest tester = new CommandFileTest(this.getClass());
        final Config config = new Config(tester);
//        Unix4j.use(config).find(".", "/home/ben/dev/*/groovy/*/*", "target/*", "asdf").toStdOut();
//        Unix4j.use(config).find(".", "*.java").toStdOut();
//        Unix4j.use(config).find(-100).toStdOut();
        Unix4j.use(config).find(Find.Options.typeDirectory, ".", "*java").toStdOut();
//        Unix4j.use(config).find(-100).toStdOut();
//      Unix4j.use(config).find(Find.Options.timeNewer, ".", new Date(3600*1000)).toStdOut();
    }
}
