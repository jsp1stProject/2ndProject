package com.sist.web.user.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class KakaoUserDTO {
    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Properties{
        private String nickname, thumbnail_image, profile_image;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class KakaoAccount{
        private String email;
    }
}
