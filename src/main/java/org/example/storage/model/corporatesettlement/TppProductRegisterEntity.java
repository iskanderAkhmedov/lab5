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
import org.example.enums.AccountState;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tpp_product_register")
public class TppProductRegisterEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "value", nullable = false)
    private TppRefProductRegisterTypeEntity type;

    @Column(name = "account")
    private Long account;

    @Column(name = "currency_code", length = 30)
    private String currencyCode;

    @Column(name = "state", length = 50)
    private AccountState state;

    @Column(name = "account_number", length = 25)
    private String accountNumber;
}
