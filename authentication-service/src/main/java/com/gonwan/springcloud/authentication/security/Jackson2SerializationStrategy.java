package com.gonwan.springcloud.authentication.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Deserializer;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;

import java.io.IOException;

class MyOAuth2AccessTokenJackson2Serializer extends StdSerializer<OAuth2AccessToken>  {

    private OAuth2AccessTokenJackson2Serializer oAuth2AccessTokenJackson2Serializer;

    public MyOAuth2AccessTokenJackson2Serializer() {
        super(OAuth2AccessToken.class);
        oAuth2AccessTokenJackson2Serializer = new OAuth2AccessTokenJackson2Serializer();
    }

    @Override
    public void serializeWithType(OAuth2AccessToken value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        serialize(value, gen, serializers);
    }

    @Override
    public void serialize(OAuth2AccessToken value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        oAuth2AccessTokenJackson2Serializer.serialize(value, gen, serializers);
    }

}

class MyOAuth2AccessTokenJackson2Deserializer extends StdDeserializer<OAuth2AccessToken> {

    private OAuth2AccessTokenJackson2Deserializer oAuth2AccessTokenJackson2Deserializer;

    public MyOAuth2AccessTokenJackson2Deserializer() {
        super(OAuth2AccessToken.class);
        oAuth2AccessTokenJackson2Deserializer = new OAuth2AccessTokenJackson2Deserializer();
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(p, ctxt);
    }

    @Override
    public OAuth2AccessToken deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return oAuth2AccessTokenJackson2Deserializer.deserialize(p, ctxt);
    }

}

@JsonSerialize(using = MyOAuth2AccessTokenJackson2Serializer.class)
@JsonDeserialize(using = MyOAuth2AccessTokenJackson2Deserializer.class)
abstract class OAuth2AccessTokenMixIn {

}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
abstract class OAuth2AuthenticationMixIn {

    public OAuth2AuthenticationMixIn(
            @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class") @JsonProperty("oauth2Request") OAuth2Request storedRequest,
            @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class") @JsonProperty("userAuthentication") Authentication userAuthentication) {

    }

}

public class Jackson2SerializationStrategy extends StandardStringSerializationStrategy {

    private GenericJackson2JsonRedisSerializer serializer;

    public Jackson2SerializationStrategy() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.registerModule(new CoreJackson2Module());
        objectMapper.addMixIn(OAuth2AccessToken.class, OAuth2AccessTokenMixIn.class);
        objectMapper.addMixIn(OAuth2Authentication.class, OAuth2AuthenticationMixIn.class);
        serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Override
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        return serializer.deserialize(bytes, clazz);
    }

    @Override
    protected byte[] serializeInternal(Object object) {
        return serializer.serialize(object);
    }

}
