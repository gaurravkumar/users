package com.prototype.users.dto;

import java.time.LocalDateTime;

public record UserOutputDTO(String name, String token, LocalDateTime date, String error) {
}
