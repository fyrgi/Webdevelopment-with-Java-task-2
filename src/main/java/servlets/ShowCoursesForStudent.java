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

@WebServlet(urlPatterns = "/show-courses-for-student")
public class ShowCoursesForStudent extends HttpServlet {
    // we use 2 sets of result table headers. The main one that comes from students, and it is the default one
    // and another one for the result which shows the courses.
    private String tableHeaders = "<tr><th>ID</th><th>First name</th><th>Last name</th><th>City</th><th>Interests</th></tr>";
    private String errorMsg = "";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showHeader(req, resp);
        PrintWriter out = resp.getWriter();

        showForm(req, resp);
        String id = req.getParameter("id");
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        if(!id.isEmpty() && (!fname.isEmpty() && !lname.isEmpty())){
            tableHeaders = "<tr><th>ID</th><th>Student</th><th>Course</th><th>Points</th></tr>";
            showDataTable(req, resp, DBConnector.getConnector().selectQuery("showRegistrationsWithId", req.getParameter("fname"), req.getParameter("lname"), req.getParameter("id")));
        } else if(!id.isEmpty() && fname.isEmpty() && lname.isEmpty()){
            tableHeaders = "<tr><th>ID</th><th>Student</th><th>Course</th><th>Points</th></tr>";
            showDataTable(req, resp, DBConnector.getConnector().selectQuery("showRegistrationsIdOnly", req.getParameter("id")));
        } else if(id.isEmpty() && (!fname.isEmpty() && !lname.isEmpty())){
            tableHeaders = "<tr><th>ID</th><th>Student</th><th>Course</th><th>Points</th></tr>";
            showDataTable(req, resp, DBConnector.getConnector().selectQuery("showRegistrationsName", req.getParameter("fname"), req.getParameter("lname")));
        } else {
            if((fname.isEmpty() && !lname.isEmpty()) || (!fname.isEmpty() && lname.isEmpty())){
                errorMsg = "Both names should be filled";
                if(!errorMsg.isEmpty()){
                    out.println("<p class=error>"+errorMsg+"</p>");
                    errorMsg="";
                }
                tableHeaders = "<tr><th>ID</th><th>First name</th><th>Last name</th><th>City</th><th>Interests</th></tr>";
                showDataTable(req, resp, DBConnector.getConnector().selectQuery("showStudents"));
            } else if(fname.isEmpty() && lname.isEmpty() && id.isEmpty()) {
                tableHeaders = "<tr><th>ID</th><th>First name</th><th>Last name</th><th>City</th><th>Interests</th></tr>";
                showDataTable(req, resp, DBConnector.getConnector().selectQuery("showStudents"));
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showHeader(req, resp);
        showForm(req, resp);
        tableHeaders = "<tr><th>ID</th><th>First name</th><th>Last name</th><th>City</th><th>Interests</th></tr>";
        showDataTable(req, resp, DBConnector.getConnector().selectQuery("showStudents"));
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
            out.println("<tr><td colspan=\"4\"> The student is not signed up for any courses or does not exist.</td></tr>");
        }
        out.println(bottom);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String fName = req.getParameter("fname")==null?"":req.getParameter("fname");
        String lName = req.getParameter("lname")==null?"":req.getParameter("lname");
        String id = req.getParameter("id")==null?"":req.getParameter("id");

        out.println(
                "<br>"
                + "<div class=\"studentCoursesForm\">"
                + "<form style='margin:5px;' action=/show-courses-for-student method=POST>"
                + "            <label for=fname>First Name:</label>"
                + "            <input type=text id=fname name=fname value="+fName+" >"
                + "            <label for=fname>Last Name:</label>"
                + "            <input type=text id=lname name=lname value="+lName+" >"
                + "            <label for=_id>Student id:</label>"
                + "            <input type=text id=_id name=id value="+id+"><br><br>"
                + "            <input class=\"submBttn\" id=\"centeredBttn\" type=submit value=Search></a>"
                + "        </form>"
                + "</div>"
                + "<br>"
                +"<button class=resetNewLine id=reset onclick=location.href='/show-courses-for-student'>Reset</button></div>"
        );
    }
    private void showHeader(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<head><link rel=\"stylesheet\" href=\"styles.css\"><title>Courses for student</title>"
                + "</head>"
                + "<body>"
                + "<div class=\"topnav center\"><a class=\"active\" href=\"/index.html\">Home</a>&nbsp<a href=\"/all-students\">Show Students</a>&nbsp<a href=\"/add-student\">Add Student</a>&nbsp<a href=\"/show-courses-for-student\" class=\"current\">Show Courses for student</a>&nbsp<a href=\"/all-courses\">Show Courses</a>&nbsp<a href=\"/add-courses\">Add Course</a>&nbsp<a href=\"/student-registrations\">Registration list</a>&nbsp<a href=\"/register-student\">Register student</a>&nbsp</div>"
                + "<h2>Show classes for a specific person</h2>");
    }
}
