package music.constant;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = PermissionName.PermissionDeserializer.class)
public enum PermissionName {
    CREATE_USER,
    GET_USER,
    UPDATE_USER,
    DELETE_USER;
    // ,etc...

    public static class PermissionDeserializer extends JsonDeserializer<PermissionName> {
        @Override
        public PermissionName deserialize(JsonParser jsonParser,
                                          DeserializationContext deserializationContext)
                throws IOException, JacksonException {
            String value = jsonParser.getText().toUpperCase();
            return PermissionName.valueOf(value);
        }
    }
}
