package za.ac.tut.bl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import za.ac.tut.db.DBConnection;

@WebServlet("/AnswerUpdateServlet.do")
public class AnswerUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int questionId = Integer.parseInt(request.getParameter("questionid"));
        String answer = request.getParameter("answer");

        HttpSession session = request.getSession();
        Integer tutorId = (Integer) session.getAttribute("userid"); 
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "INSERT INTO answer (questionid, answer_text, tutorid, answer_date) VALUES (?, ?, ?, NOW())";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, questionId);
            ps.setString(2, answer);
            ps.setInt(3, tutorId);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                response.sendRedirect("tutor_dashboard.jsp?message=Answer+submitted+successfully");
            } else {
                response.sendRedirect("answer_question.jsp?error=Could+not+submit+answer");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("answer_question.jsp?error=Server+error");
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }
}
