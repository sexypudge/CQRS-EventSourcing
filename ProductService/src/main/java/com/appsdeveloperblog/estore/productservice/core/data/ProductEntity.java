package com.appsdeveloperblog.estore.productservice.core.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
@Data
@Entity
@Table(name="products")
public class ProductEntity implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(unique = true)
    private String productId;

    private String title;

    private BigDecimal price;

    private Integer quantity;

}
