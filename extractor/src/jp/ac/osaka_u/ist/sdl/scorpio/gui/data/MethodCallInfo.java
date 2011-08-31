package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import java.util.concurrent.atomic.AtomicInteger;

public class MethodCallInfo implements Comparable<MethodCallInfo> {

	private static AtomicInteger ID_GENERATER = new AtomicInteger(0);

	public MethodCallInfo() {
		this.id = ID_GENERATER.getAndIncrement();
	}

	public void setCallerID(final int callerID) {
		this.callerID = callerID;
	}

	public int getCallerID() {
		return this.callerID;
	}

	public void setCalleeID(final int calleeID) {
		this.calleeID = calleeID;
	}

	public int getCalleeID() {
		return this.calleeID;
	}

	public void setFromLine(final int fromLine) {
		this.fromLine = fromLine;
	}

	public int getFromLine() {
		return this.fromLine;
	}

	public void setFromColumn(final int fromColumn) {
		this.fromColumn = fromColumn;
	}

	public int getFromColumn() {
		return this.fromColumn;
	}

	public void setToLine(final int toLine) {
		this.toLine = toLine;
	}

	public int getToLine() {
		return this.toLine;
	}

	public void setToColumn(final int toColumn) {
		this.toColumn = toColumn;
	}

	public int getToColumn() {
		return this.toColumn;
	}

	@Override
	public int compareTo(MethodCallInfo o) {
		if (this.id < o.id) {
			return -1;
		} else if (this.id > o.id) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "";
	}

	private int callerID;
	private int calleeID;
	private int fromLine;
	private int fromColumn;
	private int toLine;
	private int toColumn;

	private final int id;
}
