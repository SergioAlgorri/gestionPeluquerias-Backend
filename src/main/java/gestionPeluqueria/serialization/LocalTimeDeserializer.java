package gestionPeluqueria.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeDeserializer extends StdDeserializer<LocalTime> {

    public LocalTimeDeserializer() {
        this(null);
    }

    public LocalTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalTime deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JacksonException {
        String time = jsonparser.getText();
        return LocalTime.parse(time);
    }
}