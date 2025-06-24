package example.rollingpager.global.presentation;

import example.rollingpager.global.presentation.dto.SignInCauseDto;
import example.rollingpager.global.presentation.dto.SignInResultDto;
import example.rollingpager.global.presentation.dto.SignUpCauseDto;
import example.rollingpager.global.presentation.dto.SignUpResultDto;
import example.rollingpager.global.service.SignService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SignController {
    private final Logger logger = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInCauseDto request, HttpServletResponse response) throws RuntimeException {
        logger.info("SignContoller : signIn() - 로그인을 시도하고 있습니다. id : {}, pw : ****", request.getUserId());
        HttpHeaders headers = new HttpHeaders();
        SignInResultDto signInResultDto = signService.signIn(request, response);

        if (signInResultDto.getCode() == 0) {
            logger.info("SignController : signIn() - 정상적으로 로그인되었습니다. id : {}", request.getUserId());
            headers.set("access", signInResultDto.getAccess());
            headers.set("refresh", signInResultDto.getRefresh());
        } else {
            logger.error("SignController : signIn() - 로그인 실패. id : {}", request.getUserId());
        }
        return ResponseEntity.ok().headers(headers).body(signInResultDto);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpCauseDto request) {
        logger.info("SignController : signUp() - 회원가입을 시도합니다. id : {}", request.getUserId());
        SignUpResultDto signUpResultDto = signService.signUp(request);

        if (signUpResultDto.getCode() == 0) {
            logger.info("SignController : signUp() - 회원가입이 완료되었습니다. id : {}", request.getUserId());
        } else {
            logger.error("SignController : signUp() - 회원가입 실패. id : {}", request.getUserId());
        }
        return ResponseEntity.ok(signUpResultDto);
    }
}
