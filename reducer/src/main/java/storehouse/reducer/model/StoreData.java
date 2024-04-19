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
	String sensorId;
	Double occupation;
	
	@Override
	public int hashCode() {
		return Objects.hashCode(sensorId);
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
		return sensorId == other.sensorId;
	}
	
	public StoreDataDto toDto() {
		return new StoreDataDto(sensorId, occupation);
	}
	
	static public StoreData fromDto(StoreDataDto storeDataDto) {
		return new StoreData(storeDataDto.id(), storeDataDto.occupation()); 
	}
	
}
