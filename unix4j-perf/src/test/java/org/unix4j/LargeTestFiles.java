package org.unix4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

/**
 * User: ben
 */
public enum LargeTestFiles {
	FILE_10_MEG("/10_Meg_test_file.txt", 10),
	FILE_100_MEG("/100_Meg_test_file.txt", 100);

	private final static String FILE_1_MEG_PATH = "/1_Meg_test_file.txt";
	private int meg;
	private String filePath;
	private File file;

	private LargeTestFiles(final String filePath, final int meg){
		this.filePath = filePath;
		this.meg = meg;
		ensureLargeFileExists();
	}

	public static void load(){
		for(LargeTestFiles testFile: LargeTestFiles.values()){
			testFile.ensureLargeFileExists();
		}
	}

	public File getFile() {
		if(file == null){
			throw new RuntimeException("Test file at " + filePath + " does not exist.  Please ensure that your test" +
					"calls LargeTestFiles.load() before each test is run....");
		}
		return file;
	}

	private void ensureLargeFileExists() {
		if(file == null){
			final File smallFile = getSmallFile();
			file = new File(smallFile.getParent(), filePath);
			createLargeFileIfItDoesntExist(file, smallFile, meg);
		} else {
			System.out.println("File already exists:" + filePath);
		}
	}

	private void createLargeFileIfItDoesntExist(final File destinationFile, final File smallFile, int multiplesOfSmallFile) {
		try{
			if (!destinationFile.exists()){
				System.out.println("Large file does not exist, creating.");
				final String file1MegContents = readFile(smallFile);

				final FileWriter fileWriter = new FileWriter(destinationFile);
				System.out.println("Creating large test file at: " + destinationFile.getAbsolutePath());
				for (int i = 0; i < multiplesOfSmallFile; i++) {
					fileWriter.write(file1MegContents);
					System.out.print(".");
				}
				System.out.println("");
				fileWriter.flush();
				fileWriter.close();
			} else {
				System.out.println("File at " + destinationFile + " already exists...");
			}
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	private File getSmallFile() {
		// Ensure that the small 1MB test file exists
		final URL file1MUrl = LargeTestFiles.class.getResource(FILE_1_MEG_PATH);
		final String SMALL_FILE_MUST_EXIST_MESSAGE = "To run perf tests, " + FILE_1_MEG_PATH + " must exist on the classpath.  Usually this" +
				"should exist in the unix4j-perf/src/test/resources directory.  It should then be copied" +
				"to the target/test-classes directory.  This file will be used during perf testing to" +
				"create the larger test files which are also used for perf testing...";

		assertTrue(SMALL_FILE_MUST_EXIST_MESSAGE, (file1MUrl != null));
		return new File(file1MUrl.getFile());
	}

	private static String readFile(final File file) throws IOException {
		final FileInputStream stream = new FileInputStream(file);
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}
}
