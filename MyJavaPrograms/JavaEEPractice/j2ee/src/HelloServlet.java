import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

public class HelloServlet extends HttpServlet {

    public void init(ServletConfig config) {
        System.out.println("init of Hello Servlet");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            String value = headerNames.nextElement();
            System.out.printf("%s\t%s%n", header, value);
        }

        try {
            response.setContentType("text/html; charset=UTF-8");
//            request.setCharacterEncoding("UTF-8");

            response.getWriter().println("<h1>第一次 使用 Servlet</h1>");
            response.getWriter().println(new Date().toLocaleString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
