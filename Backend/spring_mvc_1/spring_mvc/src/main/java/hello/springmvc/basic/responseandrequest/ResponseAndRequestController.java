package hello.springmvc.basic.responseandrequest;

import hello.springmvc.basic.request.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ResponseAndRequestController {

    @GetMapping("/response-and-request-v1")
    public ResponseEntity<HelloData> responseAndRequestV1(@RequestBody HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return ResponseEntity.ok(helloData);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/response-and-request-v2")
    public HelloData responseAndRequestV2(@RequestBody HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return helloData;
    }
}
