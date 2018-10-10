package org.ulitzky.hotels.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.ulitzky.hotels.model.RoomUsageData;
import org.ulitzky.hotels.model.enums.RoomType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Parameterized.class)
public class AvailabilityCalculationServiceTest {

	
	private AvailabilityCalculationService service = new AvailabilityCalculationService();
	
	@Parameter(0)
	public int availablePremium;
	@Parameter(1)
	public int availableEconomy;
	@Parameter(2)
	public int expectedPremiumNumber;
	@Parameter(3)
	public int expectedPremiumAmount;
	@Parameter(4)
	public int expectedEconomyNumber;
	@Parameter(5)
	public int expectedEconomyAmount;
	
	private List<Integer> willingToPayAmounts;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		service = new AvailabilityCalculationService();
		InputStream is = AvailabilityCalculationServiceTest.class.getResourceAsStream("/willing_to_pay.json");
		ObjectMapper om = new ObjectMapper();
		willingToPayAmounts = om.readValue(is, List.class);
	}
	
    @Parameterized.Parameters(name 
            = "{index}: Test with Premium ={0}, Economy={1}, expected result: Premium{2} ({3} EUR), Economy = {4} ({5} UER)")
    public static Iterable<Object[]> data() {
            return Arrays.asList(new Object[][] {
                {3,3,3,738, 3,167},
                {7,5,6,1054,4,189},
                {2,7,2,583, 4,189},
                {7,1,7,1153,1,45}, 
                {0,0, 0,0, 0,0},
                {0,2, 0,0, 2,144},
                {2,0, 2,583, 0,0}
            });
    }
    
    
    @Test
    public void testCalculateAvailabilityValidInput() {
    	Map<RoomType, Integer> availableRooms = new HashMap<>();
    	availableRooms.put(RoomType.ECONOMY, availableEconomy);
    	availableRooms.put(RoomType.PREMIUM, availablePremium);
    	
    	Map<RoomType, RoomUsageData> result = service.calculateAvailability(willingToPayAmounts, availableRooms );
    	
    	assertEquals(2, result.size());
    	assertEquals(expectedEconomyNumber, result.get(RoomType.ECONOMY).getNumberOfRooms());
    	assertEquals(expectedEconomyAmount, result.get(RoomType.ECONOMY).getTotalIncome());
    	assertEquals(expectedPremiumNumber, result.get(RoomType.PREMIUM).getNumberOfRooms());
    	assertEquals(expectedPremiumAmount, result.get(RoomType.PREMIUM).getTotalIncome());
    	
    }
            
}
