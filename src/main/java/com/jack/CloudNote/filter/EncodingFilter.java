package com.jack.CloudNote.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        // 转成HTTP请求
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 设置post的字符格式，但对get没有用
        request.setCharacterEncoding("UTF-8");

//        // 获得请求方法 post/get
//        String method = request.getMethod();
//        if("GET".equalsIgnoreCase(method)) {
//            //得到服务器版本
//            String serverInfo = request.getServletContext().getServerInfo(); //类如：Apache Tomcat/7.0.79
//            //获得所需的版本号数字
//            String version = serverInfo.substring(serverInfo.lastIndexOf("/")+1, serverInfo.indexOf("."));
//            //判断是否在tomcat7以下
//            if(version != null && Integer.parseInt(version) < 8) {
//                //是则需要单独处理get, 改造request（使用wrapper, request的包装类）
//
//                chain.doFilter();
//            }
//
//        }

        chain.doFilter(request, response);
    }


}
