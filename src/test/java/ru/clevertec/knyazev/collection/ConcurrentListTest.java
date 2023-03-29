package ru.clevertec.knyazev.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//TODO finish tests
public class ConcurrentListTest {
	ConcurrentList<Integer> concurrentList;
	
	@BeforeEach
	public void setUp() {
		concurrentList = new ConcurrentList<>();
	}
	
	@Test
	public void checkSizeShouldReturnListSize() throws InterruptedException {
		int expectedSize = 4;
		
		ExecutorService es = Executors.newFixedThreadPool(3);		
		
		IntStream.range(0, expectedSize).forEach(val -> es.execute(() -> concurrentList.add(val)));
		
		es.shutdown();
		es.awaitTermination(10, TimeUnit.SECONDS);
		
		
		int actualSize = concurrentList.size();
		
		assertThat(actualSize).isEqualTo(expectedSize);
	}
	
	@Test
	public void checkSizeShouldReturnZeroListSize() {	
		concurrentList.add(12);
		concurrentList.remove(0);
		
		assertThat(concurrentList.size()).isEqualTo(0);
	}
	
	
	
}
