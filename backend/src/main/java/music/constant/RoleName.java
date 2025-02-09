package music.constant;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = RoleName.RoleDeserializer.class)
public enum RoleName {
    ADMIN,
    USER,
    ARTIST,
    MOD;

    public static class RoleDeserializer extends JsonDeserializer<RoleName> {
        @Override
        public RoleName deserialize(JsonParser jsonParser,
                                    DeserializationContext deserializationContext)
                throws IOException, JacksonException {
            String value = jsonParser.getText().toUpperCase();
            return RoleName.valueOf(value);
        }
    }
}
