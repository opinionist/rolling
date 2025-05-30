package example.rollingpager.domain.rollingpaper.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaperDto {
    private String sender;
    private String content;
}
