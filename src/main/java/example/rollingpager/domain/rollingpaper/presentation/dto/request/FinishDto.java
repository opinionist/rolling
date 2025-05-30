package example.rollingpager.domain.rollingpaper.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FinishDto {
    @NotBlank
    private String password;
}
