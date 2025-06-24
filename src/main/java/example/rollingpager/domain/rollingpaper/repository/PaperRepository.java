package example.rollingpager.domain.rollingpaper.repository;

import example.rollingpager.domain.rollingpaper.entity.Paper;
import example.rollingpager.domain.rollingpaper.entity.RollingPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaperRepository extends JpaRepository<Paper,Long> {
    List<Paper> findByRollingPaper(RollingPaper rollingPaper);
}
