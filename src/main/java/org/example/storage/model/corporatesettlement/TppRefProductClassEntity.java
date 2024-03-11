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

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tpp_ref_product_class")
public class TppRefProductClassEntity {
    @Id
    @Column(name = "internal_id")
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "value_code", length = 100, nullable = false, unique = true)
    private String value;

    @Column(name = "gbi_code", length = 50)
    private String gbiCode;

    @Column(name = "gbi_name", length = 100)
    private String gbiName;

    @Column(name = "product_row_code", length = 50)
    private String productRowCode;

    @Column(name = "product_row_name", length = 100)
    private String productRowName;

    @Column(name = "subclass_code", length = 50)
    private String subclassCode;

    @Column(name = "subclass_name", length = 100)
    private String subclassName;
}
