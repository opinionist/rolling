package example.rollingpager.domain.rollingpaper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private String content;
    private String sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paper_id")
    private RollingPaper rollingPaper;
}
