package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;

import java.io.File;

public class FindTest {
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
    public void testFind() {
        final FileTest tester = new FileTest(this.getClass());
        final Config config = new Config(tester);
        Unix4j.use(config).find(".", "/home/ben/dev/*/groovy/*/*", "target/*", "asdf").toStdOut();
    }
}
