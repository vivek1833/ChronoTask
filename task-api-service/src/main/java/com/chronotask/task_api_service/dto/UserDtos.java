package com.chronotask.task_api_service.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDtos {
        public record CreateUserRequest(
                        @NotBlank String name,
                        @Email @NotBlank String email) {
        }

        public record UserResponse(
                        UUID id,
                        String name,
                        String email) {
        }
}
