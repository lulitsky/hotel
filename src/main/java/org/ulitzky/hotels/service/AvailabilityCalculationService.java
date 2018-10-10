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
		Map<RoomType, RoomUsageData> result = new HashMap<>();
		
		Map<Boolean,List<Integer>> splitForPremiumMap = 
				willingToPayAmounts.stream().collect(Collectors.partitioningBy(amt -> amt >= RoomType.PREMIUM.getMinAmountToEnsure()));
		
		int availablePremiumRoomsNumber = availableRooms.get(RoomType.PREMIUM);
		int availableEconomyRoomsNumber = availableRooms.get(RoomType.ECONOMY);
		
		List<Integer> premiumPayments = getHighestPayments(splitForPremiumMap.get(true), availablePremiumRoomsNumber);
		List<Integer> economyPayments = splitForPremiumMap.get(false);
		
		if ((premiumPayments.size() <= availablePremiumRoomsNumber) && (economyPayments.size() >= availableEconomyRoomsNumber) ) {
			// can upgrade
			int numberOfUpgrades = Math.min(availablePremiumRoomsNumber - premiumPayments.size(), (economyPayments.size() - availableEconomyRoomsNumber));
			List<Integer> upgradesPayments = getHighestPayments(economyPayments, numberOfUpgrades);
			premiumPayments.addAll(upgradesPayments);
			economyPayments.removeAll(upgradesPayments);
		}
		
		result.put(RoomType.PREMIUM, RoomUsageData.fromAmountsList(premiumPayments));
		result.put(RoomType.ECONOMY, RoomUsageData.fromAmountsList(getHighestPayments(economyPayments, availableEconomyRoomsNumber)));

		return result;
	}
	
	private static List<Integer> getHighestPayments(final List<Integer> paymentsList, final int resultSize) {
		List<Integer> resultList = new LinkedList<>(paymentsList);
		Collections.sort(resultList, Collections.reverseOrder());
	    if (resultList.size() > resultSize) {
	    	resultList = resultList.subList(0, resultSize);
	    }
		return resultList;
			
	}
	
}
