package com.devpath.domain.user.contorller;

import com.devpath.domain.user.dto.CardPrevDto;
import com.devpath.domain.user.dto.MyCardDto;
import com.devpath.global.apiPayload.ApiResponse;

public class UserController implements UserControllerDocs{

    @Override
    public ApiResponse<MyCardDto> getMyCard() {
        return null;
    }

    @Override
    public ApiResponse<CardPrevDto> getCards() {
        return null;
    }
}
