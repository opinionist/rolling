package example.rollingpager.domain.rollingpaper.presentation;

import example.rollingpager.domain.rollingpaper.presentation.dto.request.CreateDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.request.FinishDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.request.InsertDto;
import example.rollingpager.domain.rollingpaper.service.RollingPaperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rolling-paper")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class RollingPaperController {
    private final RollingPaperService rollingPaperService;
    private final Logger logger = LoggerFactory.getLogger(RollingPaperController.class);

    @PostMapping("/create")
    public ResponseEntity<?> rollingPaper(@Valid @RequestBody CreateDto request) {
        return ResponseEntity.ok(rollingPaperService.create(request));
    }

    @PostMapping("/{page}/insert")
    public ResponseEntity<?> paper(@PathVariable String page, @Valid @RequestBody InsertDto request){
        logger.info(page);
        return ResponseEntity.ok(rollingPaperService.insert(page,request));
    }

    @PutMapping("/{page}/finish")
    public ResponseEntity<?> finish(@PathVariable String page, @Valid @RequestBody FinishDto request){
        logger.info(page);
        return ResponseEntity.ok(rollingPaperService.finish(page,request));
    }

    @GetMapping("/{page}")
    public ResponseEntity<?> paper(@PathVariable String page){
        logger.info(page);
        return ResponseEntity.ok(rollingPaperService.get(page));
    }
}
