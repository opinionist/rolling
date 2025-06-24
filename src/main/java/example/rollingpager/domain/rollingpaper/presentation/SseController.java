package example.rollingpager.domain.rollingpaper.presentation;

import example.rollingpager.domain.rollingpaper.presentation.dto.response.PaperDto;
import example.rollingpager.domain.rollingpaper.service.SseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping
@RequiredArgsConstructor
//@CrossOrigin(origins = "localhost:3000", allowCredentials = "true")
public class SseController {
    private final SseService sseService;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(SseController.class);

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sse(@RequestParam String url) {
        logger.info("SSE 요청 URL: {}", url);
        return sseService.createSseEmitter(url);
    }

    @PostMapping("/sse")
    public ResponseEntity<?> ssePost(@RequestParam String url, @RequestBody PaperDto paperDto) {
        return ResponseEntity.ok(sseService.sendSse(url,paperDto));
    }
}