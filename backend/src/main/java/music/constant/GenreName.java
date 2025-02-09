package music.constant;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = GenreName.GenreDeserializer.class)
public enum GenreName {
    POP,
    INDIE,
    HIP_HOP,
    R_AND_B,
    PHONK,
    EDM,
    JAZZ,
    COUNTRY,
    PUNK,
    ROCK;
    //etc...

    @JsonDeserialize(using = GenreDeserializer.class)
    public static class GenreDeserializer extends JsonDeserializer<GenreName> {
        @Override
        public GenreName deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext)
                throws IOException, JacksonException {
            String value = jsonParser.getText().toUpperCase();
            return GenreName.valueOf(value);
        }
    }
}
