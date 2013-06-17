package org.unix4j.unix;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;
import org.unix4j.util.FileUtil;

/**
 * Unit test for simple App.
 */
public class GrepTest {
    final File outputDir = FileUtil.getOutputDirectoryGivenClass(this.getClass());
    final ExecutionContextFactory contextFactory = new ExecutionContextFactory() {
        @Override
        public ExecutionContext createExecutionContext() {
            final DefaultExecutionContext context = new DefaultExecutionContext();
            context.setCurrentDirectory(outputDir);
            return context;
        }
    };

    @Test
    public void testCountOnRelativeFile(){
        final File testFile = new File(outputDir.getPath() + "/commuting.txt" );
        assertEquals("118: commuting.txt", Unix4j.use(contextFactory).grep(Grep.Options.count, "the", testFile).toStringResult());
    }

    @Test
    public void testCountResultUsingWc(){
        final File testFile = new File(outputDir.getPath() + "/commuting.txt" );
        assertEquals("118", Unix4j.use(contextFactory).grep("the", testFile).wc(Wc.Options.l).toStringResult());
    }

    @Test
    public void testCountMultipleAbsolutePaths(){
        final String outputDirPath = outputDir.getPath();
        final String path1 = outputDirPath + "/org-unix4j-unix/GrepTest/bb.txt";
        final String path2 = outputDirPath + "/org-unix4j-unix/GrepTest/folder/bb.txt";
        assertEquals("2", Unix4j.use(contextFactory).grep("everything", path1, path2).wc(Wc.Options.l).toStringResult());
        assertEquals("2", Unix4j.use(contextFactory).grep(Pattern.compile(".*everything.*"), path1, path2).wc(Wc.Options.l).toStringResult());
        assertEquals2(	"1: ./org-unix4j-unix/GrepTest/bb.txt", 
        				"1: ./org-unix4j-unix/GrepTest/folder/bb.txt", Unix4j.use(contextFactory).grep(Grep.Options.count, "everything", path1, path2));
    }

    @Test
    public void testGrepWholeLine(){
        final String lineRegex = "the cistern .* the dishwasher hot-rinsing, and the kettle being";
        final String actualLine = "the cistern refilling, the dishwasher hot-rinsing, and the kettle being";

        final File testFile = new File(outputDir.getPath() + "/commuting.txt" );
        assertEquals(actualLine, Unix4j.use(contextFactory).grep(Grep.Options.wholeLine, lineRegex, testFile).toStringResult());
    }

    @Test
    public void testGrepWholeLineFixedStrings(){
        final String line = "the cistern refilling, the dishwasher hot-rinsing, and the kettle being";
        final File testFile = new File(outputDir.getPath() + "/commuting.txt" );
        assertEquals(line, Unix4j.use(contextFactory).grep(Grep.Options.wholeLine.fixedStrings, line, testFile).toStringResult());
    }

    @Test
    public void testCountOnRelativeFiles(){
    	assertEquals2("118: commuting.txt", "7: commuting2.txt", Unix4j.use(contextFactory).grep(Grep.Options.count, "the", "*.txt"));
    }

    @Test
    public void testCountOnRelativeFilesWithCd(){
    	assertEquals2("118: commuting.txt", "7: commuting2.txt", Unix4j.cd(outputDir).grep(Grep.Options.count, "the", "*.txt"));
    }
    
    private static void assertEquals2(String line1, String line2, Unix4jCommandBuilder actual) {
    	assertEquals(Arrays.asList(line1, line2), actual.toStringList());
    }

}
