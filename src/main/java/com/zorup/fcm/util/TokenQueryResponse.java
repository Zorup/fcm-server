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
    private List<String> list;
}
