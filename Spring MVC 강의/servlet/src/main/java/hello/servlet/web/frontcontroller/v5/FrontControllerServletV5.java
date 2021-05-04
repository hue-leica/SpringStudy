package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5(){
        initHandlerMappingMap();
        initHanlderAdapters();
    }

    private void initHanlderAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter()); // V3 추가
        handlerAdapters.add(new ControllerV4HandlerAdapter()); // V4 추가
    }

    private void initHandlerMappingMap() {
        // V3 추가
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        // V4 추가
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* 1) 핸들러 매핑 정보 찾기 - 해당 url을 처리하는 핸들러 매핑 정보가 있는지 */
        Object handler = getHandler(req); // 요청정보로 handler 가져오기

        if(handler == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        /* 2) 핸들러를 처리할 수 있는 어댑터 조회 */
        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        /* 3,4,5) 핸들러 어댑터를 통해서 실제 처리할 핸들러 호출 + mv 반환 */
        ModelView mv = adapter.handle(req, resp, handler);

        /* 6) view Resolver로 물리 경로 생성 */
        MyView view = viewResolver(mv.getViewName());
        view.render(mv.getModel(), req, resp);
    }

    // 현재 어댑터 목록 중 알맞은 핸들러를 찾는 메서드
    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if(adapter.supports(handler)){
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다." + handler);
    }

    // 논리 이름 --> 물리 주소로 변경해주는 View Resolver
    private MyView viewResolver(String viewName) throws ServletException, IOException {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    // 해당하는 핸들러 매핑 정보가 있는지 검사
    private Object getHandler(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        Object handler = handlerMappingMap.get(requestURI);
        return handler;
    }
}
