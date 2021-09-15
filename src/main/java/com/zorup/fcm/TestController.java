package com.zorup.fcm;

import com.zorup.fcm.util.MainModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/v1")
public class TestController {

    private final MainModule mainModule;

    @GetMapping("/test")
    public long sendFcmMessage(String pushToken){
        return mainModule.getUserIdByFcmToken(pushToken);
    }
    //리스트로 처리? 일단 화면단에서 보낼 데이터 가공하면서 추가적으로 생각해볼것..
}
