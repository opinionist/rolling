package example.rollingpager.domain.rollingpaper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RollingPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;
    @Column(unique = true)
    private String url;
    private boolean finished = false;
    @OneToMany(mappedBy = "rollingPaper",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Paper> papers;

    private String password;


}
