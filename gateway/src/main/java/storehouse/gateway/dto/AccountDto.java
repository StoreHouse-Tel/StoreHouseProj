package storehouse.gateway.dto;



public record AccountDto(String email, String password, String[] roles) {

}