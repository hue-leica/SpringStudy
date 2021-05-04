package hello.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 1) @WebServlet 어노테이션으로 Servlet 이름지정 / 연결 url 지정
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet { // 2) HttpServlet을 상속

    // 3) protected 접근지정자를 갖는 service를 Override
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // 4) 비즈니스 로직 작성
    }
}
