package za.ac.tut.bl;

import java.io.IOException;
import java.io.PrintWriter;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        try {
            // Simulate sending the password reset email (you should implement the actual logic here)
            boolean emailSent = sendPasswordResetEmail(email);

            if (emailSent) {
                // Redirect to the login page with a success message
                response.sendRedirect("login.jsp?message=Password reset email sent. Please check your inbox.");
            } else {
                // Redirect to the login page with an error message if email was not found
                response.sendRedirect("login.jsp?error=Email not found or invalid.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception and send an error message
            response.sendRedirect("login.jsp?error=An error occurred while processing your request. Please try again.");
        }
    }

    private boolean sendPasswordResetEmail(String email) {
        // Here, you can implement the email sending logic (SMTP, Mail API, etc.)
        // For now, this method returns true for simulating a successful email send.
        return true;
    }
}
