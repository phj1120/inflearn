package hello.springmvc.basic.response;

import hello.springmvc.basic.request.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
//@ResponseBody // 클래스 레벨에 붙여 주면 각 메서드 마다 @ResponseBody 를 넣어 줄 필요 없음
//@RestController //@Controller + @ResponseBody
public class ResponseBodyController {

    // String

    @GetMapping("/response-body-string-v1")
    public void responseBodyStringV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyStringV2() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyStringV3() {
        return "ok";
    }

    // JSON

    /**
     * HTTP status 를 동적으로 관리하고 싶을 경우 ResponseEntity 사용
     */
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() {
        HelloData data = new HelloData();
        data.setUsername("phj");
        data.setAge(25);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * 이 방식을 많이 사용
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2() {
        HelloData data = new HelloData();
        data.setUsername("phj");
        data.setAge(25);
        return data;
    }

}
