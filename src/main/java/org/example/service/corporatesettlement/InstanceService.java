package org.example.service.corporatesettlement;

import lombok.RequiredArgsConstructor;
import org.example.api.corporatesettlement.CreateInstanceRequestDto;
import org.example.api.corporatesettlement.CreateInstanceResponseDto;
import org.example.enums.CommonState;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.EntityNotFoundException;
import org.example.storage.model.corporatesettlement.AgreementEntity;
import org.example.storage.model.corporatesettlement.TppProductEntity;
import org.example.storage.model.corporatesettlement.TppProductRegisterEntity;
import org.example.storage.model.corporatesettlement.TppRefAccountTypeEntity;
import org.example.storage.model.corporatesettlement.TppRefProductClassEntity;
import org.example.storage.model.corporatesettlement.TppRefProductRegisterTypeEntity;
import org.example.storage.repository.corporatesettlement.AgreementRepository;
import org.example.storage.repository.corporatesettlement.TppProductRepository;
import org.example.storage.repository.corporatesettlement.TppRefAccountTypeRepository;
import org.example.storage.repository.corporatesettlement.TppRefProductClassRepository;
import org.example.storage.repository.corporatesettlement.TppRefProductRegisterTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InstanceService {

    private static final String ACCOUNT_CODE_CLIENT = "Клиентский";
    private static final String PRIORITY_CODE_00 = "00";

    private final TppProductRepository tppProductRepository;
    private final AgreementRepository agreementRepository;
    private final TppRefProductClassRepository tppRefProductClassRepository;
    private final TppRefAccountTypeRepository tppRefAccountTypeRepository;
    private final TppRefProductRegisterTypeRepository tppRefProductRegisterTypeRepository;
    private final AccountService accountService;

    public CreateInstanceResponseDto createInstance(CreateInstanceRequestDto req) {

        if (req.getInstanceId() == null) {
            return createNewInstance(req);
        } else {
            return addAgreements(req);
        }
    }

    private CreateInstanceResponseDto createNewInstance(CreateInstanceRequestDto req) {
        validateCreateReq(req);

        var productClassEntity = getProductClassEntity(req.getProductCode());

        var accountClassEntity = getAccountTypeEntity();

        var productRegisterTypeList = getProductRegisterTypeEntityList(productClassEntity, accountClassEntity);

        var tppProductEntity = createProduct(productClassEntity, req);

        var productRegisterList = createProductRegisters(productRegisterTypeList,
                tppProductEntity, req.getBranchCode(), req.getIsoCurrencyCode(), req.getMdmCode());

        var agreementEntityList = createAgreements(tppProductEntity, req.getInstanceArrangements());

        return createResponseDto(tppProductEntity, productRegisterList, agreementEntityList);
    }

    private CreateInstanceResponseDto addAgreements(CreateInstanceRequestDto req) {

        var productEntity = getProductEntity(req.getInstanceId());

        checkAgreementDuplicate(req);
        
        var agreementEntityList = createAgreements(productEntity, req.getInstanceArrangements());

        return createResponseDto(productEntity, null, agreementEntityList);
    }

    private List<AgreementEntity> createAgreements(
            TppProductEntity productEntity, List<CreateInstanceRequestDto.InstanceArrangement> agreementList) {
        return agreementList.stream().map(a -> createAgreement(productEntity, a)).toList();
    }

    private AgreementEntity createAgreement(TppProductEntity productEntity, CreateInstanceRequestDto.InstanceArrangement agreement) {
        return agreementRepository.save(AgreementEntity.builder()
                .product(productEntity)
                        .generalAgreementId(agreement.getGeneralAgreementId())
                        .supplementaryAgreementId(agreement.getSupplementaryAgreementId())
                        .arrangementType(agreement.getArrangementType())
                        .shedulerJobIid(agreement.getShedulerJobId())
                        .number(agreement.getNumber())
                        .opening_date(Timestamp.valueOf(agreement.getOpeningDate().atStartOfDay()))
                        .closingDate(Timestamp.valueOf(agreement.getClosingDate().atStartOfDay()))
                        .cancelDate(Timestamp.valueOf(agreement.getCancelDate().atStartOfDay()))
                        .validityDuration(agreement.getValidityDuration())
                        .cancellationReason(agreement.getCancellationReason())
                        .status(agreement.getStatus())
                        .interestCalculationDate(Timestamp.valueOf(agreement.getInterestCalculationDate().atStartOfDay()))
                        .interestRate(agreement.getInterestRate())
                        .coefficient(agreement.getCoefficient())
                        .coefficientAction(agreement.getCoefficientAction())
                        .minimumInterestRate(agreement.getMinimumInterestRate())
                        .minimumInterestRateCoefficient(agreement.getMaximalInterestRateCoefficient())
                        .minimumInterestRateCoefficientAction(agreement.getMinimumInterestRateCoefficientAction())
                        .maximalInterestRate(agreement.getMaximalInterestRate())
                        .maximalInterestRateCoefficient(agreement.getMaximalInterestRateCoefficient())
                        .maximalInterestRateCoefficientAction(agreement.getMaximalInterestRateCoefficientAction())
                .build());
    }

    private TppProductEntity getProductEntity(Long instanceId) {
        return tppProductRepository.findById(instanceId)
                .orElseThrow(() -> (new EntityNotFoundException("Экземпляр продукта с параметром instanceId "
                        + instanceId + " не найден")));
    }



    private void validateCreateReq(CreateInstanceRequestDto req) {
        var productList = tppProductRepository.findAllByNumber(req.getContractNumber());
        if (productList.size() != 0) {
            throw new EntityAlreadyExistsException("Параметр ContractNumber № договора "
                    + req.getContractNumber() + " уже существует для ЭП с ИД " + productList.get(0).getId());
        }

        checkAgreementDuplicate(req);
    }

    private void checkAgreementDuplicate(CreateInstanceRequestDto req) {
        var reqAgreementNumberList = req.getInstanceArrangements().stream()
                .map(CreateInstanceRequestDto.InstanceArrangement::getNumber).toList();
        var agreementEntityList = agreementRepository.findAllByNumberIn(reqAgreementNumberList);
        if (agreementEntityList.size() != 0) {
            throw new EntityAlreadyExistsException("Параметр № Дополнительного соглашения (сделки) Number "
                    + agreementEntityList.get(0).getNumber() + " уже существует для ЭП с ИД "
                    + agreementEntityList.get(0).getProduct().getId());
        }
    }

    private TppRefProductClassEntity getProductClassEntity(String productCode) {
        return tppRefProductClassRepository.findByValue(productCode)
                .orElseThrow(() -> (new EntityNotFoundException("Не найден тип продукта по коду продукта "
                        + productCode)));
    }

    private TppRefAccountTypeEntity getAccountTypeEntity() {
        return tppRefAccountTypeRepository.findByValue(ACCOUNT_CODE_CLIENT)
                .orElseThrow(() -> (new EntityNotFoundException("Не найден тип счета с кодом " + ACCOUNT_CODE_CLIENT)));
    }

    private List<TppRefProductRegisterTypeEntity> getProductRegisterTypeEntityList(
            TppRefProductClassEntity productClassEntity, TppRefAccountTypeEntity accountClassEntity) {
        var productRegisterTypeList = tppRefProductRegisterTypeRepository
                .findAllByProductClassAndAccountType(productClassEntity, accountClassEntity);

        if (productRegisterTypeList.size() == 0) {
            throw new EntityNotFoundException("КодПродукта " + productClassEntity.getValue()
                    + " не найдено в Каталоге продуктов tpp_ref_product_class");
        }

        return productRegisterTypeList;
    }

    private TppProductEntity createProduct(TppRefProductClassEntity productClassEntity, CreateInstanceRequestDto req) {
        return tppProductRepository.save(TppProductEntity.builder()
                .productCodeId(productClassEntity.getId())
                .clientId(Long.parseLong(req.getMdmCode()))
                .type(req.getProductType())
                .number(req.getContractNumber())
                .priority(req.getPriority())
                .dateOfConclusion(Timestamp.valueOf(req.getContractDate().atStartOfDay()))
                .penaltyRate(req.getInterestRatePenalty())
                .nso(req.getMinimalBalance())
                .thresholdAmount(req.getThresholdAmount())
                .interestRateType(req.getRateType())
                .taxRate(req.getTaxPercentageRate())
                .state(CommonState.OPEN)
                .build());
    }

    private List<TppProductRegisterEntity> createProductRegisters(
            List<TppRefProductRegisterTypeEntity> productRegisterTypeList,
            TppProductEntity tppProductEntity,
            String branchCode,
            String currencyCode,
            String mdmCode
            ) {
        return productRegisterTypeList.stream().map(r -> (
                        accountService.createAccount(tppProductEntity.getId(),
                                r,
                                branchCode,
                                currencyCode,
                                mdmCode,
                                PRIORITY_CODE_00)))
                .toList();
    }

    private CreateInstanceResponseDto createResponseDto(
            TppProductEntity tppProductEntity,
            List<TppProductRegisterEntity> productRegisterList,
            List<AgreementEntity> agreementEntityList) {

        var instanceDataBuilder = CreateInstanceResponseDto.InstanceData.builder()
                .instanceId(tppProductEntity.getId().toString());

        if (productRegisterList != null) {
            instanceDataBuilder.registerId(productRegisterList.stream()
                    .map(TppProductRegisterEntity::getId).toList());
        }
        if (agreementEntityList != null) {
            instanceDataBuilder.supplementaryAgreementId(agreementEntityList.stream()
                    .map(AgreementEntity::getId).toList());
        }

        return CreateInstanceResponseDto.builder().data(instanceDataBuilder.build()).build();
    }

}
