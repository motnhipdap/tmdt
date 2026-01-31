package com.dev.dungcony.modules.users.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.users.dtos.AddressDto;
import com.dev.dungcony.modules.users.dtos.req.AddressAddReq;
import com.dev.dungcony.modules.users.services.interfaces.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<ApiRes<AddressDto>> addAddress(
            @Valid @RequestBody AddressAddReq req) {
        return ResponseEntity.ok()
                .body(ApiRes.success("add address success", addressService.addAddress(req)));
    }

    @PutMapping("/update-by-id")
    public ResponseEntity<ApiRes<AddressDto>> updateAddress(
            @Valid @RequestBody AddressDto req
    ) {
        return ResponseEntity.ok()
                .body(ApiRes.success("updated", addressService.updateAddress(req)));
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<ApiRes<Void>> deleteAddress(
            @Valid @RequestParam Integer id
    ) {
        
        addressService.deleteAddress(id);

        return ResponseEntity.ok()
                .body(ApiRes.success("deleted"));
    }

}
