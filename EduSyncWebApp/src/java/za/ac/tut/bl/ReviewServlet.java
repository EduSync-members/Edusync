package za.ac.tut.bl;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import za.ac.tut.entities.AppUser;

@WebServlet("/ReviewServlet.do")
public class ReviewServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://172.20.10.3:3306/edusync_db";
    private static final String DB_USER = "kgj";
    private static final String DB_PASSWORD = "kgotso";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        AppUser user = (AppUser) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String tutorName = request.getParameter("tutor_name");
        int rate = Integer.parseInt(request.getParameter("rate"));
        String comment = request.getParameter("review_comment");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "INSERT INTO review (userid, tutor_name, rate, review_comment, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getUserId());
            ps.setString(2, tutorName);
            ps.setInt(3, rate);
            ps.setString(4, comment);
            ps.setString(5, "Submitted");
            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("students_dashboard.jsp");
    }
}
