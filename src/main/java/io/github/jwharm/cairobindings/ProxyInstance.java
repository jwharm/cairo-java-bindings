package io.github.jwharm.cairobindings;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.ref.Cleaner;

/**
 * Base type for a Java proxy object to an instance in native memory.
 */
public abstract class ProxyInstance implements Proxy {

	{
		Interop.ensureInitialized();
	}

	private MemorySegment address;
	private DestroyRunner destroyRunner;
	private static final Cleaner CLEANER = Cleaner.create();
	private Cleaner.Cleanable cleanable;
	
	/**
	 * Create a new {@code ProxyInstance} object for an instance in native memory.
	 * 
	 * @param address the memory address of the instance
	 */
	public ProxyInstance(MemorySegment address) {
		this.address = address;
		this.destroyRunner = new DestroyRunner(address);
		this.cleanable = CLEANER.register(this, destroyRunner);
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
		destroyRunner.owned = true;
	}

	/**
	 * Yield ownership of the native instance. When the Java proxy object is
	 * garbage-collected, the native instance will not be destroyed.
	 */
	public void yieldOwnership() {
		destroyRunner.owned = false;
	}

	/**
	 * Register the name of the function that will be used to destroy a native
	 * instance. The function must return void, and take exactly one parameter: the
	 * memory address of the instance.
	 * 
	 * @param func Name of the destroy-function in native code
	 */
	protected void setDestroyFunc(String func) {
		destroyRunner.destroyFunc = func;
	}

	/**
	 * Run the action that destroys the native instance. This action is guaranteed
	 * to run only once, and will only run if the object is currently owned by the
	 * JVM (see {@link #takeOwnership()}.
	 * <p>
	 * Under normal circumstances, you should <strong>not</strong> call this method.
	 * It is called by a {@link Cleaner} during garbage collection.
	 * <p>
	 * You don't have to free native resources manually; this is automatically
	 * handled during garbage collection. However, if waiting for the GC causes
	 * problems with excessive native memory consumption, {@link #destroy()} will
	 * allow you to free specific instances immediately.
	 * <p>
	 * You cannot use the Java object instance anymore after running
	 * {@link #destroy()}. To prevent mistakes, it is recommended to assign
	 * {@code null} immediately after {@link #destroy()}:
	 * 
	 * <pre>
	 * surface.destroy();
	 * surface = null;
	 * </pre>
	 */
	public void destroy() {
		cleanable.clean();
		address = null;
	}

	/**
	 * A closure that is run by the Cleaner when this ProxyInstance is
	 * garbage-collected. When a destroy-function name has been registered, and
	 * ownership is set to true, the destroy-function will be executed for the
	 * memory address of the native instance.
	 */
	private static final class DestroyRunner implements Runnable {

		private final MemorySegment address;
		public boolean owned;
		public String destroyFunc;

		public DestroyRunner(MemorySegment address) {
			this.address = address;
			this.owned = false;
		}

		public void run() {
			if (owned && (destroyFunc != null)) {
				try {
					Interop.downcallHandle(destroyFunc, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS), false)
							.invoke(address);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
