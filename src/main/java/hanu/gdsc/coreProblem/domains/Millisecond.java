package hanu.gdsc.coreProblem.domains;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hanu.gdsc.coreProblem.controllers.jackson.MillisecondDeserializer;
import hanu.gdsc.coreProblem.controllers.jackson.MillisecondSerializer;

@JsonSerialize(using = MillisecondSerializer.class)
@JsonDeserialize(using = MillisecondDeserializer.class)
public class Millisecond {
    private long value;
    
    public Millisecond(Long millisecond) {
        this.value = millisecond;
    }

    public static Millisecond fromSecond(Float val) {
        long m = (long) (val * 1000);
        return new Millisecond(m);
    }

    public boolean greaterThan(Millisecond that) {
        return this.value > that.value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Millisecond{" +
                "value=" + value +
                '}';
    }
}
