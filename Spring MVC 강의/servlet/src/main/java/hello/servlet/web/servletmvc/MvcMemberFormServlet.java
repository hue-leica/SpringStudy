package hello.servlet.web.servletmvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* /WEB-INF 경로에 있으면 외부에서 직접 JSP를 호출할 수 없게 할 수 있음
           --> 컨트롤러를 통해 반드시 호출되어야 함  */
        String viewPath = "/WEB-INF/views/new-form.jsp";

        /* dispatcher --> 다른 Servlet이나 JSP로 이동하는 기능 */
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, resp);
    }
}
