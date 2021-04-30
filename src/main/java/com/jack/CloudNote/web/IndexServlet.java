package com.jack.CloudNote.web;

import com.jack.CloudNote.po.Note;
import com.jack.CloudNote.po.User;
import com.jack.CloudNote.service.NoteService;
import com.jack.CloudNote.util.Page;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "IndexServlet", value = "/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("menu_page", "index");

        noteList(request, response, null, null, null);

        request.setAttribute("changePage", "note/list.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void noteList(HttpServletRequest request, HttpServletResponse response, String title, String date, String typeId) {
        // 1. 接收参数 （当前页、每页显示的数量）
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");

        // 2. 获取Session作用域中的user对象
        User user = (User) request.getSession().getAttribute("user");

        // 3. 调用Service层查询方法，返回Page对象
        Page<Note> page = new NoteService().findNoteListByPage(pageNum, pageSize, user.getUserId(), title, date, typeId);

        // 4. 将page对象设置到request作用域中
        request.setAttribute("page", page);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
