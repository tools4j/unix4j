
package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.context.ExecutionContextFactory;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.io.URLInput;
import org.unix4j.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

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
        assertEquals("118", Unix4j.use(contextFactory).grep(Grep.Options.count, "the", testFile).toStringResult());
    }

    @Test
    public void testCountResultUsingWc(){
        final File testFile = new File(outputDir.getPath() + "/commuting.txt" );
        assertEquals("118", Unix4j.use(contextFactory).grep("the", testFile).wc(Wc.Options.l).toStringResult());
    }

    @Test
    public void testCountMultipleAbsolutePaths(){
        final String outputDirPath = outputDir.getPath();
        final String file1 = "org-unix4j-unix/GrepTest/bb.txt";
        final String file2 = "org-unix4j-unix/GrepTest/folder/bb.txt";
        final String path1 = outputDirPath + "/" + file1;
        final String path2 = outputDirPath + "/" + file2;
        assertEquals("2", Unix4j.use(contextFactory).grep("everything", path1, path2).grep(Grep.Options.count, ".*").toStringResult());
        assertEquals("2", Unix4j.use(contextFactory).grep("everything", path1, path2).wc(Wc.Options.l).toStringResult());
        assertEquals("2", Unix4j.use(contextFactory).grep(Pattern.compile(".*everything.*"), path1, path2).wc(Wc.Options.l).toStringResult());
        assertEquals2(	"1: ./" + file1, "1: ./" + file2, Unix4j.use(contextFactory).grep(Grep.Options.count, "everything", path1, path2));
    }

    @Test
    public void testCountMultipleInputs() throws Exception {
        //given
        final String outputDirPath = outputDir.getPath();
        final String file1 = "org-unix4j-unix/GrepTest/bb.txt";
        final String file2 = "org-unix4j-unix/GrepTest/folder/bb.txt";
        final String path1 = outputDirPath + "/" + file1;
        final String path2 = outputDirPath + "/" + file2;
        final URL url2 = new URL("file:" + path2);
        final Supplier<Input[]> inputs = () -> new Input[] {
                new FileInput(path1),
                new URLInput(url2)
        };

        //when + then
        assertEquals("2", Unix4j.use(contextFactory).grep("everything", inputs.get()).grep(Grep.Options.count, ".*").toStringResult());
        assertEquals("2", Unix4j.use(contextFactory).grep("everything", inputs.get()).wc(Wc.Options.l).toStringResult());
        assertEquals("2", Unix4j.use(contextFactory).grep(Pattern.compile(".*everything.*"), inputs.get()).wc(Wc.Options.l).toStringResult());
        assertEquals2(	"1: ./" + file1, "1: " + url2, Unix4j.use(contextFactory).grep(Grep.Options.count, "everything", inputs.get()));
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
    	assertEquals2("118: commuting.txt", "7: commuting2.txt", Unix4j.use(contextFactory).grep(Grep.Options.count, "the", "*.txt").sort());
    }

    @Test
    public void testCountOnRelativeFilesWithCd(){
        assertEquals2("118: commuting.txt", "7: commuting2.txt", Unix4j.cd(outputDir).grep(Grep.Options.count, "the", "*.txt").sort());
    }

    @Test
    public void testLineNumber(){
        final File testFile = new File(outputDir.getPath() + "/commuting.txt" );
        assertEquals("3:Subject: Commuting for beginners.",
                Unix4j.use(contextFactory).grep(Grep.Options.lineNumber, "Subject", testFile).toStringResult());
        assertEquals("3:Subject: Commuting for beginners.",
                Unix4j.use(contextFactory).grep(Grep.Options.lineNumber, "Subject", testFile.getPath()).toStringResult());
        assertEquals("3:Subject: Commuting for beginners.",
                Unix4j.use(contextFactory).grep(Grep.Options.lineNumber, "Subject", testFile.getAbsoluteFile()).toStringResult());
        assertEquals("3:Subject: Commuting for beginners.",
                Unix4j.use(contextFactory).grep(Grep.Options.lineNumber, "Subject", testFile.getAbsolutePath()).toStringResult());
    }

    @Test
    public void testLineNumberOnRelativeFiles() throws IOException {
        assertEquals2(
                "commuting.txt:3:Subject: Commuting for beginners.",
                "commuting2.txt:3:Subject: Commuting for beginners.",
                Unix4j.use(contextFactory).grep(Grep.Options.lineNumber, "Subject", "*.txt").sort());
        final File[] testFiles = new File[] {
                new File(outputDir.getPath() + "/commuting.txt" ),
                new File(outputDir.getPath() + "/commuting2.txt" ),
        };
        assertEquals2(
                "commuting.txt:3:Subject: Commuting for beginners.",
                "commuting2.txt:3:Subject: Commuting for beginners.",
                Unix4j.use(contextFactory).grep(Grep.Options.lineNumber, "Subject", testFiles).sort()
        );
        final String urlPathPrefix = "file:" + outputDir.getPath();
        final Input[] testInputs = new Input[] {
                new URLInput(new URL(urlPathPrefix + "/commuting.txt" )),
                new URLInput(new URL(urlPathPrefix + "/commuting2.txt" )),
        };
        assertEquals2(
                urlPathPrefix + "/commuting.txt:3:Subject: Commuting for beginners.",
                urlPathPrefix + "/commuting2.txt:3:Subject: Commuting for beginners.",
                Unix4j.use(contextFactory).grep(Grep.Options.lineNumber, "Subject", testInputs).sort()
        );
    }

    /**
     * Unit test for Issue #58.
     * <p>
     * See <a href="https://github.com/tools4j/unix4j/issues/58">Issue 58</a>
     */
    @Test
    public void testLineNumberWithRegex() {
        final String inputPath = new File(outputDir, "theFileName.theFileExtension").getAbsolutePath();
        final String evilRegex = "anyStringOrRegexpITried";
        //Unix4j.fromFile(inputPath).grep(Grep.Options.lineNumber, evilRegex).toStdOut();
        assertEquals("1:anyStringOrRegexpITried", Unix4j.fromFile(inputPath).grep(Grep.Options.lineNumber, evilRegex).toStringResult());
    }

    /**
     * Unit test for Issue #59.
     * <p>
     * See <a href="https://github.com/tools4j/unix4j/issues/59">Issue 59</a>
     */
    @Test
    public void testFixedStringWithIgnoreCase() {
        //given
        final String[] lines = {"Hello", "World", "HELLO", "WORLD", "Hello world", "HELLO WORLD"};
        final String outputDirPath = outputDir.getPath();
        final File file = new File(outputDirPath, "testFixedStringWithIgnoreCase.text");
        Unix4j.fromStrings(lines).toFile(file);

        //when + then
        assertEquals2(	"Hello world", "HELLO WORLD",
                Unix4j.fromStrings(lines).grep(Grep.Options.F.i, "hEllO wORLd"));
        assertEquals2(	"Hello world", "HELLO WORLD",
                Unix4j.grep(Grep.Options.F.i, "hEllO wORLd", file));
        assertEquals2(	"Hello world", "HELLO WORLD",
                Unix4j.grep(Grep.Options.F.i, "hEllO wORLd", file.getPath()));
        assertEquals2(	"Hello world", "HELLO WORLD",
                Unix4j.grep(Grep.Options.F.i, "hEllO wORLd", outputDir.getPath() + "/testFixedStringWithIgnoreCase.*"));
    }

    /**
     * Issue #53: The process cannot access the file because it is being used by another process
     *
     * NOTE: cannot reproduce on mac or unix, needs to be windows.
     */
    @Test
    public void testGrepFileThenDelete() throws IOException {
        //given
        final String[] lines = {"Hello", "World", "These are 3 lines"};
        final String outputDirPath = outputDir.getPath();
        final File file = new File(outputDirPath, "testGrepFileThenDelete.text");
        Unix4j.fromStrings(lines).toFile(file);
        assertEquals(true, file.exists());

        //when
        final String output = Unix4j.cat(file).grep(Grep.Options.count, ".*").toStringResult();
        Files.delete(file.toPath());

        //then
        assertEquals("3", output);
        assertEquals(false, file.exists());
    }

    //Added in response to https://github.com/tools4j/unix4j/pull/65
    @Test
    public void testFromInputStream() throws IOException {
        //given
        final File testFile = new File(outputDir.getPath() + "/commuting.txt" );
        final InputStream inputStream = new BufferedInputStream(new FileInputStream(testFile));

        //when + then
        assertEquals("Subject: Commuting for beginners.",
                Unix4j.from(inputStream).grep("Subject", testFile).toStringResult());
    }

    private static void assertEquals2(String line1, String line2, Unix4jCommandBuilder actual) {
    	assertEquals(Arrays.asList(line1, line2), actual.toStringList());
    }

}
