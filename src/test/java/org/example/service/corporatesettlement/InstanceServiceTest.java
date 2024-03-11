package org.example.service.corporatesettlement;

import org.example.BaseTest;
import org.example.api.corporatesettlement.CreateInstanceRequestDto;
import org.example.enums.CommonState;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.EntityNotFoundException;
import org.example.storage.model.corporatesettlement.AgreementEntity;
import org.example.storage.model.corporatesettlement.TppProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;

import static org.example.utils.DateUtils.localDateToTimestamp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InstanceServiceTest extends BaseTest {

    @Autowired
    private TestData testData;

    @Test
    public void createInstanceSuccessTest() {
        var requestDto = testData.createInstanceRequestDtoForCreateInstanceDefault();

        assertEquals(0, tppProductRepository.count());
        assertEquals(0, tppProductRegisterRepository.count());
        var responseDto = instanceService.createInstance(requestDto);
        assertEquals(1, tppProductRepository.count());
        assertEquals(1, tppProductRegisterRepository.count());

        var productEntity = tppProductRepository.findAll().get(0);
        assertProduct(requestDto, productEntity);

        var registerEntity = tppProductRegisterRepository.findAll().get(0);
        assertEquals(productEntity.getId().toString(), responseDto.getData().getInstanceId());
        assertEquals(1, responseDto.getData().getRegisterId().size());
        assertEquals(registerEntity.getId(), responseDto.getData().getRegisterId().get(0));
    }

    @Test
    public void addAgreementsSuccessTest() {
        var productEntity = testData.createProductDefault();

        var requestAddAgreementsRequestDto = CreateInstanceRequestDto.builder()
                .instanceId(productEntity.getId())
                .build();

        testData.addInstanceAgreementDtoDefault(requestAddAgreementsRequestDto);
        testData.addInstanceAgreementDtoDefault(requestAddAgreementsRequestDto);
        assertEquals(2, requestAddAgreementsRequestDto.getInstanceArrangements().size());

        assertEquals(1, tppProductRepository.count());
        assertEquals(0, agreementRepository.count());
        var responseDto = instanceService.createInstance(requestAddAgreementsRequestDto);
        assertEquals(1, tppProductRepository.count());
        assertEquals(2, agreementRepository.count());

        var agreementEntitySortList = agreementRepository.findAll().stream()
                .sorted(Comparator.comparing(AgreementEntity::getSupplementaryAgreementId))
                .toList();
        var agreementDtoSortList = requestAddAgreementsRequestDto.getInstanceArrangements().stream()
                .sorted(Comparator.comparing(CreateInstanceRequestDto.InstanceArrangement::getSupplementaryAgreementId))
                .toList();

        assertEquals(2, responseDto.getData().getSupplementaryAgreementId().size());

        for(int i = 0; i < agreementDtoSortList.size(); i++) {
            assertEquals(productEntity.getId(), agreementEntitySortList.get(i).getProduct().getId());
            assertAgreement(agreementDtoSortList.get(i), agreementEntitySortList.get(i));
            assertTrue(responseDto.getData().getSupplementaryAgreementId()
                    .contains(agreementEntitySortList.get(i).getId()));
        }

        assertEquals(productEntity.getId().toString(), responseDto.getData().getInstanceId());
    }

    @Test
    public void createInstanceWithAgreementSuccessTest() {
        var requestDto = testData.createInstanceRequestDtoForCreateInstanceDefault();
        testData.addInstanceAgreementDtoDefault(requestDto);
        testData.addInstanceAgreementDtoDefault(requestDto);

        assertEquals(0, tppProductRepository.count());
        assertEquals(0, tppProductRegisterRepository.count());
        assertEquals(0, agreementRepository.count());
        var responseDto = instanceService.createInstance(requestDto);
        assertEquals(1, tppProductRepository.count());
        assertEquals(1, tppProductRegisterRepository.count());
        assertEquals(2, agreementRepository.count());

        var productEntity = tppProductRepository.findAll().get(0);
        assertProduct(requestDto, productEntity);

        var registerEntity = tppProductRegisterRepository.findAll().get(0);
        assertEquals(productEntity.getId().toString(), responseDto.getData().getInstanceId());
        assertEquals(1, responseDto.getData().getRegisterId().size());
        assertEquals(registerEntity.getId(), responseDto.getData().getRegisterId().get(0));

        var agreementEntitySortList = agreementRepository.findAll().stream()
                .sorted(Comparator.comparing(AgreementEntity::getSupplementaryAgreementId))
                .toList();
        var agreementDtoSortList = requestDto.getInstanceArrangements().stream()
                .sorted(Comparator.comparing(CreateInstanceRequestDto.InstanceArrangement::getSupplementaryAgreementId))
                .toList();

        assertEquals(2, responseDto.getData().getSupplementaryAgreementId().size());

        for(int i = 0; i < agreementDtoSortList.size(); i++) {
            assertEquals(productEntity.getId(), agreementEntitySortList.get(i).getProduct().getId());
            assertAgreement(agreementDtoSortList.get(i), agreementEntitySortList.get(i));
            assertTrue(responseDto.getData().getSupplementaryAgreementId()
                    .contains(agreementEntitySortList.get(i).getId()));
        }
    }

    @Test
    public void addAgreementNoInstanceErrorTest() {
        var requestAddAgreementsRequestDto = CreateInstanceRequestDto.builder()
                .instanceId(999L)
                .build();
        testData.addInstanceAgreementDtoDefault(requestAddAgreementsRequestDto);

        assertEquals(0, tppProductRepository.count());
        assertEquals(0, agreementRepository.count());
        assertThrows(EntityNotFoundException.class, () -> instanceService.createInstance(requestAddAgreementsRequestDto));
        assertEquals(0, tppProductRepository.count());
        assertEquals(0, agreementRepository.count());
    }

    @Test
    public void createInstanceDuplicateContractNumberErrorTest() {
        var requestDto = testData.createInstanceRequestDtoForCreateInstanceDefault();

        assertEquals(0, tppProductRepository.count());
        assertEquals(0, tppProductRegisterRepository.count());
        instanceService.createInstance(requestDto);
        assertEquals(1, tppProductRepository.count());
        assertEquals(1, tppProductRegisterRepository.count());

        assertThrows(EntityAlreadyExistsException.class, () -> instanceService.createInstance(requestDto));
        assertEquals(1, tppProductRepository.count());
        assertEquals(1, tppProductRegisterRepository.count());
    }

    @Test
    public void addAgreementDuplicateAgreementErrorTest() {
        var productEntity = testData.createProductDefault();

        var requestAddAgreementsRequestDto = CreateInstanceRequestDto.builder()
                .instanceId(productEntity.getId())
                .build();

        testData.addInstanceAgreementDtoDefault(requestAddAgreementsRequestDto);
        assertEquals(1, requestAddAgreementsRequestDto.getInstanceArrangements().size());

        assertEquals(1, tppProductRepository.count());
        assertEquals(0, agreementRepository.count());
        instanceService.createInstance(requestAddAgreementsRequestDto);
        assertEquals(1, tppProductRepository.count());
        assertEquals(1, agreementRepository.count());

        assertThrows(EntityAlreadyExistsException.class, () -> instanceService.createInstance(requestAddAgreementsRequestDto));
        assertEquals(1, tppProductRepository.count());
        assertEquals(1, agreementRepository.count());
    }

    @Test
    public void createInstanceNotFoundProductodeErrorTest() {
        var requestDto = testData.createInstanceRequestDtoForCreateInstanceDefault();
        requestDto.setProductCode("invalidCode");

        assertEquals(0, tppProductRepository.count());
        assertEquals(0, tppProductRegisterRepository.count());
        assertThrows(EntityNotFoundException.class, () -> instanceService.createInstance(requestDto));
        assertEquals(0, tppProductRepository.count());
        assertEquals(0, tppProductRegisterRepository.count());
    }

    private void assertProduct(CreateInstanceRequestDto requestDto, TppProductEntity productEntity) {
        var productClassEntityOptional = tppRefProductClassRepository.findById(productEntity.getProductCodeId());
        assertTrue(productClassEntityOptional.isPresent());
        var productClassEntity = productClassEntityOptional.get();

        assertEquals(requestDto.getProductCode(), productClassEntity.getValue());
        assertEquals(requestDto.getMdmCode(), productEntity.getClientId().toString());
        assertEquals(requestDto.getProductType(), productEntity.getType());
        assertEquals(requestDto.getContractNumber(), productEntity.getNumber());
        assertEquals(requestDto.getPriority(), productEntity.getPriority());
        assertEquals(localDateToTimestamp(requestDto.getContractDate()), productEntity.getDateOfConclusion());
        assertEquals(requestDto.getInterestRatePenalty(), productEntity.getPenaltyRate());
        assertEquals(requestDto.getMinimalBalance(), productEntity.getNso());
        assertEquals(requestDto.getThresholdAmount(), productEntity.getThresholdAmount());
        assertEquals(requestDto.getRateType(), productEntity.getInterestRateType());
        assertEquals(requestDto.getTaxPercentageRate(), productEntity.getTaxRate());
        assertEquals(CommonState.OPEN, productEntity.getState());
    }

    private void assertAgreement(CreateInstanceRequestDto.InstanceArrangement arrangementReq, AgreementEntity agreement) {
        assertEquals(arrangementReq.getGeneralAgreementId(), agreement.getGeneralAgreementId());
        assertEquals(arrangementReq.getSupplementaryAgreementId(), agreement.getSupplementaryAgreementId());
        assertEquals(arrangementReq.getArrangementType(), agreement.getArrangementType());
        assertEquals(arrangementReq.getShedulerJobId(), agreement.getShedulerJobId());
        assertEquals(arrangementReq.getNumber(), agreement.getNumber());
        assertEquals(localDateToTimestamp(arrangementReq.getOpeningDate()), agreement.getOpeningDate());
        assertEquals(localDateToTimestamp(arrangementReq.getClosingDate()), agreement.getClosingDate());
        assertEquals(localDateToTimestamp(arrangementReq.getCancelDate()), agreement.getCancelDate());
        assertEquals(arrangementReq.getValidityDuration(), agreement.getValidityDuration());
        assertEquals(arrangementReq.getCancellationReason(), agreement.getCancellationReason());
        assertEquals(arrangementReq.getStatus(), agreement.getStatus());
        assertEquals(localDateToTimestamp(arrangementReq.getInterestCalculationDate()), agreement.getInterestCalculationDate());
        assertEquals(arrangementReq.getInterestRate(), agreement.getInterestRate());
        assertEquals(arrangementReq.getCoefficient(), agreement.getCoefficient());
        assertEquals(arrangementReq.getCoefficientAction(), agreement.getCoefficientAction());
        assertEquals(arrangementReq.getMinimumInterestRate(), agreement.getMinimumInterestRate());
        assertEquals(arrangementReq.getMinimumInterestRateCoefficient(), agreement.getMinimumInterestRateCoefficient());
        assertEquals(arrangementReq.getMinimumInterestRateCoefficientAction(), agreement.getMinimumInterestRateCoefficientAction());
        assertEquals(arrangementReq.getMaximalInterestRate(), agreement.getMaximalInterestRate());
        assertEquals(arrangementReq.getMaximalInterestRateCoefficient(), agreement.getMaximalInterestRateCoefficient());
        assertEquals(arrangementReq.getMaximalInterestRateCoefficientAction(), agreement.getMaximalInterestRateCoefficientAction());
    }
}
