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

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tpp_ref_product_register_type")
public class TppRefProductRegisterTypeEntity {
    @Id
    @Column(name = "internal_id")
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "value_code", nullable = false, unique = true)
    private String value;

    @Column(name = "register_type_name", length = 100, nullable = false)
    private String registerTypeName;

    @ManyToOne
    @JoinColumn(name = "product_class_code", referencedColumnName = "value_code", nullable = false)
    private TppRefProductClassEntity productClass;

    @Column(name = "register_type_start_date")
    private Timestamp registerTypeStartDate;

    @Column(name = "register_type_end_date")
    private Timestamp registerTypeEndDate;

    @ManyToOne
    @JoinColumn(name = "account_type", referencedColumnName = "value_code")
    private TppRefAccountTypeEntity accountType;
}
