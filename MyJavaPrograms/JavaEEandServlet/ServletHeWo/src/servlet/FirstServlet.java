package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FirstServlet")
public class FirstServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String developer = request.getParameter("developer");
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("欢迎开发者" + developer);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //在给浏览器的响应 response 中写入一句 html
            response.getWriter().println("<h1>Hello Servlet!</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
