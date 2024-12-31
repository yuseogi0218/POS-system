package com.yuseogi.tradeservice.dto.request;

import java.util.List;

import static com.yuseogi.tradeservice.dto.request.CreateOrderRequestDto.*;

public class CreateOrderRequestDtoBuilder {

    public static CreateOrderRequestDto build() {
        Product product = new Product(1L, 10);

        return new CreateOrderRequestDto(List.of(product));
    }

    public static CreateOrderRequestDto nullProductIdBuild() {
        Product product = new Product(null, 10);

        return new CreateOrderRequestDto(List.of(product));
    }

    public static CreateOrderRequestDto nullProductCountBuild() {
        Product product = new Product(1L, null);

        return new CreateOrderRequestDto(List.of(product));
    }

    public static CreateOrderRequestDto invalidProductCountBuild() {
        Product product = new Product(1L, 10000001);

        return new CreateOrderRequestDto(List.of(product));
    }

}
