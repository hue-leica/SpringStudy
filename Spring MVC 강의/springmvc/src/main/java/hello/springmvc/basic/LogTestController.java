package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController를 사용하면 뷰가 아닌 데이터를 직접 반환한다
@RestController
@Slf4j // 밑에 log를 가져오는 것을 알아서 모두 처리해주는 롬복 어노테이션
public class LogTestController {
    //private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/log-test")
    public String logTest(){
        String name = "Spring";
        // System.out.println("name = " + name);

        // 로그 레벨 낮은 레벨 --> 높은 레벨
        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info("info log={}", name);
        log.warn("warn log={}", name);
        log.error("error log={}", name);
        return "ok";
    }

}
