package org.example.service.corporatesettlement;

import org.apache.commons.lang3.RandomUtils;
import org.example.BaseTest;
import org.example.api.corporatesettlement.CreateAccountRequestDto;
import org.example.enums.CommonState;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.EntityNotFoundException;
import org.example.storage.model.corporatesettlement.TppProductRegisterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountServiceTest extends BaseTest {

    @Autowired
    private TestData testData;

    @Test
    public void createAccountSuccessTest() {
        var requestDto = testData.createAccountRequestDtoDefault(RandomUtils.nextLong(1, Long.MAX_VALUE));

        assertEquals(0, tppProductRegisterRepository.count());
        var responseDto = accountService.createAccount(requestDto);
        assertEquals(1, tppProductRegisterRepository.count());

        var productRegisterList = tppProductRegisterRepository.findAll();
        assertEquals(1, productRegisterList.size());

        var productRegisterEntity = productRegisterList.get(0);
        assertProductRegister(requestDto, productRegisterEntity);

        assertEquals(productRegisterEntity.getId().toString(), responseDto.getData().getAccountId());
    }

    @Test
    public void createAccountDuplicateRegisterTest() {
        var requestDto = testData.createAccountRequestDtoDefault(RandomUtils.nextLong(1, Long.MAX_VALUE));

        assertEquals(0, tppProductRegisterRepository.count());
        accountService.createAccount(requestDto);
        assertEquals(1, tppProductRegisterRepository.count());
        assertThrows(EntityAlreadyExistsException.class, () -> accountService.createAccount(requestDto));
        assertEquals(1, tppProductRegisterRepository.count());
    }

    @Test
    public void createAccountInvalidRegisterTypeTest() {
        var requestDto = testData.createAccountRequestDtoDefault(RandomUtils.nextLong(1, Long.MAX_VALUE));
        requestDto.setRegistryTypeCode("invalidCode");

        assertEquals(0, tppProductRegisterRepository.count());
        assertThrows(EntityNotFoundException.class, () -> accountService.createAccount(requestDto));
        assertEquals(0, tppProductRegisterRepository.count());
    }

    @Test
    public void createAccountNotFoundAccountPoolTest() {
        var requestDto = testData.createAccountRequestDtoDefault(RandomUtils.nextLong(1, Long.MAX_VALUE));
        requestDto.setBranchCode("InvalidCode");

        assertEquals(0, tppProductRegisterRepository.count());
        assertThrows(EntityNotFoundException.class, () -> accountService.createAccount(requestDto));
        assertEquals(0, tppProductRegisterRepository.count());
    }

    @Test
    public void createAccountNotFoundAccountTest() {
        var requestDto = testData.createAccountRequestDtoDefault(RandomUtils.nextLong(1, Long.MAX_VALUE));

        var accountPoolEntityOptional = accountPoolRepository.findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(
                requestDto.getBranchCode(),
                requestDto.getCurrencyCode(),
                requestDto.getMdmCode(),
                requestDto.getPriorityCode(),
                requestDto.getRegistryTypeCode());

        assertTrue(accountPoolEntityOptional.isPresent());
        var accountPoolEntity = accountPoolEntityOptional.get();
        var accountEntityList = accountRepository.findAllByAccountPool(accountPoolEntity);

        accountRepository.saveAll(accountEntityList.stream().peek(a -> a.setBussy(true)).toList());

        var accountEntityOptional = accountRepository.findFirstByAccountPoolAndBussy(accountPoolEntity, false);
        assertTrue(accountEntityOptional.isEmpty());

        assertEquals(0, tppProductRegisterRepository.count());
        assertThrows(EntityNotFoundException.class, () -> accountService.createAccount(requestDto));
        assertEquals(0, tppProductRegisterRepository.count());
    }

    private void assertProductRegister(CreateAccountRequestDto requestDto, TppProductRegisterEntity productRegister) {
        assertEquals(requestDto.getInstanceId(), productRegister.getProductId());
        assertEquals(requestDto.getRegistryTypeCode(), productRegister.getType().getValue());
        assertEquals(requestDto.getCurrencyCode(), productRegister.getCurrencyCode());
        assertEquals(CommonState.OPEN, productRegister.getState());

        assertAccount(requestDto, productRegister.getAccount(), productRegister.getAccountNumber());
    }

    private void assertAccount(CreateAccountRequestDto requestDto, Long accountId, String accountNumber) {
        var accountEntityOptional = accountRepository.findById(accountId);
        assertTrue(accountEntityOptional.isPresent());

        var accountEntity = accountEntityOptional.get();
        assertEquals(accountEntity.getAccountNumber(), accountNumber);
        assertTrue(accountEntity.getBussy());

        var accountPoolEntity = accountEntity.getAccountPool();
        assertEquals(requestDto.getCurrencyCode(), accountPoolEntity.getCurrencyCode());
        assertEquals(requestDto.getBranchCode(), accountPoolEntity.getBranchCode());
        assertEquals(requestDto.getPriorityCode(), accountPoolEntity.getPriorityCode());
        assertEquals(requestDto.getMdmCode(), accountPoolEntity.getMdmCode());
        assertEquals(requestDto.getRegistryTypeCode(), accountPoolEntity.getRegistryTypeCode());
    }
}
