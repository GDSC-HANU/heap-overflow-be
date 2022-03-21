package hanu.gdsc.share.controller.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import hanu.gdsc.share.domains.DateTime;

import java.io.IOException;

public class DateTimeSerializer extends StdSerializer<DateTime> {
    protected DateTimeSerializer() {
        this(null);
    }


    public DateTimeSerializer(Class<DateTime> t) {
        super(t);
    }

    @Override
    public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(dateTime.toString());
    }
}
