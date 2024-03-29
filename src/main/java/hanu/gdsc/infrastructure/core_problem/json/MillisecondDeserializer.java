package hanu.gdsc.infrastructure.core_problem.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import hanu.gdsc.domain.core_problem.models.Millisecond;

import java.io.IOException;

public class MillisecondDeserializer extends StdDeserializer<Millisecond> {
    public MillisecondDeserializer() {
        this(null);
    }

    protected MillisecondDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Millisecond deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return new Millisecond(jsonParser.getValueAsLong());
    }
}