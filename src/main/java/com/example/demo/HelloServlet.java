import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "helloServlet", value = "/hello")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Construiți răspunsul JSON
        String jsonResponse = "{\"message\": \"" + message + "\"}";

        // Trimiteți răspunsul JSON către client
        PrintWriter out = response.getWriter();
        out.println(jsonResponse);
    }

    public void destroy() {

    }
}
