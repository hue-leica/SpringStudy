package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {
    /** (/** + Enter 하면 주석이 생성됨)
     *
     * @param paramMap
     * @param model
     * @return
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
