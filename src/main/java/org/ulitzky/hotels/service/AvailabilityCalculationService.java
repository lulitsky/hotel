package org.ulitzky.hotels.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.ulitzky.hotels.model.RoomUsageData;
import org.ulitzky.hotels.model.enums.RoomType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AvailabilityCalculationService {

	public Map<RoomType, RoomUsageData> calculateAvailability(
			final List<Integer> willingToPayAmounts, 
			final Map<RoomType, Integer> availableRooms ) 
	{
		Map<Boolean,List<Integer>> splitForPremiumMap = 
				willingToPayAmounts.stream().collect(Collectors.partitioningBy(amt -> amt >= RoomType.PREMIUM.getMinAmountToEnsure()));
		
		int availablePremiumRooms = availableRooms.get(RoomType.PREMIUM);
		int availableEconomyRooms = availableRooms.get(RoomType.ECONOMY);
		
		List<Integer> premiumPayments = getHighestPayments(splitForPremiumMap.get(true), availablePremiumRooms);
		List<Integer> economyPayments = splitForPremiumMap.get(false);
		
		if ((premiumPayments.size() < availablePremiumRooms) && (economyPayments.size() > availableEconomyRooms) ) {
			// can upgrade
			int numberOfUpgrades = Math.min(availablePremiumRooms - premiumPayments.size(), (economyPayments.size() - availableEconomyRooms));
			List<Integer> upgradesPayments = getHighestPayments(economyPayments, numberOfUpgrades);
			premiumPayments.addAll(upgradesPayments);
			economyPayments.removeAll(upgradesPayments);
		}
		
		Map<RoomType, RoomUsageData> result = new HashMap<>();
		result.put(RoomType.PREMIUM, RoomUsageData.fromAmountsList(premiumPayments));
		result.put(RoomType.ECONOMY, RoomUsageData.fromAmountsList(getHighestPayments(economyPayments, availableEconomyRooms)));

		return result;
	}
	
	/*
	 * Given list of integers, returns subList of N highest values in it
	 * 
	 * @param - paymentsList original List
	 * @resultSize - size of list to return
	 * 
	 * @return - List<Integer> of size resultSize
	 */
	private static List<Integer> getHighestPayments(final List<Integer> paymentsList, final int resultSize) {
		List<Integer> resultList = new LinkedList<>(paymentsList);
		Collections.sort(resultList, Collections.reverseOrder());
	    if (resultList.size() > resultSize) {
	    	resultList = resultList.subList(0, resultSize);
	    }
		return resultList;
	}
	
}
