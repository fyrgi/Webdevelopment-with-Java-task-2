package servlets;
import models.DBConnector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;

@WebServlet(urlPatterns = "/add-courses")
public class AddCourse extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showHeader(req, resp);
        showForm(req, resp);
        if(!req.getParameter("name").isEmpty() && !req.getParameter("points").isEmpty()){
            try {
                DBConnector.getConnector().insertQuery("addNewCourse", req.getParameter("name"),req.getParameter("points"),req.getParameter("description"),"S","I","S");
                resp.sendRedirect(req.getContextPath() + "/all-courses");
            }catch (NumberFormatException e){
                System.out.println(e);
            }
        } else {
            //TODO write the missing field message.
            System.out.println("Missing name fields");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showHeader(req, resp);
        showForm(req, resp);
    }
    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String name = req.getParameter("name") == null ? "" : req.getParameter("name");
        String points = req.getParameter("points") == null ? "" : req.getParameter("points");
        String description = req.getParameter("description") == null ? "" : req.getParameter("description");

        out.println(
                "<br>"
                        + "<div class=\"newStudentForm studentCoursesForm\">"
                        + "<form style='margin:5px;' action=/add-courses method=POST>"
                        + "            <label for=name>Course Name:</label>"
                        + "            <input type=text id=name name=name value=" + name + "><br/><br/>"
                        + "            <label for=points>Points:</label>"
                        + "            <input type=number id=points name=points min=\"10\" value=" + points + "><br/><br/>"
                        + "            <label for=description>Description:</label>"
                        + "            <input type=text id=description name=description value=" + description + "><br/><br/>"
                        + "            <input class=\"submBttn\" type=submit value=Submit>"
                        + "            <input class=reset id=reset type=reset value=Cancel  onclick=location.href='/add-courses'></div>"
                        + "        </form>"
                        + "</div>"
                        + "</body></html>"
        );
    }
    private void showHeader(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<head><link rel=\"stylesheet\" href=\"styles.css\"><title>Add new course</title>"
                + "</head>"
                + "<body>"
                + "<div class=\"topnav center\"><a class=\"active\" href=\"/index.html\">Home</a>&nbsp<a href=\"/all-students\">Show Students</a>&nbsp<a href=\"/add-student\">Add Student</a>&nbsp<a href=\"/show-courses-for-student\" >Show Courses for student</a>&nbsp<a href=\"/all-courses\">Show Courses</a>&nbsp<a href=\"/add-courses\" class=\"current\">Add Course</a>&nbsp<a href=\"/student-registrations\">Registration list</a>&nbsp<a href=\"/register-student\">Register student</a>&nbsp</div>"
                + "<h2>Add new course</h2>");
        resp.setContentType("text/HTML");
    }
}
