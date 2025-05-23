package za.ac.tut.bl;

import java.io.IOException;
import java.sql.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import za.ac.tut.entities.AppUser;

@WebServlet("/BookConsultationServlet.do")
public class BookConsultationServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://172.20.10.3:3306/edusync_db";
    private static final String DB_USER = "kgj";
    private static final String DB_PASSWORD = "kgotso";

    private static final String EMAIL_FROM = "your_email@gmail.com";
    private static final String EMAIL_PASSWORD = "your_app_password";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String date = request.getParameter("booking_date");
        String timeSlot = request.getParameter("time_slot");
        String reason = request.getParameter("reason");

        HttpSession session = request.getSession();
        AppUser user = (AppUser) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String userEmail = user.getEmail();
        String fullName = user.getFirstName() + " " + user.getLastName();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Updated SQL: remove student_id and use student_email and student_name
            String sql = "INSERT INTO tutorial_booking (student_email, student_name, booking_date, time_slot, reason) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userEmail);
            ps.setString(2, fullName);
            ps.setString(3, date);
            ps.setString(4, timeSlot);
            ps.setString(5, reason);

            int result = ps.executeUpdate();

            if (result > 0) {
                sendConfirmationEmail(userEmail, fullName, date, timeSlot, reason);
                request.setAttribute("status", "success");
                request.setAttribute("emailMessage", "A confirmation email has been sent to " + userEmail);
            } else {
                request.setAttribute("status", "error");
            }

            ps.close();
            conn.close();

            request.getRequestDispatcher("book_consultation.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", "error");
            request.getRequestDispatcher("book_consultation.jsp").forward(request, response);
        }
    }

    private void sendConfirmationEmail(String recipient, String name, String date, String timeSlot, String reason) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });

        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("EduSync: Consultation Booking Confirmation");

        String content = "Dear " + name + ",\n\n"
                + "Your consultation has been successfully booked with the following details:\n\n"
                + "📅 Date: " + date + "\n"
                + "⏰ Time Slot: " + timeSlot + "\n"
                + "📌 Reason: " + reason + "\n\n"
                + "Please be on time and reach out if you need to reschedule.\n\n"
                + "Best regards,\nEduSync Team";

        message.setText(content);
        Transport.send(message);
    }
}
