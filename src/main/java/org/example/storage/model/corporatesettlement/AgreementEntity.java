package org.example.storage.model.corporatesettlement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.CoefficientAction;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "agreement")
public class AgreementEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private TppProductEntity product;

    @Column(name = "general_agreement_id", length = 50)
    private String generalAgreementId;

    @Column(name = "supplementary_agreement_id", length = 50)
    private String supplementaryAgreementId;

    @Column(name = "arrangement_type", length = 50)
    private String arrangementType;

    @Column(name = "sheduler_job_id")
    private Long shedulerJobId;

    @Column(name = "number", length = 50)
    private String number;

    @Column(name = "opening_date")
    private Timestamp openingDate;

    @Column(name = "closingDate")
    private Timestamp closingDate;

    @Column(name = "cancel_date")
    private Timestamp cancelDate;

    @Column(name = "validity_duration")
    private Long validityDuration;

    @Column(name = "cancellation_reason", length = 100)
    private String cancellationReason;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "interest_calculation_date")
    private Timestamp interestCalculationDate;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "coefficient")
    private BigDecimal coefficient;

    @Column(name = "coefficient_action", length = 50)
    private CoefficientAction coefficientAction;

    @Column(name = "minimum_interest_rate")
    private BigDecimal minimumInterestRate;

    @Column(name = "minimum_interest_rate_coefficient")
    private BigDecimal minimumInterestRateCoefficient;

    @Column(name = "minimum_interest_rate_coefficient_action", length = 50)
    private CoefficientAction minimumInterestRateCoefficientAction;

    @Column(name = "maximal_interest_rate")
    private BigDecimal maximalInterestRate;

    @Column(name = "maximal_interest_rate_coefficient")
    private BigDecimal maximalInterestRateCoefficient;

    @Column(name = "maximal_interest_rate_coefficient_action", length = 50)
    private CoefficientAction maximalInterestRateCoefficientAction;
}
