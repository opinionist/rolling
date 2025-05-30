package example.rollingpager.domain.rollingpaper.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class InsertDto {
    @NotBlank
    private String content;
    @NotBlank
    private String sender;
}
