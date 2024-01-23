package com.example.goormoa.controller;


import com.example.goormoa.entity.User;
import com.example.goormoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class OAuthController {

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final String CLIENT_ID = "3d83704a506c101d079ccb4927d62bce";
    private final String REDIRECT_URI = "http://localhost:8081/kakao";

    @Autowired
    private UserRepository userRepository;

    public String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", CLIENT_ID);
        parameters.add("redirect_uri", REDIRECT_URI);
        parameters.add("code", code);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(KAKAO_TOKEN_URL, parameters, String.class);
        JSONObject body = new JSONObject(responseEntity.getBody());

        return body.getString("access_token");
    }

    public JSONObject getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(KAKAO_USERINFO_URL, request, String.class);

        return new JSONObject(responseEntity.getBody());
    }


    @GetMapping("/kakao")
    public String kakaoCallback(@RequestParam String code) {
        String accessToken = getAccessToken(code);
        JSONObject userInfo = getUserInfo(accessToken);
        String nickname = userInfo.getJSONObject("properties").getString("nickname");

        User newUser = new User();
        newUser.setNickname(nickname);
        userRepository.save(newUser);


        return "mainPage";
    }
}
