package example.rollingpager.domain.rollingpaper.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetDto {
    private String recipient;
    private boolean finished;
    private String url;
    private List<PaperDto> papers;
}
