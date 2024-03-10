package org.example.enums;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum RateType {
    @JsonAlias({"0", "DIFFERENTIATED"})
    DIFFERENTIATED,
    @JsonAlias({"1", "PROGRESSIVE"})
    PROGRESSIVE
}
