package za.ac.tut.bl;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import za.ac.tut.entities.AppUser;

@WebServlet("/NotificationServlet.do")
public class NotificationServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://172.20.10.3:3306/edusync_db";
    private static final String DB_USER = "kgj";
    private static final String DB_PASSWORD = "kgotso";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        AppUser user = (AppUser) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<String> notifications = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Adjusted to use Long for user_id
            String sql = "SELECT notification_date, message FROM notification WHERE user_id = ? ORDER BY notification_date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getUserId());  // Use setLong to bind Long userId

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Date date = rs.getDate("notification_date");
                String message = rs.getString("message");
                notifications.add(date.toString() + " - " + message);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("notifications", notifications);
        RequestDispatcher dispatcher = request.getRequestDispatcher("notification.jsp");
        dispatcher.forward(request, response);
    }
}
