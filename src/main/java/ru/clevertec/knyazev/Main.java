package ru.clevertec.knyazev;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ru.clevertec.knyazev.client.Client;
import ru.clevertec.knyazev.collection.ConcurrentList;

public class Main {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		ConcurrentList<Integer> list = new ConcurrentList<>();
//		
//		for (int i = 0; i < 1000; i++) {
//			list.add(i);
//		}
//		
//		Client client = new Client(list);
//		
//		ExecutorService executorService = Executors.newFixedThreadPool(8);
//		
//		while (list.size() > 0) {
//			Future<Integer> result = executorService.submit(client);
//			System.out.println(result.get());
//		}
//	    
//		executorService.shutdown();
//		
//		System.out.println("List size check: " + list.size());
		
	}
	
}
