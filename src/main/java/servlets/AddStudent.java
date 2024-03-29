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

@WebServlet(urlPatterns = "/add-student")
public class AddStudent extends HttpServlet {
    private String errorMsg = "";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showHeader(req, resp);
        PrintWriter out = resp.getWriter();
        if(!errorMsg.isEmpty()){
            out.println("<p class=error>"+errorMsg+"</p>");
            errorMsg="";
        }
        showForm(req, resp);
        // Here we have to determine which insert query we will use
        if((!req.getParameter("fname").isEmpty() && req.getParameter("fname").length()>1) && (!req.getParameter("lname").isEmpty() && req.getParameter("lname").length()>1)){
            try {
                DBConnector.getConnector().insertQuery("addNewStudent", req.getParameter("fname"), req.getParameter("lname"), req.getParameter("city"), req.getParameter("interests"), "S","S","S","S");
                resp.sendRedirect(req.getContextPath() + "/all-students");
            }catch (NumberFormatException e){
                errorMsg = "No connection with the database";
            }
        } else {
            if(req.getParameter("fname").isEmpty() && req.getParameter("lname").isEmpty()){
                errorMsg = "First name and Last name must be filled!<br>";
            } else if (req.getParameter("fname").isEmpty() && !req.getParameter("lname").isEmpty()){
                errorMsg = "Missing first name!<br>";
            } else if (!req.getParameter("fname").isEmpty() && req.getParameter("lname").isEmpty()){
                errorMsg = "Missing last name!<br>";
            }

            if (req.getParameter("fname").length() < 2 && req.getParameter("lname").length() < 2) {
                errorMsg += "The provided names are too short. Minimum 2 symbols.<br>";
            }else if (req.getParameter("fname").length() < 2 && req.getParameter("lname").length() > 1) {
                errorMsg += "The first name is too short. Minimum 2 symbols.<br>";
            }else if (req.getParameter("fname").length() > 1 && req.getParameter("lname").length() < 2) {
                errorMsg += "The last name is too short. Minimum 2 symbols.<br>";
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showHeader(req, resp);
        showForm(req, resp);

    }
    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/HTML");
        PrintWriter out = resp.getWriter();
        String fName = req.getParameter("fname")==null?"":req.getParameter("fname");
        String lName = req.getParameter("lname")==null?"":req.getParameter("lname");
        String city = req.getParameter("city")==null?"":req.getParameter("city");
        String interests = req.getParameter("interests")==null?"":req.getParameter("interests");

        out.println(
                "<br>"
                        + "<div class=\"newStudentForm studentCoursesForm\">"
                        + "<form style='margin:5px;' action=/add-student method=POST>"
                        + "            <label for=fname>First Name:</label>"
                        + "            <input type=text id=fname name=fname value="+fName+"><br/><br/>"
                        + "            <label for=fname>Last Name:</label>"
                        + "            <input type=text id=lname name=lname value="+lName+"><br/><br/>"
                        + "            <label for=city>City:</label>"
                        + "            <input type=text id=city name=city value="+city+"><br/><br/>"
                        + "            <label for=interests>Interests:</label>"
                        + "            <input type=text id=interests name=interests value="+interests+"><br/><br/>"
                        + "            <input class=\"submBttn\" type=submit value=Submit>"
                        +"             <input class=reset id=reset type=reset value=Cancel  onclick=location.href='/add-student'></div>"
                        + "        </form>"
                        + "</div>"
                        +"</body></html>"
        );
    }
    private void showHeader(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<head><link rel=\"stylesheet\" href=\"styles.css\"><title>Add student</title>"
                + "</head>"
                + "<body>"
                + "<div class=\"topnav center\"><a class=\"active\" href=\"/index.html\">Home</a>&nbsp<a href=\"/all-students\">Show Students</a>&nbsp<a href=\"/add-student\" class=\"current\">Add Student</a>&nbsp<a href=\"/show-courses-for-student\" >Show Courses for student</a>&nbsp<a href=\"/all-courses\">Show Courses</a>&nbsp<a href=\"/add-courses\">Add Course</a>&nbsp<a href=\"/student-registrations\">Registration list</a>&nbsp<a href=\"/register-student\">Register student</a>&nbsp</div>"
                + "<h2>Add new student</h2>");
    }
}
