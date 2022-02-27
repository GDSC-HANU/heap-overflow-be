package hanu.gdsc.problem.repositories.entities;

import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.*;

@Entity
@Table(name="memoryLimit")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MemoryLimitEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @ManyToOne()
    @JoinColumn(name="problem_uuid")
    private ProblemEntity problem;

    @Column(name="programmingLanguage")
    private String programmingLanguage;

    @Column(name="memoryLimit")
    private double memoryLimit;

    @Column(name="version")
    @Version
    private int version;
}