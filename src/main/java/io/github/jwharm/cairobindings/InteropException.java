package io.github.jwharm.cairobindings;

/**
 * This exception is thrown when an error occurs while calling a native function
 * or accessing native memory.
 */
public class InteropException extends RuntimeException {

	/**
	 * Create a new InteropException that wraps the provided Throwable
	 * @param cause the Throwable to wrap
	 */
	public InteropException(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a new InteropException with the provided message
	 * @param message the error message
	 */
	public InteropException(String message) {
		super(message);
	}
}