package com.tracktopiasapi.one.model.mapper;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tracktopiasapi.one.web.controller.auth.JWTToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JWTTokenMapper {

    @Mapping(target = "expiresAt", ignore = true)
    JWTToken toPayload(DecodedJWT jwt);
}