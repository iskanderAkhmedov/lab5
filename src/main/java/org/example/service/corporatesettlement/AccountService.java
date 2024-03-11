package org.example.service.corporatesettlement;

import lombok.RequiredArgsConstructor;
import org.example.api.corporatesettlement.CreateAccountRequestDto;
import org.example.api.corporatesettlement.CreateAccountResponseDto;
import org.example.enums.CommonState;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.EntityNotFoundException;
import org.example.storage.model.corporatesettlement.TppProductRegisterEntity;
import org.example.storage.model.corporatesettlement.TppRefProductRegisterTypeEntity;
import org.example.storage.repository.corporatesettlement.AccountPoolRepository;
import org.example.storage.repository.corporatesettlement.AccountRepository;
import org.example.storage.repository.corporatesettlement.TppProductRegisterRepository;
import org.example.storage.repository.corporatesettlement.TppRefProductRegisterTypeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.utils.AnnotationUtils.getTableNameByEntityClass;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final TppProductRegisterRepository tppProductRegisterRepository;
    private final TppRefProductRegisterTypeRepository tppRefProductRegisterTypeRepository;
    private final AccountPoolRepository accountPoolRepository;
    private final AccountRepository accountRepository;

    @Value("${db-schema}")
    private String dbSchema;

    public CreateAccountResponseDto createAccount(CreateAccountRequestDto req) {

        var registerTypeEntity = getRegisterTypeEntity(req.getRegistryTypeCode());

        checkExistsRegister(req.getInstanceId(), registerTypeEntity);

        var tppProductRegisterEntity = createAccount(req.getInstanceId(),
                registerTypeEntity,
                req.getBranchCode(),
                req.getCurrencyCode(),
                req.getMdmCode(),
                req.getPriorityCode());

        return createResponseDto(tppProductRegisterEntity.getId());
    }

    public TppProductRegisterEntity createAccount(
            Long productId,
            TppRefProductRegisterTypeEntity type,
            String branchCode,
            String currencyCode,
            String mdmCode,
            String priorityCode
    ) {

        var accountPoolEntity = accountPoolRepository.findByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegistryTypeCode(
                        branchCode, currencyCode, mdmCode, priorityCode, type.getValue())
                .orElseThrow(() -> (new EntityNotFoundException("Не найден подходящий пул счетов")));

        var accountEntity = accountRepository.findFirstByAccountPoolAndBussy(accountPoolEntity, false)
                .orElseThrow(() -> (new EntityNotFoundException("Не найден свободный счет в пуле")));

        accountEntity.setBussy(true);
        accountEntity = accountRepository.save(accountEntity);

        return tppProductRegisterRepository.save(TppProductRegisterEntity.builder()
                .productId(productId)
                .type(type)
                .account(accountEntity.getId())
                .accountNumber(accountEntity.getAccountNumber())
                .currencyCode(currencyCode)
                .state(CommonState.OPEN)
                .build());
    }

    private CreateAccountResponseDto createResponseDto(Long productRegisterId) {
        return CreateAccountResponseDto.builder()
                .data(CreateAccountResponseDto.AccountData.builder()
                        .accountId(productRegisterId.toString())
                        .build())
                .build();
    }

    private TppRefProductRegisterTypeEntity getRegisterTypeEntity(String registerTypeCode) {
        return tppRefProductRegisterTypeRepository.findByValue(registerTypeCode)
                .orElseThrow(() -> (new EntityNotFoundException("Код Продукта " + registerTypeCode
                        + " не найдено в Каталоге продуктов " + dbSchema + "." + getTableNameByEntityClass(TppRefProductRegisterTypeEntity.class)
                        + " для данного типа Регистра")));
    }

    private void checkExistsRegister(Long instanceId, TppRefProductRegisterTypeEntity registerTypeEntity) {
        var existingRegistry = tppProductRegisterRepository.findByProductIdAndType(instanceId, registerTypeEntity);

        if (existingRegistry.isPresent()) {
            throw new EntityAlreadyExistsException("Параметр registryTypeCode тип регистра "
                    + registerTypeEntity.getValue() + " уже существует для ЭП с ИД " + instanceId + ".");
        }
    }
}
