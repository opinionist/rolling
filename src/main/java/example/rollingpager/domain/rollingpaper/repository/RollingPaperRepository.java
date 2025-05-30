package example.rollingpager.domain.rollingpaper.repository;

import example.rollingpager.domain.rollingpaper.entity.RollingPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RollingPaperRepository extends JpaRepository<RollingPaper, Long> {
    boolean existsByUrl(String url);
    Optional<RollingPaper> findByUrlAndFinishedFalse(String url);
    Optional<RollingPaper> findByUrl(String url);
}
