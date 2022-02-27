package hanu.gdsc.problem.repositories.entities;

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
    private String programmingLanguage;
    private double timeLimit;
    @Column(name="version")
    @Version
    private int version;
}