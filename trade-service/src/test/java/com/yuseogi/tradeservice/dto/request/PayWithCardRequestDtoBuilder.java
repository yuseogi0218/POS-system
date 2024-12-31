package com.yuseogi.tradeservice.dto.request;

public class PayWithCardRequestDtoBuilder {

    public static PayWithCardRequestDto build() {
        return new PayWithCardRequestDto("K");
    }

    public static PayWithCardRequestDto nullCardCompanyBuild() {
        return new PayWithCardRequestDto(null);
    }

    public static PayWithCardRequestDto emptyCardCompanyBuild() {
        return new PayWithCardRequestDto("");
    }

    public static PayWithCardRequestDto invalidCardCompanyBuild() {
        return new PayWithCardRequestDto("CARD_COMPANY");
    }
}
