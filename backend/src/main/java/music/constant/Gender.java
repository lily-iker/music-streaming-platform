package music.constant;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = Gender.GenderDeserializer.class)
public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    @JsonDeserialize(using = GenderDeserializer.class)
    public static class GenderDeserializer extends JsonDeserializer<Gender> {
        @Override
        public Gender deserialize(JsonParser jsonParser,
                                  DeserializationContext deserializationContext)
                throws IOException, JacksonException {
            String value = jsonParser.getText().toUpperCase();
            return Gender.valueOf(value);
        }
    }
}

