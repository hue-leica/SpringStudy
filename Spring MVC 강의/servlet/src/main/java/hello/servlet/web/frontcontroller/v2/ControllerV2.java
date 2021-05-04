package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ControllerV2 {

    /* v1과 유사하지만 return Type을 MyView로 해줘야 함 */
    MyView process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
