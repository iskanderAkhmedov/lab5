package org.example.service.corporatesettlement;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.example.api.corporatesettlement.CreateAccountRequestDto;
import org.example.api.corporatesettlement.CreateInstanceRequestDto;
import org.example.enums.CoefficientAction;
import org.example.enums.CommonState;
import org.example.enums.RateType;
import org.example.storage.model.corporatesettlement.TppProductEntity;
import org.example.storage.repository.corporatesettlement.AccountPoolRepository;
import org.example.storage.repository.corporatesettlement.TppProductRepository;
import org.example.storage.repository.corporatesettlement.TppRefAccountTypeRepository;
import org.example.storage.repository.corporatesettlement.TppRefProductClassRepository;
import org.example.storage.repository.corporatesettlement.TppRefProductRegisterTypeRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.example.service.corporatesettlement.InstanceService.ACCOUNT_CODE_CLIENT;

@Component
@RequiredArgsConstructor
public class TestData {

    private final TppRefProductClassRepository tppRefProductClassRepository;
    private final AccountPoolRepository accountPoolRepository;
    private final TppRefProductRegisterTypeRepository tppRefProductRegisterTypeRepository;
    private final TppRefAccountTypeRepository tppRefAccountTypeRepository;
    private final TppProductRepository tppProductRepository;

    public CreateAccountRequestDto createAccountRequestDtoDefault(Long instanceId) {

        var accountPoolEntity = accountPoolRepository.findAll().get(0);

        return CreateAccountRequestDto.builder()
                .instanceId(instanceId)
                .registryTypeCode(accountPoolEntity.getRegistryTypeCode())
                .accountType(null)
                .currencyCode(accountPoolEntity.getCurrencyCode())
                .branchCode(accountPoolEntity.getBranchCode())
                .priorityCode(accountPoolEntity.getPriorityCode())
                .mdmCode(accountPoolEntity.getMdmCode())
                .clientCode(null)
                .trainRegion(null)
                .counter(null)
                .salesCode(null)
                .build();
    }

    public CreateInstanceRequestDto createInstanceRequestDtoForCreateInstanceDefault() {

        var productClassEntity = tppRefProductClassRepository.findAll().get(0);
        var accountTypeEntity = tppRefAccountTypeRepository.findByValue(ACCOUNT_CODE_CLIENT).get();
        var registerTypeEntityList = tppRefProductRegisterTypeRepository
                .findAllByProductClassAndAccountType(productClassEntity, accountTypeEntity);

        var accountPoolEntity = accountPoolRepository
                .findAllByRegistryTypeCode(registerTypeEntityList.get(0).getValue()).get(0);

        return CreateInstanceRequestDto.builder()
                .instanceId(null)
                .productType(RandomStringUtils.randomAlphanumeric(10))
                .productCode(productClassEntity.getValue())
                .registerType(accountPoolEntity.getRegistryTypeCode())
                .mdmCode(accountPoolEntity.getMdmCode())
                .contractNumber(RandomStringUtils.randomAlphanumeric(10))
                .contractDate(LocalDate.now())
                .priority(RandomUtils.nextLong(1, 100))
                .interestRatePenalty(new BigDecimal(RandomUtils.nextLong(1, 100)))
                .minimalBalance(new BigDecimal(RandomUtils.nextLong(1, 1000000)))
                .thresholdAmount(new BigDecimal(RandomUtils.nextLong(1, 1000000)))
                .accountingDetails(null)
                .rateType(RateType.PROGRESSIVE)
                .taxPercentageRate(null)
                .technicalOverdraftLimitAmount(null)
                .contractId(null)
                .branchCode(accountPoolEntity.getBranchCode())
                .isoCurrencyCode(accountPoolEntity.getCurrencyCode())
                .urgencyCode(null)
                .referenceCode(null)
                .build();
    }

    public void addInstanceAgreementDtoDefault(CreateInstanceRequestDto requestDto) {
        requestDto.getInstanceArrangements().add(CreateInstanceRequestDto.InstanceArrangement.builder()
                .generalAgreementId(RandomStringUtils.randomAlphanumeric(20))
                .supplementaryAgreementId(RandomStringUtils.randomAlphanumeric(20))
                .arrangementType(RandomStringUtils.randomAlphanumeric(10))
                .shedulerJobId(RandomUtils.nextLong(1, Long.MAX_VALUE))
                .number(RandomStringUtils.randomAlphanumeric(10))
                .openingDate(LocalDate.now())
                .closingDate(LocalDate.now().plusDays(100))
                .cancelDate(null)
                .validityDuration(RandomUtils.nextLong(1, 1000))
                .cancellationReason(RandomStringUtils.randomAlphanumeric(50))
                .status(RandomStringUtils.randomAlphanumeric(10))
                .interestCalculationDate(LocalDate.now())
                .interestRate(new BigDecimal(RandomUtils.nextLong(1, 100)))
                .coefficient(new BigDecimal(RandomUtils.nextLong(1, 100)))
                .coefficientAction(CoefficientAction.MINUS)
                .minimumInterestRate(new BigDecimal(RandomUtils.nextLong(1, 100)))
                .minimumInterestRateCoefficient(new BigDecimal(RandomUtils.nextLong(1, 100)))
                .minimumInterestRateCoefficientAction(CoefficientAction.MINUS)
                .maximalInterestRate(new BigDecimal(RandomUtils.nextLong(1, 100)))
                .maximalInterestRateCoefficient(new BigDecimal(RandomUtils.nextLong(1, 100)))
                .maximalInterestRateCoefficientAction(CoefficientAction.PLUS)
                .build());
    }

    public TppProductEntity createProductDefault() {
        return tppProductRepository.save(TppProductEntity.builder()
                .state(CommonState.OPEN)
                .build());
    }
}
