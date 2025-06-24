package example.rollingpager.global.presentation.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpCauseDto {
    @NonNull
    private String userId;

    @NonNull
    private String password;

    @NonNull
    private String passwordCheck;

    @NonNull
    private String nickname;

    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    public boolean isPasswordMatching() {
        return password.equals(passwordCheck);
    }
}
