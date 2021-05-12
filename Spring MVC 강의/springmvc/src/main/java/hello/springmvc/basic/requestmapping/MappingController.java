package hello.springmvc.basic.requestmapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController// 오류가 발생해도 JSON형식으로 맞춰서 오류를 출력해줌
@Slf4j
public class MappingController {

    @GetMapping("/hello-basic")
    public String helloBasic(){
        log.info("helloBasic");
        return "ok";
    }

    /* 경로 변수 꺼내 쓰기 */
    @GetMapping("/mapping/{userId}")
    // @PathVariable String userId와 동일
    public String mappingPath(@PathVariable String userId) {
        log.info("mappingPath userId={}", userId);
        return "ok";
    }
}
