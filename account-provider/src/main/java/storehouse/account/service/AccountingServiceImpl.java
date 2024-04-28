package storehouse.account.service;


import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import storehouse.account.dto.AccountDto;
import storehouse.account.exceptionHandler.NotFoundException;
import storehouse.account.model.Account;
import storehouse.account.repo.AccountRepo;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService {

	final AccountRepo accountRepo;
	final PasswordEncoder passwordEncoder;

	@Override
	public AccountDto getAccount(String email) {
		log.debug("getAccount method get email: {}", email);
		Account account = accountRepo.findById(email).orElseThrow(() ->
		new NotFoundException(String.format("account %s not found", email)));
		return account.buildSecretless();
	}

	@Override
	public List<AccountDto> getAccounts() {
		return accountRepo.findAll().stream().map((a) -> a.build()).toList();
	}

	@Override
	public AccountDto createAccount(AccountDto accountDto) {
		Optional<Account> accountOptional = accountRepo.findById(accountDto.email());
		Account account = null;
		accountOptional.ifPresent(__ -> {throw new IllegalStateException(String.format("account %s already exists", accountDto.email()));});
		AccountDto accountEncoded = getAccountDtoEncoded(accountDto);
		account = accountRepo.save(Account.of(accountEncoded));
		log.debug("account {} has been saved", account.build());
		return account.build();
	}

	private AccountDto getAccountDtoEncoded(AccountDto accountDto) {
		return new AccountDto(accountDto.email(),
				passwordEncoder.encode(accountDto.password()), accountDto.roles());
	}

	@Override
	public AccountDto removeAccount(String email) {
		AccountDto accountDto = getAccount(email); 
		accountRepo.delete(Account.of(accountDto));
		return accountDto;
	}

	@Override
	public AccountDto updateAccount(AccountDto accountDto) {
		getAccount(accountDto.email()); 
		return accountRepo.save(Account.of(accountDto)).build();
	}
}