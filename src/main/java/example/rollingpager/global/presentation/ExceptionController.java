package example.rollingpager.global.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exceptions")
public class ExceptionController {
    @GetMapping("/access-denied")
    public void accessDenied() {
        throw new RuntimeException("권한이 없는 사용자 접근 시도");
    }
}
