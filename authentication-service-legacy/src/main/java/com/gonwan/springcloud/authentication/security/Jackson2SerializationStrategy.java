package com.gonwan.springcloud.authentication.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Deserializer;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Serializer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonSerialize(using = OAuth2AccessTokenJackson2Serializer.class)
@JsonDeserialize(using = OAuth2AccessTokenJackson2Deserializer.class)
abstract class OAuth2AccessTokenMixIn {

}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
abstract class OAuth2AuthenticationMixIn {

    public OAuth2AuthenticationMixIn(
            @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) @JsonProperty("oauth2Request") OAuth2Request storedRequest,
            @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) @JsonProperty("userAuthentication") Authentication userAuthentication) {

    }

}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
abstract class OAuth2RequestMixIn {

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
        objectMapper.addMixIn(OAuth2Request.class, OAuth2RequestMixIn.class);
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
