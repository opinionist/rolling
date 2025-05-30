package example.rollingpager.domain.rollingpaper.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RollingPaperDto {
    private String url;
    private String recipient;
    private boolean finished;
}
