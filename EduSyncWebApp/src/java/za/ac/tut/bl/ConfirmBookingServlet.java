package za.ac.tut.bl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/ConfirmBookingServlet.do")
public class ConfirmBookingServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://172.20.10.3:3306/edusync_db";
    private static final String DB_USER = "kgj";
    private static final String DB_PASSWORD = "kgotso";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get parameters from the request
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        String action = request.getParameter("action");  // Can be either "confirm" or "reject"
        
        // Database connection variables
        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare SQL update query to confirm or reject the booking
            String sql = "";
            if ("confirm".equals(action)) {
                sql = "UPDATE bookings SET status = 'Confirmed' WHERE booking_id = ?";
            } else if ("reject".equals(action)) {
                sql = "UPDATE bookings SET status = 'Rejected' WHERE booking_id = ?";
            }

            ps = con.prepareStatement(sql);
            ps.setInt(1, bookingId);

            // Execute update
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                response.sendRedirect("confirm_bookings.jsp?message=Booking+status+updated+successfully");
            } else {
                response.sendRedirect("confirm_bookings.jsp?message=Failed+to+update+booking+status");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("confirm_bookings.jsp?message=An+error+occurred");
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception ex) {}
            try { if (con != null) con.close(); } catch (Exception ex) {}
        }
    }
}
