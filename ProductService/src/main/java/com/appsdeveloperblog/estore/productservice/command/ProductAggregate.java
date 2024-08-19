package com.appsdeveloperblog.estore.productservice.command;

import com.appsdeveloperblog.estore.core.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.productservice.core.event.ProductCreatedEvent;
import lombok.Builder;
import lombok.Data;
import org.axonframework.commandhandling.CommandExecutionException;
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
    public ProductAggregate(CreateProductCommand command) throws Exception {
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

//        throw new Exception("ProductAggregate exception is thrown");
    }


    @CommandHandler
    public void handle(ReserveProductCommand command) throws Exception {
        // because Aggregate holds latest state of product for us. no need to query to get Product
        if (quantity < command.getQuantity()) {
            throw new IllegalStateException("Insufficient quantity in stock");
        }

        ProductReservedEvent event = ProductReservedEvent.builder()
                .orderId(command.getOrderId())
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .userId(command.getUserId())
                .build();
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand command) {

    }


    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        // Update Aggregate state, then update Query DB
        this.price = event.getPrice();
        this.productId = event.getProductId();
        this.quantity = event.getQuantity();
        this.title = event.getTitle();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent event) {
        this.quantity -= event.getQuantity(); // subtract reserved quantity from Aggregate state, then update Query DB
    }
}
