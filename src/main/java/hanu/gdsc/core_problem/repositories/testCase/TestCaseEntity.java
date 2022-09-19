package hanu.gdsc.core_problem.repositories.testCase;

import hanu.gdsc.core_problem.domains.TestCase;
import lombok.*;

import java.lang.reflect.Constructor;

import javax.persistence.*;

@Entity
@Table(name = "core_problem_test_case")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseEntity {
    @Id
    @Column(columnDefinition = "VARCHAR(30)")
    private String id;
    private String problemId;
    @Column(columnDefinition = "LONGTEXT")
    private String input;
    @Column(columnDefinition = "LONGTEXT")
    private String expectedOutput;
    private int ordinal;
    private boolean isSample;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String serviceToCreate;

    public static TestCaseEntity toEntity(TestCase testCase) {
        return TestCaseEntity.builder()
                .id(testCase.getProblemId() + "#" + testCase.getOrdinal())
                .problemId(testCase.getProblemId().toString())
                .input(testCase.getInput())
                .expectedOutput(testCase.getExpectedOutput())
                .ordinal(testCase.getOrdinal())
                .isSample(testCase.isSample())
                .description(testCase.getDescription())
                .serviceToCreate(testCase.getServiceToCreate())
                .build();
    }

    public static TestCase toDomain(TestCaseEntity testCaseEntity) {
        try {
            Constructor<TestCase> constructor = TestCase.class.getDeclaredConstructor(
                hanu.gdsc.share.domains.Id.class,
                String.class,
                String.class,
                Integer.TYPE,
                Boolean.TYPE,
                String.class, 
                String.class
            );
            constructor.setAccessible(true);
            return constructor.newInstance(
                new hanu.gdsc.share.domains.Id(testCaseEntity.getProblemId()),
                testCaseEntity.getInput(),
                testCaseEntity.getExpectedOutput(),
                testCaseEntity.getOrdinal(),
                testCaseEntity.isSample(),
                testCaseEntity.getDescription(),
                testCaseEntity.getServiceToCreate()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }
}
