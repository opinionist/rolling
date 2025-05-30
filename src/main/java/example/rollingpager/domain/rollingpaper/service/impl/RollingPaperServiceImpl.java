package example.rollingpager.domain.rollingpaper.service.impl;

import example.rollingpager.domain.rollingpaper.entity.RollingPaper;
import example.rollingpager.domain.rollingpaper.entity.Paper;
import example.rollingpager.domain.rollingpaper.presentation.dto.request.CreateDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.request.FinishDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.request.InsertDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.response.GetDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.response.PaperDto;
import example.rollingpager.domain.rollingpaper.presentation.dto.response.RollingPaperDto;
import example.rollingpager.domain.rollingpaper.repository.RollingPaperRepository;
import example.rollingpager.domain.rollingpaper.repository.PaperRepository;
import example.rollingpager.domain.rollingpaper.service.RollingPaperService;
import example.rollingpager.global.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RollingPaperServiceImpl implements RollingPaperService {
    private final RollingPaperRepository rollingPaperRepository;
    private final PaperRepository paperRepository;

    @Transactional
    public RollingPaperDto create(CreateDto request){
        String password = PasswordUtil.encode(request.getPassword());

        String url;
        do {
            url = generateRandomString(35);
        } while (rollingPaperRepository.existsByUrl(url));

        RollingPaper rollingPaper = RollingPaper.builder()
                .recipient(request.getRecipient())
                .password(password)
                .url(url)
                .build();

        rollingPaperRepository.save(rollingPaper);
        return RollingPaperDto.builder()
                .recipient(request.getRecipient())
                .url(rollingPaper.getUrl())
                .finished(rollingPaper.isFinished())
                .build();
    }

    @Transactional
    public PaperDto insert(String page, InsertDto request){
        RollingPaper rollingPaper = getPaperByURL(page);
        Paper paper = Paper.builder()
                .rollingPaper(rollingPaper)
                .sender(request.getSender())
                .content(request.getContent())
                .build();
        paperRepository.save(paper);
        return PaperDto.builder()
                .sender(request.getSender())
                .content(request.getContent())
                .build();
    }

    @Transactional
    public RollingPaperDto finish(String page, FinishDto request){
        RollingPaper rollingPaper = getPaperByURL(page);
        if(!PasswordUtil.matches(request.getPassword(),rollingPaper.getPassword())){
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }
        rollingPaperRepository.save(rollingPaper.toBuilder()
                        .finished(true)
                .build());
        return RollingPaperDto.builder()
                .finished(true)
                .recipient(rollingPaper.getRecipient())
                .url(rollingPaper.getUrl())
                .build();
    }

    public GetDto get(String page){
        RollingPaper rollingPaper = rollingPaperRepository.findByUrl(page).orElseThrow(()-> new IllegalArgumentException("존재 하지 않은 URL입니다."));
        List<Paper> papers = paperRepository.findByRollingPaper(rollingPaper);
        List<PaperDto> paperDtos = new ArrayList<>();
        for(Paper paper : papers){
            paperDtos.add(PaperDto.builder()
                    .sender(paper.getSender())
                    .content(paper.getContent())
                    .build());
        }
        return GetDto.builder()
                .url(rollingPaper.getUrl())
                .recipient(rollingPaper.getRecipient())
                .papers(paperDtos)
                .finished(rollingPaper.isFinished())
                .build();
    }

    private RollingPaper getPaperByURL(String url){
        return rollingPaperRepository.findByUrlAndFinishedFalse(url).orElseThrow(()-> new IllegalArgumentException("존재 하지 않거나, 이미 완료된 URL입니다."));
    }
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }
}
