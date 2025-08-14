package com.sd20201.datn.infrastructure.secutiry.oauth2.user;


import com.sd20201.datn.infrastructure.constant.AuthProvider;
import com.sd20201.datn.infrastructure.secutiry.exception.OAuth2AuthenticationProcessingException;
import lombok.extern.slf4j.Slf4j;


import java.util.Map;

@Slf4j
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        log.info("OAuth2UserInfoFactory ===> registrationId = {}, attributes = {} ", registrationId, attributes);
        if (registrationId.equalsIgnoreCase(AuthProvider.google.name())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId +" is not supported yet.");
        }
    }

}
