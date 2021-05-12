package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest req,
                          HttpServletResponse resp,
                          HttpMethod httpMethod,
                          Locale locale, // 언어 정보
                          @RequestHeader MultiValueMap<String, String> headerMap, // 헤더 모든 정보
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie){
        log.info("request={}", req);
        log.info("response={}", resp);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap); // 모든 헤더 정보
        log.info("header host={}", host); // 헤더의 특정 필드
        log.info("myCookie={}", cookie); // 쿠키 정보
        return "ok";
    }

}
