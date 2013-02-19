package org.unix4j.util;


import java.io.File;
import java.net.URL;

public class FileTestUtils {
    public static final File getTestFile(Class<?> testClass, String fileName) {
        final StackTraceElement stackTraceElement = StackTraceUtil.getCurrentMethodStackTraceElement(1);
        return getTestFile(testClass, stackTraceElement.getMethodName(), fileName);

    }

    public static final File getTestFile(Class<?> testClass, String testMethod, String fileName) {
        return getTestFile(testClass, testMethod, fileName, null);
    }

    public static final File getTestFile(Class<?> testClass, String testMethod, String fileName, String defaultFileName) {
        return(getTestFile(getTestDir(testClass), testMethod, fileName, defaultFileName));
    }

    public static final File getTestFile(File parentDir, String testMethod, String fileName, String defaultFileName) {
        File file = new File(parentDir, fileName);
        if (!file.exists()) {
            if (defaultFileName == null) {
                throw new IllegalArgumentException("test file for " + parentDir.getName() + "." + testMethod + " not found, expected file: " + fileName);
            }
            file = new File(parentDir, defaultFileName);
            if (!file.exists()) {
                throw new IllegalArgumentException("test file for " + parentDir.getName() + "." + testMethod + " not found, expected file: " + fileName + " or default file: " + defaultFileName);
            }
        }
        return file;
    }

    public static File getTestDir(Class<?> testClass){
        final String testDir = "/" + getTestDirRelativeToPackageDir(testClass);
        URL fileURL = testClass.getResource(testDir);
        if(fileURL == null){
            throw new IllegalArgumentException("Test directory does not exist.  Please ensure it exists at [" + testDir + "]");
        }
        return new File(fileURL.getFile());
    }

    private static String getTestDirRelativeToPackageDir(Class<?> testClass) {
        final String packageDir = testClass.getPackage().getName().replace('.', '-');
        return packageDir + "/" + testClass.getSimpleName();
    }
}
