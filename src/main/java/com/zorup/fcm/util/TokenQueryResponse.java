package com.zorup.fcm.util;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenQueryResponse {
    private boolean success;
    private Long code;
    private String msg;
    private List<UserTokenInfo> list;

    @Data
    public static class UserTokenInfo{
        private Long userId;
        private String pushToken;
    }
}
