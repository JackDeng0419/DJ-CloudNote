package com.jack.CloudNote.web;

import com.jack.CloudNote.service.NoteService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

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
