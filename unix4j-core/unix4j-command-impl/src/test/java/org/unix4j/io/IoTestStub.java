package org.unix4j.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class IoTestStub implements Input, Output {
	private static final RecordedActions recordedActions = new RecordedActions();
	private static boolean recording = false;
	private final List<String> lines = new LinkedList<String>();
	@SuppressWarnings("unused")
	private final String id;

	public enum ActionType{READ, WRITE;}

	static class Action{
		final IoTestStub instance;
		final ActionType actionType;
		final String line;
		Action(IoTestStub instance, ActionType actionType, String line) {
			this.instance = instance;
			this.actionType = actionType;
			this.line = line;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Action)) return false;

			Action action = (Action) o;

			if (actionType != action.actionType) return false;
			if (instance != null ? !instance.equals(action.instance) : action.instance != null) return false;
			if (line != null ? !line.equals(action.line) : action.line != null) return false;

			return true;
		}

		@Override
		public String toString() {
			return "Action{" +
					"instance=" + instance +
					", actionType=" + actionType +
					", line='" + line + '\'' +
					'}';
		}

	}

	public static class RecordedActions{
		private static final List<Action> actions = new ArrayList<Action>();
		private void recordAction(final IoTestStub instance, final ActionType actionType, final String line){
			actions.add(new Action(instance, actionType, line));
		}

		public RecordedActions expect(final IoTestStub instance, final ActionType actionType, final String line){
			final Action action = new Action(instance, actionType, line);
			assertTrue("Asserting expect: " + action + " but no more actions recorded.", actions.size() > 0);
			assertThat(actions.remove(0), is(action));
			return this;
		}

		public void clear(){
			actions.clear();
		}

		public void finish(){
			assertTrue("There should be no more actions recorded. " + actions, actions.isEmpty());
			recordedActions.clear();
		}
	}

	public IoTestStub(final String id) {
		this.id = id;
	}

	public static void startRecording(){
		recordedActions.clear();
		recording = true;
	}

	public static RecordedActions stopRecordingAndVerify(){
		recording = false;
		return recordedActions;
	}

	@Override
	public boolean hasMoreLines() {
		return (lines.size() > 0);
	}

	@Override
	public String readLine() {
		final String line = lines.remove(0);
		if(recording) recordedActions.recordAction(this, ActionType.READ, line);
		return line;
	}

	@Override
	public void writeLine(String line) {
		if(recording) recordedActions.recordAction(this, ActionType.WRITE, line);
		lines.add(line);
	}

	public IoTestStub addLine(String line) {
		writeLine(line);
		return this;
	}

	@Override
	public void finish() {
		//Do nothing
	}
}
