package com.appsdeveloperblog.estore.productservice.command;

import com.appsdeveloperblog.estore.productservice.core.data.ProductLookupEntity;
import com.appsdeveloperblog.estore.productservice.core.data.ProductLookupRepository;
import com.appsdeveloperblog.estore.productservice.core.event.ProductCreatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group") // to group events that belongs or related to products
@AllArgsConstructor
public class ProductLookupEventsHandler {
    private final ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        productLookupRepository.save(productLookupEntity);
    }
}
