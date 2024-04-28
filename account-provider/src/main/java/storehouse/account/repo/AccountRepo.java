package storehouse.account.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import storehouse.account.model.Account;

public interface AccountRepo extends MongoRepository<Account, String> {}