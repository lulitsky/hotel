package org.ulitzky.hotels.api.v1.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ulitzky.hotels.api.exception.NegativeInputParameterException;
import org.ulitzky.hotels.api.v1.resource.RoomTypeUsageResource;
import org.ulitzky.hotels.model.RoomUsageData;
import org.ulitzky.hotels.model.enums.RoomType;
import org.ulitzky.hotels.service.AvailabilityCalculationService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/rooms/usage")
@Slf4j
public class RoomUsageController {

	@Autowired
	private AvailabilityCalculationService availabilityCalculationService; 
	
    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    public List<RoomTypeUsageResource> getFlightPlan(@RequestParam(name = "freePremium", required=false, defaultValue = "0") final Integer freePremiumRooms, 
    												 @RequestParam(name = "freeEconomy", required=false, defaultValue = "0") final Integer freeEconomyRooms,
    												 @RequestParam(name = "willingToPay", required=false) final List<Integer> willingToPayInput)
    																throws NegativeInputParameterException {
    	
    	List<Integer> willingToPay = (willingToPayInput == null) ? new LinkedList<>() : willingToPayInput;
    	validateInputParams(freePremiumRooms, freeEconomyRooms, willingToPayInput);
        log.info("Getting rooms usage for free Premium = {}, free Economy = {}, willing to pay list {}", freePremiumRooms, freeEconomyRooms, willingToPay);
        
        Map<RoomType, Integer> availableRoomsMap = new HashMap<>();
        availableRoomsMap.put(RoomType.PREMIUM, freePremiumRooms);
        availableRoomsMap.put(RoomType.ECONOMY, freeEconomyRooms);
        Map<RoomType, RoomUsageData> usageMap = availabilityCalculationService.calculateAvailability(willingToPay, availableRoomsMap); 
        
        return buildUsageResourceList(usageMap);
    }
    
    private void validateInputParams(final Integer freePremiumRooms, final Integer freeEconomyRooms, final List<Integer> willingToPayInput) throws NegativeInputParameterException{
		if (freePremiumRooms < 0) {
			throw new NegativeInputParameterException("freePremium", freePremiumRooms);
		}
		if (freeEconomyRooms < 0) {
			throw new NegativeInputParameterException("freeEconomy", freeEconomyRooms);
		}
		for (Integer  willingToPayAmount : willingToPayInput) {
			if (willingToPayAmount < 0) {
				throw new NegativeInputParameterException("willingToPay", willingToPayAmount);
			}
		}
	}

	private static  List<RoomTypeUsageResource> buildUsageResourceList(Map<RoomType, RoomUsageData> usageMap) {
    	
    	return usageMap.entrySet()
    			.stream()
    			.map(entry -> new RoomTypeUsageResource(entry.getKey().toString(), entry.getValue().getNumberOfRooms(), entry.getValue().getTotalIncome()))
    			.collect(Collectors.toList());
    }
}
