package com.appsdeveloperblog.estore.productservice.command;

import com.appsdeveloperblog.estore.productservice.core.event.ProductCreatedEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
@Data
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        // validate command => moved to Message Interceptor
//        if (command.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalArgumentException("price can not be less than zero");
//        }
//
//        if (command.getTitle() == null || command.getTitle().isBlank()) {
//            throw new IllegalArgumentException("Title can not be empty");
//        }

        ProductCreatedEvent event = new ProductCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        this.price = event.getPrice();
        this.productId = event.getProductId();
        this.quantity = event.getQuantity();
        this.title = event.getTitle();
    }
}
