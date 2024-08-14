package com.appsdeveloperblog.estore.productservice.query;

import com.appsdeveloperblog.estore.productservice.core.data.ProductsRepository;
import com.appsdeveloperblog.estore.productservice.query.rest.ProductResponseModel;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductsQueryHandler {
    private final ProductsRepository productsRepository;

    @QueryHandler
    public List<ProductResponseModel> findProducts(FindProductsQuery query) {
        return productsRepository.findAll()
                .stream()
                .map( t -> {
                    ProductResponseModel model = new ProductResponseModel();
                    BeanUtils.copyProperties(t, model);
                    return model;
                })
                .collect(Collectors.toList());
    }
}
