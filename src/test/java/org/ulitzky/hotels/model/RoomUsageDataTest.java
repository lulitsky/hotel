package org.ulitzky.hotels.model;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

public class RoomUsageDataTest {

	
	@Test
	public void testFromAmountsList() {
		List<Integer> inList = new LinkedList<>();
		
		int size = ThreadLocalRandom.current().nextInt(100);
		int sum = 0;
		for(int i=0; i < size; i++) {
			int next = ThreadLocalRandom.current().nextInt(10000);
			sum += next;
			inList.add(next);
		}
		
		RoomUsageData created = RoomUsageData.fromAmountsList(inList);
		assertNotNull(created);
		assertEquals(size, created.getNumberOfRooms());
		assertEquals(sum, created.getTotalIncome());
	}
	
}
