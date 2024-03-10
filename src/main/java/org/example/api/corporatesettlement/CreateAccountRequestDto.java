package org.example.api.corporatesettlement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequestDto {

    @NotNull
    @Schema(description = "Идентификатор экземпляра продукта")
    private Long instanceId;

    @Schema(description = "Тип регистра", nullable = true)
    private String registryTypeCode;

    @Schema(description = "Тип счета", nullable = true)
    private String accountType;

    @Size(max = 3)
    @Schema(description = "Код валюты", maxLength = 3, nullable = true)
    private String currencyCode;

    @Schema(description = "Код филиала", nullable = true)
    private String branchCode;

    @Schema(description = "Код срочности", nullable = true)
    private String priorityCode;

    @Pattern(regexp = "^\\d*$")
    @Schema(description = "Id клиента", pattern = "^\\d*$", nullable = true)
    private String mdmCode;

    @Schema(description = "Код клиента", nullable = true)
    private String clientCode;

    @Schema(description = "Регион принадлежности железной дороги", nullable = true)
    private String trainRegion;

    @Schema(description = "Счетчик", nullable = true)
    private String counter;

    @Schema(description = "Код точки продаж", nullable = true)
    private String salesCode;
}
