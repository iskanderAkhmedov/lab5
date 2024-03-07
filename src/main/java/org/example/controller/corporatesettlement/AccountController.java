package org.example.controller.corporatesettlement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.api.corporatesettlement.CreateAccountRequestDto;
import org.example.api.corporatesettlement.CreateAccountResponseDto;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = {"${api-url}/corporate-settlement-account"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    @PostMapping("/create")
    public CreateAccountResponseDto create(
            @RequestBody @Valid CreateAccountRequestDto req
            ) {
        return null;
    }
}
