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

    @Schema(description = "Тип регистра")
    private String registryTypeCode;

    @Schema(description = "Тип счета")
    private String accountType;

    @Size(max = 3)
    @Schema(description = "Код валюты", maxLength = 3)
    private String currencyCode;

    @Schema(description = "Код филиала")
    private String branchCode;

    @Schema(description = "Код срочности")
    private String priorityCode;

    @Pattern(regexp = "^\\d*$")
    @Schema(description = "Id клиента", pattern = "^\\d*$")
    private String mdmCode;

    @Schema(description = "Код клиента")
    private String clientCode;

    @Schema(description = "Регион принадлежности железной дороги")
    private String trainRegion;

    @Schema(description = "Счетчик")
    private String counter;

    @Schema(description = "Код точки продаж")
    private String salesCode;
}
