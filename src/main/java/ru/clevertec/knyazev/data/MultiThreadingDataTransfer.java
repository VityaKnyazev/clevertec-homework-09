package ru.clevertec.knyazev.data;

import java.util.concurrent.Callable;

/**
 * 
 * This interface represents multithreading methods for implementation in client and server classes
 * to achieve concurrency when sending request and receiving response.
 * 
 * @author Vitya Knyazev
 *
 * @param <P> type of producing result.
 * @param <C> type of consuming data.
 */
public interface MultiThreadingDataTransfer<P, C> {
	
	/**
	 * 
	 * Using for sending request in multithreading client or server
	 * 
	 * @return Callable<P> functional interface realization for using in executor service.
	 * 
	 */
	public Callable<P> send();
	
	
	/**
	 * 
	 * Using for receiving response in multithreading client or server.
	 * 
	 * @param data Future<C> the result of request or response of type C.
	 * @return Runnable functional interface realization for using in executor service.
	 */
	public Runnable receive(C data);
}
