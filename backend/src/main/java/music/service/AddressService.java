package music.service;

import music.dto.request.AddressRequest;
import music.model.Address;

import java.util.List;

public interface AddressService {
    long addAddress(AddressRequest request);
    Address getAddress(Long id);
    List<Address> getAllAddress();
    long updateAddress(Long id, AddressRequest request);
    long deleteAddress(Long id);
}
