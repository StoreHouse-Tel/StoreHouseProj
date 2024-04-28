package storehouse.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import storehouse.account.dto.AccountDto;
import storehouse.account.service.AccountingService;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("accounts")
public class AccountController {
	final AccountingService accountingService;	
	
	@GetMapping("/{email}")
	AccountDto getAccount(@PathVariable String email) {
		return accountingService.getAccount(email);
	}
	
	@GetMapping()
	List<AccountDto> getAccounts() {
		return accountingService.getAccounts();
	}
	
	@PostMapping()
	AccountDto createAccount(@RequestBody AccountDto accountDto) {
		return accountingService.createAccount(accountDto);
	}
	
	@PutMapping()
	AccountDto updateAccount(@RequestBody AccountDto accountDto) {
		return accountingService.updateAccount(accountDto);
	}
	@DeleteMapping()
	AccountDto deleteAccount(@RequestBody String email) {
		return accountingService.removeAccount(email);
	}
	
}
