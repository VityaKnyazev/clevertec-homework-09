package ru.clevertec.knyazev.collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		es.awaitTermination(5, TimeUnit.SECONDS);
		
		
		int actualSize = concurrentList.size();
		
		assertThat(actualSize).isEqualTo(expectedSize);
	}
	
	@Test
	public void checkSizeShouldReturnZeroListSize() {	
		concurrentList.add(12);
		concurrentList.remove(0);
		
		assertThat(concurrentList.size()).isEqualTo(0);
	}
	
	@Test
	public void checkGetShouldReturnListElement() {
		int expectedEl = 8;
		
		IntStream.range(0, 51).forEach(val -> concurrentList.add(val));
	
		int inputIndex = 8;
		int actualEl = concurrentList.get(inputIndex);
		
		assertThat(actualEl).isEqualTo(expectedEl);
	}
	
	@Test
	public void checkGetShouldThrowNoSuchElException() {		
		IntStream.range(0, 51).forEach(val -> concurrentList.add(val));
	
		int inputIndex = 51;
		
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> concurrentList.get(inputIndex));
	}
	
	@Test
	public void checkRemoveShouldRemoveAllElements() throws InterruptedException {		
		IntStream.range(1, 101).forEach(val ->  concurrentList.add(val));
		
		ExecutorService es = Executors.newFixedThreadPool(3);		
		
		while (concurrentList.size() > 0) {
			es.submit(() -> concurrentList.remove(new Random().nextInt(0, concurrentList.size())));
		}
		
		es.shutdown();
		
		assertThat(concurrentList.size()).isZero();
	}
	
	@Test
	public void checkRemoveShoulRThrowIllegalStateExceptionWhenEmptyCollection() {
		int inputIndex = 10;
		
		assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> concurrentList.remove(inputIndex));
	}
	
	@Test
	public void checkRemoveShouldThrowNoSuchElementExceptionWhenBadIndex() {
		int inputIndex = 1;
		
		concurrentList.add(12);
		
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> concurrentList.remove(inputIndex));
	}
	
	@Test
	public void checkContainsShouldReturnTrue() {
		int val = 25;
		
		concurrentList.add(val);
		
		int inputVal = 25;
		assertThat(concurrentList.contains(inputVal)).isTrue();
	}
	
	@Test
	public void checkContainsShouldReturnFalse() {
		int val = 25;
		
		concurrentList.add(val);
		
		int inputVal = 12;
		assertThat(concurrentList.contains(inputVal)).isFalse();
	}
}
