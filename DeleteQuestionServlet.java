package za.ac.tut.bl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import za.ac.tut.db.DBConnection;

@WebServlet("/DeleteQuestionServlet.do")
public class DeleteQuestionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userid") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Long userId = (Long) session.getAttribute("userid");
        String questionIdStr = request.getParameter("questionid");

        if (questionIdStr == null || questionIdStr.isEmpty()) {
            response.sendRedirect("view_responses.jsp?error=Missing+question+ID");
            return;
        }

        try {
            long questionId = Long.parseLong(questionIdStr);

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM question WHERE questionid = ? AND userid = ?")) {

                ps.setLong(1, questionId);
                ps.setLong(2, userId);
                int rowsDeleted = ps.executeUpdate();

                if (rowsDeleted > 0) {
                    response.sendRedirect("responses.jsp?message=Question+deleted+successfully");
                } else {
                    response.sendRedirect("responses.jsp?error=Failed+to+delete+question");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view_responses.jsp?error=Error+while+deleting+question");
        }
    }
}
