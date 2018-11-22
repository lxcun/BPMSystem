//package com.atw.bpmsystem.Common.filter;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//public class MyInterceptor implements HandlerInterceptor {
//
//    Logger logger = LoggerFactory.getLogger(MyInterceptor.class);
//
//@Override
//public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//        throws Exception {
//        Role role=((HandlerMethod) handler).getMethod().getAnnotation(Role.class);
//        else if(role!=request.getParameter("type")) {
//            System.out.println("permission denied");
//            response.setStatus(403);
//            return false;
//        }
//        else return true;
//}
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//                           ModelAndView modelAndView) throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//            throws Exception {
//        // TODO Auto-generated method stub
//
//    }
//
//}