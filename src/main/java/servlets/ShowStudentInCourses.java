package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

@WebServlet(urlPatterns = "/student-registrations")
public class ShowStudentInCourses extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        // Mysql connector.getConnector().connect();

        String row = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:13306/GritAcademy","user", "user");
            Statement stmt = con.createStatement();
            String sql = "";
            String sort = "";
            try{
                sort = request.getParameter("sort");
                //if I remove the next line I get an error and my table is not shown.
                System.out.println(sort.getClass());
            } catch (Exception e){
                sort = "";
            }
            if(sort.equals("student")){
                sql = sql + "SELECT s.id, CONCAT(s.f_name,' ', s.l_name) AS student_name, c.name, c.points FROM student_course AS sc LEFT JOIN students AS s ON sc.id_student = s.id LEFT JOIN courses AS c ON sc.id_course = c.id ORDER BY student_name ASC, s.id ASC";
            } else if (sort.equals("course")){
                sql = sql + "SELECT s.id, CONCAT(s.f_name,' ', s.l_name) AS student_name, c.name, c.points FROM student_course AS sc LEFT JOIN students AS s ON sc.id_student = s.id LEFT JOIN courses AS c ON sc.id_course = c.id ORDER BY c.name ASC, student_name ASC";
            } else {
                sql = sql + "SELECT s.id, CONCAT(s.f_name,' ', s.l_name) AS student_name, c.name, c.points FROM student_course AS sc LEFT JOIN students AS s ON sc.id_student = s.id LEFT JOIN courses AS c ON sc.id_course = c.id ORDER BY student_name ASC, s.id ASC";
            }
            ResultSet rs = stmt.executeQuery(sql);
            row = row + "<table class=\"view\"><tr><th>Student id</th><th><a class=\"sort\" href=\"student-registrations?sort=student\">Student name &#9660</a></th><th><a class=\"sort\" href=\"student-registrations?sort=course\">Course &#9660</a></th><th>Points</th></tr>";
            while (rs.next()){
                row = row +"<tr><td>"+rs.getInt(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td></tr>";
            }
            row = row+"</table>";
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        PrintWriter out = response.getWriter();
        String html ="<html><head><link rel=\"stylesheet\" href=\"styles.css\"><title>All registered students</title></head><body>"
                + "<div class=\"topnav center\"><a class=\"active\" href=\"/index.html\">Home</a>&nbsp<a href=\"/all-students\">Show Students</a>&nbsp<a href=\"/add-student\">Add Student</a>&nbsp<a href=\"/show-courses-for-student\" >Show Courses for student</a>&nbsp<a href=\"/all-courses\">Show Courses</a>&nbsp<a href=\"/add-courses\">Add Course</a>&nbsp<a href=\"/student-registrations\" class=\"current\">Registration list</a>&nbsp<a href=\"/register-student\">Register student</a>&nbsp</div>"
                + "<h2>Registration list</h2>"+row+"</body></html>";
        out.println(html);
    }

}