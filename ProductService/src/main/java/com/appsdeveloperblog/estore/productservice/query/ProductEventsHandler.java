package com.appsdeveloperblog.estore.productservice.query;

import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.productservice.core.data.ProductEntity;
import com.appsdeveloperblog.estore.productservice.core.data.ProductsRepository;
import com.appsdeveloperblog.estore.productservice.core.event.ProductCreatedEvent;
import com.appsdeveloperblog.estore.productservice.core.event.ProductDeletedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group") // to group events that belongs or related to products
@AllArgsConstructor
public class ProductEventsHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventsHandler.class);

    private final ProductsRepository productsRepository;

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        // Log error message
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalStateException.class)
    public void handle(IllegalStateException exception) {
        // Log error message
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);

        productsRepository.save(productEntity);
//        throw new IllegalStateException("this method will be roll-backed by Exception Handler of this class");
    }

    @EventHandler
    public void on(ProductReservedEvent event) {
        ProductEntity productEntity = productsRepository.findByProductId(event.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() - event.getQuantity()); // substract reserved quantity
        productsRepository.save(productEntity);

        LOGGER.info("ProductReservedEvent is called for productId: {} and orderId: {}", event.getProductId(), event.getOrderId());
    }

}
