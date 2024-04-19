package storehouse.reducer.service;

import storehouse.reducer.dto.StoreDataDto;

public interface ContainerOccupationService {

	StoreDataDto getChangedOccupationLevelFor(StoreDataDto storeDataDto);
	
}
