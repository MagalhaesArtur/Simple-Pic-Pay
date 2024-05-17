package com.picpaysimp.dtos;

import com.picpaysimp.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document, UserType type, BigDecimal balance, String email, String password) {
}
