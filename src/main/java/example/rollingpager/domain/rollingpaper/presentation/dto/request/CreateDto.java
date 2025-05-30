package example.rollingpager.domain.rollingpaper.presentation.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateDto {
    @NotBlank
    private String recipient;
    private String password;
    private String pscheck;

    @AssertTrue(message = "비밀번호가 비밀번호확인과 같지 않습니다.")
    public boolean isValid(){
        return password.equals(pscheck);
    }
}