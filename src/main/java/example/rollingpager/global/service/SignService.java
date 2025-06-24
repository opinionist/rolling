package example.rollingpager.global.service;

import example.rollingpager.global.presentation.dto.SignInCauseDto;
import example.rollingpager.global.presentation.dto.SignInResultDto;
import example.rollingpager.global.presentation.dto.SignUpCauseDto;
import example.rollingpager.global.presentation.dto.SignUpResultDto;
import jakarta.servlet.http.HttpServletResponse;

public interface SignService {
    SignUpResultDto signUp(SignUpCauseDto request);

    SignInResultDto signIn(SignInCauseDto request, HttpServletResponse response) throws RuntimeException;
}
