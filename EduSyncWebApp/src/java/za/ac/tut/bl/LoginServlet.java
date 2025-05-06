package za.ac.tut.bl;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import za.ac.tut.entities.AppUser;

@WebServlet("/LoginServlet.do")
public class LoginServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://172.20.10.3:3306/edusync_db";
    private static final String DB_USER = "kgj";
    private static final String DB_PASSWORD = "kgotso";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Check if the email exists first
            String emailQuery = "SELECT * FROM app_user WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(emailQuery);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (storedPassword.equals(password)) {
                    AppUser user = new AppUser();
                    user.setUserId(rs.getLong("userid"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));

                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("email", email);
                    session.setAttribute("role", user.getRole());

                    if ("student".equalsIgnoreCase(user.getRole())) {
                        response.sendRedirect("students_dashboard.jsp");
                    } else if ("tutor".equalsIgnoreCase(user.getRole())) {
                        response.sendRedirect("tutor_dashboard.jsp");
                    } else {
                        response.sendRedirect("login.jsp?error=unknownrole");
                    }
                } else {
                    // Incorrect password
                    response.sendRedirect("login.jsp?error=invalidpassword");
                }
            } else {
                // Email not found
                response.sendRedirect("login.jsp?error=invalidemail");
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            throw new ServletException("Database error during login", e);
        }
    }
}
