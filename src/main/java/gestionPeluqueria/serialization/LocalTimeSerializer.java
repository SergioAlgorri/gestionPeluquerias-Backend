package gestionPeluqueria.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeSerializer extends StdSerializer<LocalTime> {

    public LocalTimeSerializer() {
        this(null);
    }

    public LocalTimeSerializer(Class<LocalTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.toString());
    }
}