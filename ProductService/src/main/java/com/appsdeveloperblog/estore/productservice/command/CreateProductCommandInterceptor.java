package com.appsdeveloperblog.estore.productservice.command;

import com.appsdeveloperblog.estore.productservice.core.data.ProductLookupEntity;
import com.appsdeveloperblog.estore.productservice.core.data.ProductLookupRepository;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@AllArgsConstructor
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

    private final ProductLookupRepository productLookupRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>>
    handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            LOGGER.info("Interceptor cmd: " + command.getPayloadType());
            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand cmd = (CreateProductCommand) command.getPayload();
                ProductLookupEntity productLookupEntity = productLookupRepository.findByProductIdOrTitle(cmd.getProductId(), cmd.getTitle());
                if (productLookupEntity != null) {
                    throw new IllegalStateException("Product with product Id or product Title already existed: "
                            + productLookupEntity.getProductId() + " - " + productLookupEntity.getTitle());
                }
            }
            return command;
        };
    }

    @Nonnull
    @Override
    public CommandMessage<?> handle(@Nonnull CommandMessage<?> message) {
        return MessageDispatchInterceptor.super.handle(message);
    }
}
