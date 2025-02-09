package music.controller;

import music.dto.request.AddressRequest;
import music.dto.response.ApiResponse;
import music.model.Address;
import music.service.AddressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
@Tag(name = "Address Controller")
public class AddressController {
    private final AddressService addressService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> addAddress(@Valid @RequestBody AddressRequest request) {
        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Create address success",
                addressService.addAddress(request));
    }

    @GetMapping("/{addressId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Address> getAddress(@PathVariable @Min(1) long addressId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get address success",
                addressService.getAddress(addressId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Address>> getAllAddress() {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Get all address success",
                addressService.getAllAddress());
    }

    @PutMapping("/{addressId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> updateAddress(@PathVariable @Min(1) long addressId,
                                           @Valid @RequestBody AddressRequest request) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Update address success",
                addressService.updateAddress(addressId, request));
    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> deleteAddress(@PathVariable @Min(1) long addressId) {
        return new ApiResponse<>(HttpStatus.OK.value(),
                "Delete address success",
                addressService.deleteAddress(addressId));
    }
}
