package org.example.api.corporatesettlement;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CreateInstanceResponseDto {

    private CreateInstanceResponseDto.InstanceData data;

    @Builder
    @Getter
    public static class InstanceData {
        private String instanceId;

        @Builder.Default
        private List<Long> registerId = new ArrayList<>();

        @Builder.Default
        private List<Long> supplementaryAgreementId = new ArrayList<>();
    }
}
