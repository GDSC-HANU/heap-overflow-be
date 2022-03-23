package hanu.gdsc.practiceProblem.controllers.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import hanu.gdsc.coreProblem.domains.ProgrammingLanguage;
import hanu.gdsc.practiceProblem.domains.Difficulty;

import java.io.IOException;

public class DifficultyDeserializer extends StdDeserializer<Difficulty> {
    public DifficultyDeserializer() {
        this(null);
    }

    protected DifficultyDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Difficulty deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return Difficulty.from(jsonParser.getValueAsString());
    }
}
