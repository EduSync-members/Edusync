package za.ac.tut.bl;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import za.ac.tut.db.DBConnection;

@WebServlet("/QuestionUpdateServlet.do")
public class QuestionUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String questionText = request.getParameter("question");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");

        if (firstName == null || lastName == null || questionText == null || questionText.trim().isEmpty()) {
            request.setAttribute("error", "Missing required information.");
            request.getRequestDispatcher("question_response.jsp").forward(request, response);
            return;
        }

        Long userId = null;
        Connection conn = null;
        PreparedStatement userStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            // Get user ID from app_user table
            String userQuery = "SELECT user_id FROM app_user WHERE first_name = ? AND last_name = ?";
            userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, firstName);
            userStmt.setString(2, lastName);

            rs = userStmt.executeQuery();
            if (rs.next()) {
                userId = rs.getLong("user_id");
            } else {
                request.setAttribute("error", "User not found.");
                request.getRequestDispatcher("question_response.jsp").forward(request, response);
                return;
            }

            // Insert the question using the found userId
            String insertSQL = "INSERT INTO question (userid, question_text) VALUES (?, ?)";
            insertStmt = conn.prepareStatement(insertSQL);
            insertStmt.setLong(1, userId);
            insertStmt.setString(2, questionText);
            insertStmt.executeUpdate();

            request.setAttribute("message", "Your question was submitted successfully!");
            request.getRequestDispatcher("question_response.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while submitting your question.");
            request.getRequestDispatcher("question_response.jsp").forward(request, response);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (userStmt != null) userStmt.close(); } catch (SQLException e) {}
            try { if (insertStmt != null) insertStmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }
}
