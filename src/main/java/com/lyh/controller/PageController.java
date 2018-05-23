package com.lyh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class PageController {
    @RequestMapping("/page/hh")
    public void pageTo(HttpServletRequest request, HttpServletResponse response,String to)throws IOException,ServletException{
        request.getRequestDispatcher("/templates/hh.html").forward(request,response);
    }
}
