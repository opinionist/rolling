package example.rollingpager.domain.rollingpaper.repository;

import example.rollingpager.domain.rollingpaper.entity.RollingPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RollingPaperRepository extends JpaRepository<RollingPaper, Long> {
    boolean existsByUrl(String url);
    Optional<RollingPaper> findByUrlAndFinishedFalse(String url);
    Optional<RollingPaper> findByUrl(String url);
    @Query(value = "SELECT * FROM rolling_paper ORDER BY RAND() LIMIT 6", nativeQuery = true)
    List<RollingPaper> findRandomPapers();
}
