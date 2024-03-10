package org.example.api.corporatesettlement;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAccountResponseDto {
    private AccountData data;

    @Builder
    @Getter
    public static class AccountData {
        private String accountId;
    }
}
