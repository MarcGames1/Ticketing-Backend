import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
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

        var employee = new Employee("Hamza", "Hazin");
        Gson gson = new Gson();

        var responseString = gson.toJson(employee);

        PrintWriter out = response.getWriter();
        out.println(responseString);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        var employee = new Employee("John", "Doe");
        Gson gson = new Gson();

        var responseString = gson.toJson(employee);

        PrintWriter out = response.getWriter();
        out.println(responseString);
    }

    public void destroy() {

    }
}

record Employee (String firstName, String lastName){}
