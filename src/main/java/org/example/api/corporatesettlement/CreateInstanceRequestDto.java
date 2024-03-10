package org.example.api.corporatesettlement;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.enums.CoefficientAction;
import org.example.enums.RateType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInstanceRequestDto {

    @Schema(description = "Идентификатор экземпляра продукта", nullable = true)
    private Long instanceId;

    @NotEmpty
    @Schema(description = "Тип Экземпляра Продукта")
    private String productType;

    @NotEmpty
    @Schema(description = "Код продукта в каталоге продуктов")
    private String productCode;

    @NotEmpty
    @Schema(description = "Тип регистра")
    private String registerType;

    @NotEmpty
    @Schema(description = "Код Клиента (mdm)")
    private String mdmCode;

    @NotEmpty
    @Schema(description = "Номер договора")
    private String contractNumber;

    @NotNull
    @Schema(description = "Дата заключения договора обслуживания")
    private LocalDate contractDate;

    @NotNull
    @Schema(description = "Приоритет")
    private Long priority;

    @Schema(description = "Штрафная процентная ставка", nullable = true)
    private BigDecimal interestRatePenalty;

    @Schema(description = "Неснижаемый остаток", nullable = true)
    private BigDecimal minimalBalance;

    @Schema(description = "Пороговая сумма", nullable = true)
    private BigDecimal thresholdAmount;

    @Schema(description = "Реквизиты выплаты", nullable = true)
    private String accountingDetails;

    @Schema(description = "Выбор ставки в зависимости от суммы", nullable = true)
    private RateType rateType;

    @Schema(description = "Ставка налогообложения", nullable = true)
    private BigDecimal taxPercentageRate;

    @Schema(description = "Сумма лимита технического овердрафта", nullable = true)
    private BigDecimal technicalOverdraftLimitAmount;

    @NotNull
    @Schema(description = "ID Договора")
    private Long contractId;

    @NotEmpty
    @Schema(description = "Код филиала")
    @JsonAlias({"branchCode", "BranchCode"})
    private String branchCode;

    @NotEmpty
    @Size(max = 3)
    @Schema(description = "Код валюты", maxLength = 3)
    @JsonAlias({"IsoCurrencyCode", "isoCurrencyCode"})
    private String isoCurrencyCode;

    @NotEmpty
    @Schema(description = "Код срочности договора")
    private String urgencyCode;

    @Schema(description = "Код точки продаж")
    @JsonAlias({"ReferenceCode", "referenceCode"})
    private Long referenceCode;

    @Schema(description = "Массив дополнительных признаков для сегмента КИБ(VIP)")
    //TODO change to meet requirements?
    @Builder.Default
    private List<AdditionalProperty> additionalProperties = new ArrayList<>();

    @Schema(description = "массив Доп.Соглашений")
    @Builder.Default
    private List<InstanceArrangement> instanceArrangements = new ArrayList<>();

    @Builder
    @Getter
    public static class AdditionalProperty {
        //TODO change to meet requirements?
        private String key;
        private String value;
    }

    @Builder
    @Getter
    public static class InstanceArrangement {
        @Schema(description = "ID доп.Ген.соглашения", nullable = true)
        @JsonAlias({"GeneralAgreementId", "generalAgreementId"})
        private String generalAgreementId;

        @Schema(description = "ID доп.соглашения", nullable = true)
        @JsonAlias({"SupplementaryAgreementId", "supplementaryAgreementId"})
        private String supplementaryAgreementId;

        @Schema(description = "Тип соглашения", nullable = true)
        private String arrangementType;

        @Schema(description = "Идентификатор периодичности учета", nullable = true)
        private Long shedulerJobId;

        @NotEmpty
        @Schema(description = "Номер ДС")
        @JsonAlias({"Number", "number"})
        private String number;

        @NotNull
        @Schema(description = "Дата начала сделки")
        private LocalDate openingDate;

        @Schema(description = "Дата окончания сделки", nullable = true)
        private LocalDate closingDate;

        @Schema(description = "Дата отзыва сделки", nullable = true)
        @JsonAlias({"CancelDate", "cancelDate"})
        private LocalDate cancelDate;

        @Schema(description = "Срок действия сделки", nullable = true)
        private Long validityDuration;

        @Schema(description = "Причина расторжения", nullable = true)
        private String cancellationReason;

        @Schema(description = "Состояние/статус", nullable = true)
        @JsonAlias({"Status", "status"})
        private String status;

        @Schema(description = "Начисление начинается с (дата)", nullable = true)
        private LocalDate interestCalculationDate;

        @Schema(description = "Процент начисления на остаток", nullable = true)
        private BigDecimal interestRate;

        @Schema(description = "Коэффициент", nullable = true)
        private BigDecimal coefficient;

        @Schema(description = "Действие коэффициента", nullable = true)
        private CoefficientAction coefficientAction;

        @Schema(description = "Минимум по ставке", nullable = true)
        private BigDecimal minimumInterestRate;

        @Schema(description = "Коэффициент по минимальной ставке", nullable = true)
        private String minimumInterestRateCoefficient;

        @Schema(description = "Действие коэффициента по минимальной ставке", nullable = true)
        private CoefficientAction minimumInterestRateCoefficientAction;

        @Schema(description = "Максимум по ставке", nullable = true)
        private BigDecimal maximalInterestRate;

        @Schema(description = "Коэффициент по максимальной ставке", nullable = true)
        private BigDecimal maximalInterestRateCoefficient;

        @Schema(description = "Действие коэффициента по максимальной ставке", nullable = true)
        private CoefficientAction maximalInterestRateCoefficientAction;
    }
}
