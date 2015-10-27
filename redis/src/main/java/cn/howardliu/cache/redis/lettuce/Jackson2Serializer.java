package cn.howardliu.cache.redis.lettuce;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import net.sf.json.JSONNull;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * <p>create at 15-10-22</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public class Jackson2Serializer implements StringValueSerializer {
    private ObjectMapper objectMapper;

    public Jackson2Serializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(JSONNull.class, NullSerializer.instance);
        this.objectMapper
                .setSerializerFactory(this.objectMapper.getSerializerFactory().withAdditionalSerializers(serializers));
        this.objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        this.objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> String serialize(T value) {
        if (value == null) {
            return "";
        }
        try {
            return this.objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(String stringValue, Class<T> clazz) {
        if (StringUtils.isBlank(stringValue)) {
            return null;
        }
        try {
            return this.objectMapper.readValue(stringValue, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
