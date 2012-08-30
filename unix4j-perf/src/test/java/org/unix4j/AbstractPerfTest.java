package org.unix4j;

import org.junit.Before;
import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.util.StackTraceUtil;

import java.util.Date;

import static java.lang.String.format;
import static org.junit.Assert.fail;

/**
 * User: ben
 */
public class AbstractPerfTest {
	private final static ResultsFile resultsFile = new ResultsFile();
	private final static double MAX_EXECUTION_TIME_TO_BASELINE = 3.0d;
	private final static LinuxTestScriptCreator linuxTestScriptCreator = new LinuxTestScriptCreator();
	private final static ResultsSummaryCsvFile resultsSummaryCsvFile = new ResultsSummaryCsvFile();

	private long timeStarted;

	@Before
	public void beforeEach(){
		LargeTestFiles.load();
	}

	protected void run(final Unix4jCommandBuilder command, final String linuxEquivalentCommand) {
		final String testName = getTestName();

		final long executionTime = runCommand(command);
		System.out.println(testName + ".executionTime=" + executionTime);
		System.out.println("");

		linuxTestScriptCreator.writeCommandTest(testName, linuxEquivalentCommand);
		resultsFile.write(testName + ".executionTime", ""+executionTime);

		final TestBaseline unix4jBaseline = TestBaseline.Factory.UNIX4J.create(testName);
		final TestBaseline linuxBaseline = TestBaseline.Factory.LINUX.create(testName);
		printExecutionTimeSummary(testName, command, executionTime, unix4jBaseline, linuxBaseline);
		resultsSummaryCsvFile.write(testName, linuxBaseline.getExecutionTime(), unix4jBaseline.getExecutionTime(), executionTime);

		if(executionTime > MAX_EXECUTION_TIME_TO_BASELINE*unix4jBaseline.getExecutionTime()){
			failAsExecutionTimeTooLargeComparedToBaseline(unix4jBaseline.getExecutionTime(), executionTime);
		}
	}

	private void startTimer() {
		System.out.println("Starting timer...");
		timeStarted = new Date().getTime();
	}

	private void failAsExecutionTimeTooLargeComparedToBaseline(final long baseline, final long executionTime) {
		final String failureMessage =
				"Test failed.  Baseline=" + baseline + " ExecutionTime:" + executionTime + " Failure occurred "
						+ "because it took longer to execute this test than the previously "
						+ "recorded baseline by a factor of " + MAX_EXECUTION_TIME_TO_BASELINE + ". This can mean "
						+ "one of a few things: "
						+ "The test takes longer now because of some changes you have made.  In this case you you "
						+ "can either optimize to reduce the execution time, or talk to the project team about "
						+ "raising the current baseline for this test.\n"
						+ "OR. You are using the default baseline. To get around this, save the file which was just "
						+ "written to the output directory into the unix4j-perf/src/test/resources directory.";
		fail(failureMessage);
	}

	private String getTestName(){
		final StackTraceElement callerStackTraceElement = StackTraceUtil.getCurrentMethodStackTraceElement(2);
		final String callerClass  = callerStackTraceElement.getClassName();
		final String callerMethod = callerStackTraceElement.getMethodName();
		return callerClass + "." + callerMethod;
	}

	private long runCommand(final Unix4jCommandBuilder command) {
		startTimer();
		command.toDevNull();
		return getRunningTime();
	}

	private void printExecutionTimeSummary(String testName,
										   Unix4jCommandBuilder command,
										   long currentExecutionTime,
										   TestBaseline unix4jBaseline,
										   TestBaseline linuxBaseline) {

		System.out.println("============================================================");
		System.out.println(format("Test name...........................%s", testName));
		System.out.println(format("Command.............................%s", command.toString()));
		System.out.println(format("Execution time - Current............%s ms", currentExecutionTime));
		System.out.println(format("Execution time - Unix4jBaseline.....%s ms", unix4jBaseline.getExecutionTime()));
		System.out.println(format("Execution time - LinuxEquivalent....%s ms", linuxBaseline.getExecutionTime()));
		System.out.println(format("Current to Baseline %%...............%s %%", Float.valueOf((1.0f * currentExecutionTime / unix4jBaseline.getExecutionTime()) * 100).intValue()));
		System.out.println(format("Current to Linux %%..................%s %%", Float.valueOf((1.0f * currentExecutionTime / linuxBaseline.getExecutionTime()) * 100).intValue()));
		System.out.println();
	}

	private long getRunningTime() {
		return (new Date()).getTime() - timeStarted;
	}
}
