package hello.servlet.basic.request;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

// url : http://localhost:8080/request-param?username=hello&age=20
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class requestParamServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /* 전체 파라미터 조회 */
        req.getParameterNames().asIterator()
        .forEachRemaining(paramName -> System.out.println(paramName + " = " + req.getParameter(paramName)));

        /* 단일 파라미터 조회 */
        String username = req.getParameter("username");
        String age = req.getParameter("age");
        System.out.println("age = " + age);
        System.out.println("username = " + username);

        /* 하나의 파라미터 변수에 여러 값이 존재할 때
        *  --> getParameterValues()로 꺼내면 된다 */
        String[] usernames = req.getParameterValues("username");
        for (String s : usernames) {
            System.out.println("s = " + s);
        }
    }
}
