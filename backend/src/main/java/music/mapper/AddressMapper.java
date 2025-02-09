package music.mapper;

import music.dto.request.AddressRequest;
import music.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest request);
    void updateAddress(@MappingTarget Address address, AddressRequest request);
}
