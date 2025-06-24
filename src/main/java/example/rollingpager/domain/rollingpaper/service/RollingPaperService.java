package example.rollingpager.domain.rollingpaper.service;

import example.rollingpager.domain.rollingpaper.presentation.dto.request.CreateDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.request.FinishDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.request.InsertDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.response.GetDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.response.PaperDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.response.RollingPaperDto;

import java.util.List;

public interface RollingPaperService {
    RollingPaperDto create(CreateDto request);
    PaperDto insert(String page, InsertDto request);
    RollingPaperDto finish(String page, FinishDto request);
    GetDto get(String page);
    List<GetDto> getAll();
}
