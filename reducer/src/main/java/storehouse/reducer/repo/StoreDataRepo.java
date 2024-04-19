package storehouse.reducer.repo;

import org.springframework.data.repository.CrudRepository;

import storehouse.reducer.model.StoreData;

public interface StoreDataRepo extends CrudRepository<StoreData, String> {

}
