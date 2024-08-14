package com.appsdeveloperblog.estore.productservice.command.rest;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CreateProductRestModal {
    @NotBlank(message = "required Product title")
    private String title;

    @Min(value = 1, message = "price can't be lower than 1")
    private BigDecimal price;

    @Min(value = 1, message = "quantity can't be  than 1")
    @Max(value = 5, message = "quantity can't exceed  5")
    private Integer quantity;
}
