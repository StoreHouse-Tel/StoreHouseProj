package storehouse.account.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import storehouse.account.dto.AccountDto;

@Document(collection="storehouse-accounts")
@Getter
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class Account {
	@Id
	String email;
	String hashPassword;
	String [] roles;
	
	public static Account of(AccountDto accountDto) {
		return new Account(accountDto.email(), accountDto.password(), accountDto.roles());
	}
	
	public AccountDto buildSecretless() {
		return new AccountDto(email, hashPassword, roles);
	}
	
	public AccountDto build() {
		return new AccountDto(email, "********", roles);
	}
}