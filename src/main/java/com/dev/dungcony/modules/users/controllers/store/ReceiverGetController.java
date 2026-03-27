package com.dev.dungcony.modules.users.controllers.store;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.users.dtos.res.ReceiverRes;
import com.dev.dungcony.modules.users.services.interfaces.RecieverGetService;
import com.dev.dungcony.modules.users.services.interfaces.UserGetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Receiver")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/user/receiver/get")
public class ReceiverGetController {

    private final RecieverGetService recieverGetService;
    private final UserGetService userGetService;

    @Operation(summary = "Get my receivers")
    @GetMapping("/all")
    public ResponseEntity<ApiRes<List<ReceiverRes>>> getAll(
            @AuthenticationPrincipal AccountDetails details) {
        return ResponseEntity.ok(
                ApiRes.success("receivers", recieverGetService.getAllByUser(getUserId(details))));
    }

    @Operation(summary = "Get receiver by id")
    @GetMapping
    public ResponseEntity<ApiRes<ReceiverRes>> getById(
            @AuthenticationPrincipal AccountDetails details,
            @RequestParam Integer receiverId) {
        return ResponseEntity.ok(
                ApiRes.success("receiver", recieverGetService.getReceiverById(getUserId(details), receiverId)));
    }

    private UUID getUserId(AccountDetails details) {
        return userGetService.getUserByAccId(details.getId()).id();
    }
}
