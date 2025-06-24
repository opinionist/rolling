package example.rollingpager.global.presentation.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class SignInResultDto extends SignUpResultDto{
    private String access;
    private String refresh;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token, String refresh) {
        super(success, code, msg);
        this.access = token;
        this.refresh = refresh;
    }
}
