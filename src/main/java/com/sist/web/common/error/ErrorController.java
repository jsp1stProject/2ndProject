package com.sist.web.common.error;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {
    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView mv = new ModelAndView("main/main");
        mv.addObject("main_jsp", "../main/error.jsp");
        mv.addObject("status", status);
        return mv;
    }
}
