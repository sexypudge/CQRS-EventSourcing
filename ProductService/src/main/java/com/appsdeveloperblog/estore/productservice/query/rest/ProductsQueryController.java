package com.appsdeveloperblog.estore.productservice.query.rest;

import com.appsdeveloperblog.estore.productservice.query.FindProductsQuery;
import lombok.AllArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductsQueryController {

    private final QueryGateway queryGateway;

    @GetMapping
    public List<ProductResponseModel> getProduct() {
        FindProductsQuery query = new FindProductsQuery();
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(ProductResponseModel.class)).join();
    }
}
