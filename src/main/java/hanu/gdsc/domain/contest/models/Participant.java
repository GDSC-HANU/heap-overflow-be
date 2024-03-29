package hanu.gdsc.domain.contest.models;

import hanu.gdsc.domain.contest.exception.ContestEndedException;
import hanu.gdsc.domain.share.models.DateTime;
import hanu.gdsc.domain.share.models.Id;
import hanu.gdsc.domain.share.models.VersioningDomainObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Participant extends VersioningDomainObject {
    private Id coderId;
    private Id contestId;
    private List<ProblemScore> problemScores;
    private DateTime createdAt;

    private Participant(long version,
                        Id coderId,
                        Id contestId,
                        List<ProblemScore> problemScores,
                        DateTime createdAt) {
        super(version);
        this.coderId = coderId;
        this.contestId = contestId;
        this.problemScores = problemScores;
        this.createdAt = createdAt;
    }

    public static Participant create(Id coderId, Contest contest) throws ContestEndedException {
        if (contest.ended()) {
            throw new ContestEndedException();
        }
        return new Participant(
                0,
                coderId,
                contest.getId(),
                new ArrayList<>(),
                DateTime.now()
        );
    }

    public void updateProblemScore(double score,
                                   int problemOrdinal,
                                   boolean accepted) {
        for (ProblemScore problemScore : problemScores) {
            if (problemScore.getProblemOrdinal() == problemOrdinal) {
                accepted = problemScore.isAccepted() || accepted;
                final double finalScore = Math.max(score, problemScore.getScore());
                problemScores.remove(problemScore);
                problemScores.add(new ProblemScore(
                        problemOrdinal,
                        finalScore,
                        accepted
                ));
                break;
            }
        }
    }

    public double totalScore() {
        double res = 0;
        for (ProblemScore score : problemScores)
            res += score.getScore();
        return res;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return coderId.equals(that.coderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coderId);
    }

    public Id getCoderId() {
        return coderId;
    }

    public Id getContestId() {
        return contestId;
    }

    public List<ProblemScore> getProblemScores() {
        return problemScores;
    }
}
