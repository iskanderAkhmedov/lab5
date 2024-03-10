package org.example.storage.model.corporatesettlement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "value", nullable = false, unique = true)
    private String value;

    @Column(name = "register_type_name", nullable = false)
    private String registerTtypeNname;

    @Column(name = "product_class_code", nullable = false)
    private String productClassCode;

    @Column(name = "register_type_start_date")
    private Timestamp registerTypeStartDate;

    @Column(name = "register_type_end_date")
    private Timestamp registerTypeEndDate;

    @Column(name = "account_type", length = 50)
    private String accountType;
}
