package storehouse.reducer.service;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import storehouse.reducer.dto.StoreDataDto;
import storehouse.reducer.model.StoreData;
import storehouse.reducer.repo.StoreDataRepo;

@Service
@Slf4j
public class ContainerOccupationServiceImpl implements ContainerOccupationService {

	@Autowired
	StoreDataRepo storeDataRepo;

	@Override
	public StoreDataDto getChangedOccupationLevelFor(StoreDataDto currentDataDto) {
		AtomicReference<StoreDataDto> resultStoreDataDtoRef = new AtomicReference<>(null);
		storeDataRepo
			.findById(currentDataDto.name())
			.ifPresentOrElse(
				(storedStoreData) -> {
					StoreDataDto storedStoreDataDto = storedStoreData.toDto();
					log.trace("ContainerOccupationServiceImpl - getChangedOccupationLevelFor: ServiceStoreData from DB: {}", storedStoreDataDto);
					if(!storedStoreDataDto.equals(currentDataDto)) {
						resultStoreDataDtoRef.set(storeDataRepo.save(StoreData.fromDto(currentDataDto)).toDto());
					}
				},
				() -> {
					log.debug("ContainerOccupationServiceImpl - getChangedOccupationLevelFor: There was no StoreData in DB");
					resultStoreDataDtoRef.set(storeDataRepo.save(StoreData.fromDto(currentDataDto)).toDto());
				}
			);	
		return resultStoreDataDtoRef.get();
	}
}
