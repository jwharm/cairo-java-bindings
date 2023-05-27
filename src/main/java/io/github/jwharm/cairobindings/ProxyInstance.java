package io.github.jwharm.cairobindings;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.ref.Cleaner;

/**
 * Base type for a Java proxy object to an instance in native memory.
 */
public abstract class ProxyInstance implements Proxy {

	private MemorySegment address;
	private Dereferencer dereferencer;
	private static final Cleaner CLEANER = Cleaner.create();

	/**
	 * Create a new {@code ProxyInstance} object for an instance in native memory.
	 * 
	 * @param address the memory address of the instance
	 */
	public ProxyInstance(MemorySegment address) {
		this.address = address;
		this.dereferencer = new Dereferencer(address);
		CLEANER.register(this, dereferencer);
	}

	/**
	 * Get the memory address of the instance
	 * 
	 * @return the memory address of the instance
	 */
	@Override
	public MemorySegment handle() {
		return address;
	}

	/**
	 * Private constructor to prevent instantiation without a memory address.
	 */
	@SuppressWarnings("unused")
	private ProxyInstance() {
	}

	/**
	 * Assume ownership of the native instance. When the Java proxy object is
	 * garbage-collected, the native instance will be destroyed as well.
	 */
	public void takeOwnership() {
		dereferencer.owned = true;
	}

	/**
	 * Yield ownership of the native instance. When the Java proxy object is
	 * garbage-collected, the native instance will not be destroyed.
	 */
	public void yieldOwnership() {
		dereferencer.owned = false;
	}

	/**
	 * Register the name of the function that will be used to destroy a native
	 * instance. The function must return void, and take exactly one parameter: the
	 * memory address of the instance.
	 * 
	 * @param func Name of the dereference-function in native code
	 */
	public void setDereferenceFunc(String func) {
		dereferencer.dereferenceCall = func;
	}

	/**
	 * A closure that is run by the Cleaner when this ProxyInstance is
	 * garbage-collected. When a dereference-function name has been registered, and
	 * ownership is set to true, the dereference-function will be executed for the
	 * memory address of the native instance.
	 */
	private static final class Dereferencer implements Runnable {

		private static final FunctionDescriptor fdesc = FunctionDescriptor.ofVoid(ValueLayout.ADDRESS);
		private final MemorySegment address;
		public boolean owned;
		public String dereferenceCall;

		public Dereferencer(MemorySegment address) {
			this.address = address;
		}

		public void run() {
			if (owned && (dereferenceCall != null)) {
				try {
					Interop.downcallHandle(dereferenceCall, fdesc, false).invoke(address);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
