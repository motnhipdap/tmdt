package com.dev.dungcony.modules.users.controllers.store;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.users.dtos.AddressDto;
import com.dev.dungcony.modules.users.services.interfaces.AddressGetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Address")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/address/get")
public class AddressGetController {

    private final AddressGetService addrGet;

    @Operation(summary = "Get my address")
    @GetMapping("/gets")
    public ResponseEntity<ApiRes<List<AddressDto>>> getMyAddresses(
            @AuthenticationPrincipal AccountDetails details,
            @RequestParam UUID userId) {
        return ResponseEntity.ok()
                .body(ApiRes.success("my addresses", addrGet.getAddressByUserId(userId)));
    }

    @Operation(summary = "Get my address")
    @GetMapping("/get")
    public ResponseEntity<ApiRes<AddressDto>> getById(
            @AuthenticationPrincipal AccountDetails details,
            @RequestParam Integer addrId) {
        return ResponseEntity.ok()
                .body(ApiRes.success("address", addrGet.getAddressById(addrId)));
    }
}
