package org.mendybot.announcer.fault;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * This class allows for the wrapping of an existing Throwable while maintaining
 * the original stack trace.
 * 
 * <pre>
 * <B>History
 * Date        Author         Description/Justification</B>
 * 11 Mar 2018 Brian Sorensen ENG-100 - Initial code.
 * </pre>
 */
public class ExecuteException extends Exception {
	private static final long serialVersionUID = 1847340170819345925L;
	private Throwable parent;

	/**
	 * This constructor creates an instance with the given message, and the
	 * stack trace will be calculated normally.
	 * 
	 * @param message
	 *            Information used to describe the nature of the Exception
	 */
	public ExecuteException(String message) {
		super(message);
	}

	/**
	 * This constructor creates an instance using the message and stack trace of
	 * the given parent Throwable
	 * 
	 * @param t
	 *            The parent Throwable used to obtain the message and stack
	 *            trace.
	 */
	public ExecuteException(Throwable t) {
		super(t.getMessage());
		parent = t;
	}

	/**
	 * This constructor creates an instance using the given message and stack
	 * trace from the given parent Throwable
	 * 
	 * @param message
	 *            Information used to describe the nature of the Exception
	 * @param t
	 *            The parent Throwable used to obtain the message and stack
	 *            trace.
	 */
	public ExecuteException(String message, Throwable t) {
		super(message);
		parent = t;
	}

	@Override
	public void printStackTrace(PrintStream ps) {
		if (parent == null) {
			super.printStackTrace(ps);
		} else {
			parent.printStackTrace(ps);
		}
	}

	@Override
	public void printStackTrace(PrintWriter pw) {
		if (parent == null) {
			super.printStackTrace(pw);
		} else {
			parent.printStackTrace(pw);
		}
	}

	@Override
	public void printStackTrace() {
		if (parent == null) {
			super.printStackTrace();
		} else {
			parent.printStackTrace();
		}
	}

}
