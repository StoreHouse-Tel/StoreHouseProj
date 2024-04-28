package storehouse.account.dto;


public record AccountDto(String email, String password, String[] roles) {}