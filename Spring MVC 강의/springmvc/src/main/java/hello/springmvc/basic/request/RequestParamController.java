package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String age = req.getParameter("age");
        log.info("username={}, age={}", username, age);
        resp.getWriter().write("ok");
    }

    @ResponseBody // 해당 태그를 통해 response시 바로 문자 반환됨 -- @RestController 인 것 처럼! (뷰 검사도 X)
    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") String memberName,
                                 @RequestParam("age") int memberAge){
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam String username,
                                 @RequestParam int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username,int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /* @RequestParam의 required 옵션을 줘서 필수 값을 검증할 수 있다 */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(required = true) String username,
                                       @RequestParam(required = true) int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /* @RequestParam의 defaultValue 옵션을 줘서 기본 값 지정 가능 */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(@RequestParam(required = true, defaultValue = "guest") String username,
                                       @RequestParam(required = true, defaultValue = "-1") int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /* 파라미터를 Map으로 받기 */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap){
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData){
        log.info("helloData={}", helloData); // @Data에 ToString 기능도 있어서 바로 객체 출력 가능!
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData){
        log.info("helloData={}", helloData); // @Data에 ToString 기능도 있어서 바로 객체 출력 가능!
        return "ok";
    }
}
