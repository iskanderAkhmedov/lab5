package org.example.controller.corporatesettlement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.api.corporatesettlement.CreateInstanceRequestDto;
import org.example.api.corporatesettlement.CreateInstanceResponseDto;
import org.example.service.corporatesettlement.InstanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = {"${api-url}/corporate-settlement-instance"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class InstanceController {

    private final InstanceService instanceService;

    @PostMapping("/create")
    public CreateInstanceResponseDto create(
            @RequestBody @Valid @NotNull CreateInstanceRequestDto req
    ) {
        return instanceService.createInstance(req);
    }
}
