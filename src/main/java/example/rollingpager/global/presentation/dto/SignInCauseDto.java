package example.rollingpager.global.presentation.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignInCauseDto {
    @NonNull
    private String userId;

    @NonNull
    private String password;
}
