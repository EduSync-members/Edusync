package za.ac.tut.bl;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/SignupServlet.do")
public class SignupServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://172.20.10.3:3306/edusync_db";
    private static final String DB_USER = "kgj";
    private static final String DB_PASSWORD = "kgotso";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String firstName = request.getParameter("name");
        String lastName = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        
        email = email.toLowerCase();
        role = role.toLowerCase();

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String checkSql = "SELECT email FROM app_user WHERE email = ?";
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                
                response.sendRedirect("signup.jsp?error=exists");
                return;
            }

            String insertSql = "INSERT INTO app_user (first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, ?)";
            insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, firstName);
            insertStmt.setString(2, lastName == null ? "" : lastName);
            insertStmt.setString(3, email);
            insertStmt.setString(4, password); 
            insertStmt.setString(5, role);

            int rows = insertStmt.executeUpdate();

            if (rows > 0) {
               
                response.sendRedirect("login.jsp?signup=success");
            } else {
               
                response.sendRedirect("signup.jsp?error=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("signup.jsp?error=exception");
        } finally {
            try { if (rs != null) rs.close(); } 
                catch (Exception e) {}
            try { if (checkStmt != null) checkStmt.close(); } 
                catch (Exception e) {}
            try { if (insertStmt != null) insertStmt.close(); }
                catch (Exception e) {}
            try { if (conn != null) conn.close(); }
                catch (Exception e) {}
        }
    }
}
