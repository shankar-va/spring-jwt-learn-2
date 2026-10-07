package com.example.spring_jwtlearn.mapper;

import com.example.spring_jwtlearn.dto.RegistrationRequest;
import com.example.spring_jwtlearn.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface RegistrationRequestMapper {

    User toEntity(RegistrationRequest request);

}
