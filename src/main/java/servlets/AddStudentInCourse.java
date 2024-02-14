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

@WebServlet(urlPatterns = "/register-student")
public class AddStudentInCourse extends HttpServlet {

    private String idStudent = "";
    private String studentData;
    private String tableHeaders = "<tr><th>ID</th><th>First name</th><th>Last name</th><th>City</th><th>Interests</th></tr>";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showHeader(req, resp);
        showForm(req, resp);
        PrintWriter out = resp.getWriter();
        // determine from which function is the Post made, so it could show the right information.
        String id = req.getParameter("id");
        String courseId = req.getParameter("idCourse");
        if(id != null && !id.isEmpty()){
            idStudent = req.getParameter("id");
            showDataDropDownCourses(req, resp, DBConnector.getConnector().selectQuery("showAvailableCourses",idStudent));
            // Get the data for the chosen student to present to the viewer.
            LinkedList<String[]> data = DBConnector.getConnector().selectQuery("showSelectedStudent", idStudent);
            if(data.size()<2){
                studentData = "No student with id " + idStudent + " can be found in the database.";
            } else {
                String[] dataset = data.get(1);
                studentData = "You are currently looking data for " + dataset[1] + " with id " + dataset[0];
            }
            out.println("<h4>"+studentData+"</h4>");
            tableHeaders = "<tr><th>ID</th><th>Student</th><th>Course</th><th>Points</th></tr>";
            showDataTable(req, resp, DBConnector.getConnector().selectQuery("showRegistrationsIdOnly", idStudent));
        } else if (courseId != null && !courseId.isEmpty() && !idStudent.isEmpty()){
            DBConnector.getConnector().insertQuery("registerStudentInCourse", idStudent, req.getParameter("idCourse"),"I","I");
            showDataDropDownCourses(req, resp, DBConnector.getConnector().selectQuery("showAvailableCourses",idStudent));
            tableHeaders = "<tr><th>ID</th><th>Student</th><th>Course</th><th>Points</th></tr>";
            showDataTable(req, resp, DBConnector.getConnector().selectQuery("showRegistrationsIdOnly", idStudent));
        } else {
            showDataTable(req, resp, DBConnector.getConnector().selectQuery("showStudents"));
            System.out.println("Missing field");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        idStudent = "";
        showHeader(req, resp);
        showForm(req, resp);
        tableHeaders = "<tr><th>ID</th><th>First name</th><th>Last name</th><th>City</th><th>Interests</th></tr>";
        showDataTable(req, resp, DBConnector.getConnector().selectQuery("showStudents"));
    }

    private void showDataDropDownCourses(HttpServletRequest req, HttpServletResponse resp, LinkedList<String[]> data) throws ServletException, IOException {
        String top = "<form action=/register-student method=POST><select name=idCourse>";
        String bottom = "</select><input class=\"reset\" type=submit value=Register></form><button class=resetNewLine id=reset onclick=location.href='/register-student'>Reset</button></div></body></html>";
        PrintWriter out = resp.getWriter();
        out.println(top);
        // The stream returns 1st row as the headers. In order to not show it is skipped.
        // if it is only one row as result then there is no data from the search and a message will be displayed
        if(data.size()>1){
            for(int i = 1; i<data.size(); i++){
                out.println("<option value=\""+data.get(i)[0]+"\">"+ data.get(i)[1]+"</option>");
            }
        } else {
            out.println("<option value=\"none\" id=\"-1\" disabled>No available courses</option>");
        }
        out.println(bottom);
    }

    private void showDataTable(HttpServletRequest req, HttpServletResponse resp, LinkedList<String[]> data) throws ServletException, IOException {

        String top = "<table class=\"view\">" + tableHeaders;
        String bottom = "</table>"
                + "</body>"
                + "</html>";

        resp.setContentType("text/HTML");
        PrintWriter out = resp.getWriter();
        out.println(top);
        // The stream returns 1st row as the headers. In order to not show it is skipped.
        // if it is only one row as result then there is no data from the search and a message will be displayed
        if(data.size()>1){

            for(int i = 1; i<data.size(); i++){
                out.println("<tr>");
                Arrays.stream(data.get(i)).forEach(dataPoint -> {
                    out.println("<td>" + dataPoint + "</td>");
                });
                out.println("</tr>");
            }
        } else {
            out.println("<tr><td colspan=\"4\"> There are no records in the database.</td></tr>");
        }
        out.println(bottom);
    }
    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String id = req.getParameter("id")==null?idStudent:req.getParameter("id");

        out.println(
                "<br>"
                        + "<div class=\"studentCoursesForm\">"
                        + "<form style='margin:5px;' action=/register-student method=POST>"
                        + "<label for=_id>Student id:</label>"
                        + "<input type=text id=_id name=id value="+id+">"
                        + "<input class=\"submBttn\" type=submit value=Submit>"
                        + "</form>"
                        + "</div>"
        );
    }
    private void showHeader(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<head><link rel=\"stylesheet\" href=\"styles.css\"><title>Register student in course</title>"
                + "</head>"
                + "<body>"
                + "<div class=\"topnav center\"><a class=\"active\" href=\"/index.html\">Home</a>&nbsp<a href=\"/all-students\">Show Students</a>&nbsp<a href=\"/add-student\">Add Student</a>&nbsp<a href=\"/show-courses-for-student\">Show Courses for student</a>&nbsp<a href=\"/all-courses\">Show Courses</a>&nbsp<a href=\"/add-courses\">Add Course</a>&nbsp<a href=\"/student-registrations\">Registration list</a>&nbsp<a href=\"/register-student\" class=\"current\">Register student</a>&nbsp</div>"
                + "<h2>Register student in course</h2>");
    }
}
