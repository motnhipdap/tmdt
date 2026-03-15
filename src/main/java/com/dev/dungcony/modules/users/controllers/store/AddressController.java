package com.dev.dungcony.modules.users.controllers.store;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.users.dtos.AddressRes;
import com.dev.dungcony.modules.users.dtos.req.AddressAddReq;
import com.dev.dungcony.modules.users.dtos.req.AddressUpdateReq;
import com.dev.dungcony.modules.users.services.interfaces.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Address")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/address")
public class AddressController {

        private final AddressService addressService;

        @Operation(summary = "Add new address")
        @PostMapping("/add-new")
        public ResponseEntity<ApiRes<AddressRes>> addAddress(
                        @Valid @RequestBody AddressAddReq req) {
                return ResponseEntity.ok()
                                .body(ApiRes.success("add address success", addressService.addNew(req)));
        }

        @Operation(summary = "Update New")
        @PutMapping("/update")
        public ResponseEntity<ApiRes<AddressRes>> updateAddress(
                        @Valid @RequestBody AddressUpdateReq req) {
                return ResponseEntity.ok()
                                .body(ApiRes.success("updated", addressService.update(req)));
        }

        @Operation(summary = "Delete address")
        @DeleteMapping("/delete")
        public ResponseEntity<ApiRes<Void>> deleteAddress(
                        @Valid @RequestParam Integer id) {

                addressService.delete(id);

                return ResponseEntity.ok()
                                .body(ApiRes.success("deleted"));
        }

}
