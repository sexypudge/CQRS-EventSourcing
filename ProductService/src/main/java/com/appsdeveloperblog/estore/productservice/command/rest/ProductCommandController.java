package com.appsdeveloperblog.estore.productservice.command.rest;

import com.appsdeveloperblog.estore.productservice.command.CreateProductCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Getter
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductCommandController {

    private final Environment env;
    private final CommandGateway commandGateway;

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRestModal model) {
        CreateProductCommand command  = CreateProductCommand.builder()
                .price(model.getPrice())
                .title(model.getTitle())
                .quantity(model.getQuantity())
                .productId(UUID.randomUUID().toString())
                .build();

        String returnValue;
        try {
            returnValue = commandGateway.sendAndWait(command);
        } catch (Exception e) {
            returnValue = e.getLocalizedMessage();
        }

        return returnValue;
    }

//    @GetMapping
//    public String getProduct() {
//        return "HTTP GET HANDLED " + env.getProperty("local.server.port"); // not "server.port" because "server.port = 0" in application.properties
//    }
//
//    @PutMapping
//    public String updateProduct() {
//        return "HTTP UPDATE HANDLED ";
//    }
//
//    @DeleteMapping
//    public String deleteProduct() {
//        return "HTTP DELETE HANDLED ";
//    }
}
