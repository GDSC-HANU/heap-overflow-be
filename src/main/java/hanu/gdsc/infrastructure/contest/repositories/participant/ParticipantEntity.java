package hanu.gdsc.infrastructure.contest.repositories.participant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.gdsc.domain.contest.models.Participant;
import hanu.gdsc.domain.contest.models.ProblemScore;
import hanu.gdsc.domain.share.models.DateTime;
import lombok.*;

import javax.persistence.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contest_participant")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantEntity {
    @Id
    @Column(columnDefinition = "VARCHAR(60)")
    private String id;
    @Version
    private long version;
    @Column(columnDefinition = "VARCHAR(30)")
    private String coderId;
    @Column(columnDefinition = "VARCHAR(30)")
    private String contestId;
    private String problemScores;
    private String createdAt;
    private long createdAtMillis;
    private double totalScore;

    public static ParticipantEntity fromDomains(Participant participant, ObjectMapper objectMapper) {
        try {
            return ParticipantEntity.builder()
                    .id(participant.getCoderId() + "#" + participant.getContestId())
                    .version(participant.getVersion())
                    .coderId(participant.getCoderId().toString())
                    .contestId(participant.getContestId().toString())
                    .problemScores(objectMapper.writeValueAsString(participant.getProblemScores()))
                    .createdAt(participant.getCreatedAt().toString())
                    .createdAtMillis(participant.getCreatedAt().toMillis())
                    .totalScore(participant.totalScore())
                    .build();
        } catch (JsonProcessingException e) {
            throw new Error(e);
        }
    }

    public Participant toDomain(ObjectMapper objectMapper) {
        try {
            Constructor<Participant> con = Participant.class.getDeclaredConstructor(
                    long.class,
                    hanu.gdsc.domain.share.models.Id.class,
                    hanu.gdsc.domain.share.models.Id.class,
                    List.class,
                    DateTime.class
            );
            con.setAccessible(true);
            return con.newInstance(
                    version,
                    new hanu.gdsc.domain.share.models.Id(coderId),
                    new hanu.gdsc.domain.share.models.Id(contestId),
                    objectMapper.readValue(problemScores, new TypeReference<List<ProblemScore>>() {}),
                    new DateTime(createdAt)
            );
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
