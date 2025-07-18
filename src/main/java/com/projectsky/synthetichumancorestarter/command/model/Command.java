package com.projectsky.synthetichumancorestarter.command.model;

import com.projectsky.synthetichumancorestarter.command.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Command {

    @NotBlank(message = "Должно быть описание")
    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    @NotNull(message = "Должен быть объявлен приоритет")
    private Priority priority;

    @NotBlank(message = "Должен быть автор")
    @Size(max = 100, message = "Имя не должно превышать 100 символов")
    private String author;

    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?$",
            message = "Дата должна быть в ISO 8601 формате")
    private String time;
}
