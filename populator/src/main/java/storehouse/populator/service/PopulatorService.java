package storehouse.populator.service;

import java.util.Optional;


import storehouse.populator.dto.StoreDataDto;

public interface PopulatorService {

	Optional<Integer> putCurrentPercentage(StoreDataDto storeDataDto);
	
}
