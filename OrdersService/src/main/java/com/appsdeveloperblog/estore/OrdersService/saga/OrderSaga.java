package com.appsdeveloperblog.estore.OrdersService.saga;

import com.appsdeveloperblog.estore.OrdersService.core.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.core.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.estore.core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.core.events.ProductReservedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@Saga
//@AllArgsConstructor
public class OrderSaga {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSaga.class);

    @Autowired
    private transient CommandGateway commandGateway;
//
//    @Autowired
//    private transient QueryGateway queryGateway;
//
//    @Autowired
//    private transient DeadlineManager deadlineManager;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .build();
        LOGGER.info("OrderCreatedEvent handled for orderId: {} - and productId: {}", reserveProductCommand.getOrderId(), reserveProductCommand.getProductId());

        commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // Start a compensating transaction
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent event) {
        // Process user payment
        LOGGER.info("ProductReservedEvent handled for orderId: {} - and productId: {}", event.getOrderId(), event.getProductId());
//        commandGateway.send(command, (commandMessage, commandResultMessage) -> {
//
//        });

        // Start a compensating transaction
        cancelProductReservation(event, "can't proceed reservation with payment detail");
    }

    private void cancelProductReservation(ProductReservedEvent event, String reason) {
        CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .reason(reason)
                .build();
    }

}
