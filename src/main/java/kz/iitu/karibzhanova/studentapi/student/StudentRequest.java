package kz.iitu.karibzhanova.studentapi.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/** Data the client sends when creating or updating a student. */
public record StudentRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        String major,
        @Min(1) @Max(4) Integer course
) {
}
