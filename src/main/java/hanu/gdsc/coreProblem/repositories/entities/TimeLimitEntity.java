package hanu.gdsc.coreProblem.repositories.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.*;

@Entity
@Table(name="timeLimit")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TimeLimitEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @ManyToOne
    @JoinColumn(name="problem_uuid")
    private ProblemEntity problem;
    private List<String> programmingLanguage;
    private Long timeLimit;
    @Column(name="version")
    @Version
    private Long version;
}
