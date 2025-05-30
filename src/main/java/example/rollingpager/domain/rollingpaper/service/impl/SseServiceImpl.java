package example.rollingpager.domain.rollingpaper.service.impl;

import example.rollingpager.domain.rollingpaper.presentation.dto.response.PaperDto;
import example.rollingpager.domain.rollingpaper.repository.RollingPaperRepository;
import example.rollingpager.domain.rollingpaper.service.SseService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {

    private final Map<String, CopyOnWriteArrayList<SseEmitter>> urlEmitters = new ConcurrentHashMap<>();
    private final RollingPaperRepository rollingPaperRepository;
    private final Logger log = LoggerFactory.getLogger(SseServiceImpl.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SseEmitter createSseEmitter(String url) {
        if (!rollingPaperRepository.existsByUrl(url)) {
            throw new IllegalArgumentException("존재하지 않은 URL입니다.");
        }

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);  // 변경

        urlEmitters.computeIfAbsent(url, key -> new CopyOnWriteArrayList<>())
                .add(emitter);

        emitter.onCompletion(() -> urlEmitters.get(url).remove(emitter));
        emitter.onTimeout(() -> {
            urlEmitters.get(url).remove(emitter);
            System.out.println("SSE 타임아웃 발생, 연결 해제");
        });

        emitter.onError(e -> {
            urlEmitters.get(url).remove(emitter);
            System.out.println("SSE 에러 발생: " + e.getMessage());
        });

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("SSE (URL : " + url + ")"));

        } catch (IOException e) {
            urlEmitters.get(url).remove(emitter);
            System.out.println("SSE 이벤트 전송 실패: " + e.getMessage());
        }

        return emitter;
    }


    public String sendSse(String url, PaperDto paperDto) {
        if (!rollingPaperRepository.existsByUrl(url)) {
            throw new IllegalArgumentException("존재하지 않은 URL입니다.");
        }

        List<SseEmitter> emitters = urlEmitters.get(url);
        if (emitters == null || emitters.isEmpty()) {
            urlEmitters.remove(url);
            return "failure";
        }

        List<SseEmitter> failedEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("paper")
                        .data(paperDto));
            } catch (IOException e) {
                log.debug("SSE 전송 실패 (클라이언트 연결 종료): {}", e.getMessage());
                emitter.completeWithError(e);
                failedEmitters.add(emitter);
            }
        }

        emitters.removeAll(failedEmitters);
        return "success";
    }

    @PostConstruct
    public void startPingTask() {
        scheduler.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, CopyOnWriteArrayList<SseEmitter>> entry : urlEmitters.entrySet()) {
                CopyOnWriteArrayList<SseEmitter> emitters = entry.getValue();
                for (SseEmitter emitter : emitters) {
                    try {
                        emitter.send(SseEmitter.event()
                                .name("ping")
                                .data("keepalive"));
                        log.info("Ping 전송: {}", entry.getKey());
                    } catch (IOException e) {
                        log.info("Ping 실패로 emitter 제거: {}", e.getMessage());
                        emitter.completeWithError(e);
                        emitters.remove(emitter);
                    }
                }
            }
        }, 10, 30, TimeUnit.SECONDS);
    }


    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }
}

