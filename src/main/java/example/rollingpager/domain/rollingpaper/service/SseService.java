package example.rollingpager.domain.rollingpaper.service;

import example.rollingpager.domain.rollingpaper.presentation.dto.response.PaperDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    SseEmitter createSseEmitter(String url);
    String sendSse(String url, PaperDto paperDto);
}
