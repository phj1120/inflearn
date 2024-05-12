package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class ServletExController {

    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생");
    }

    @GetMapping("/error-400")
    public void error400(HttpServletResponse response) throws IOException {
        response.sendError(400, "400 오류");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류");
    }

    @GetMapping("/error-405")
    public void error405(HttpServletResponse response) throws IOException {
        response.sendError(405, "405 오류");
    }

    @GetMapping("/error-300")
    public void error300(HttpServletResponse response) throws IOException {
        response.sendError(300, "300 오류");
    }

    @GetMapping("/error-301")
    public void error301(HttpServletResponse response) throws IOException {
        response.sendError(301, "301 오류");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }

}
