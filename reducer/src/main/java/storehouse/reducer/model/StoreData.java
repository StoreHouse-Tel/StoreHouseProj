package storehouse.reducer.model;

import java.text.ParseException;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import storehouse.reducer.dto.StoreDataDto;

@RedisHash
@NoArgsConstructor
@AllArgsConstructor
public class StoreData {
	@Id
	String name;
	Integer fillPercentage;
	
	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoreData other = (StoreData) obj;
		return name.equals(other.name);
	}
	
	public StoreDataDto toDto() {
		return new StoreDataDto(name, fillPercentage);
	}
	
	static public StoreData fromDto(StoreDataDto storeDataDto) {
		return new StoreData(storeDataDto.name(), storeDataDto.fillPercentage()); 
	}
	
}
