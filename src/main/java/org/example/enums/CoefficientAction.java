package org.example.enums;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum CoefficientAction {
    @JsonAlias({"+", "PLUS"})
    PLUS,
    @JsonAlias({"-", "MINUS"})
    MINUS
}
