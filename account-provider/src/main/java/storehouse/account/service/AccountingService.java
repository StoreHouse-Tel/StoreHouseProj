package storehouse.account.service;

import java.util.List;

import storehouse.account.dto.AccountDto;

public interface AccountingService {
	
	AccountDto getAccount(String email);
	
	List<AccountDto> getAccounts();
	
	AccountDto createAccount(AccountDto accountDto);

	AccountDto removeAccount(String email);
	
	AccountDto updateAccount(AccountDto accountDto);



}
