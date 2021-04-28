package com.jack.CloudNote.web;

import com.jack.CloudNote.po.NoteType;
import com.jack.CloudNote.po.User;
import com.jack.CloudNote.service.NoteService;
import com.jack.CloudNote.service.NoteTypeService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "NoteServlet", value = "/note")
public class NoteServlet extends HttpServlet {

    NoteService noteService = new NoteService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("menu_page", "note");
        String actionName = request.getParameter("actionName");
        if ("view".equals(actionName)) {
            view(request, response);
        }
    }

    private void view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. 从Session对象中获取用户对象
        User user = (User) request.getSession().getAttribute("user");
        // 2. 通过用户ID查询对应的类型列表
        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());
        // 3. 将类型列表设置到request请求域中
        request.setAttribute("typeList", typeList);

        request.setAttribute("changePage", "note/view.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
