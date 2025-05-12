package za.ac.tut.bl;

import za.ac.tut.db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/DownloadCSVServlet.do")
public class DownloadCSVServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"responses.csv\"");

        try (
            Connection conn = DBConnection.getConnection();
            PrintWriter writer = response.getWriter()
        ) {
            String sql = "SELECT q.questionid, q.question_text, q.userid AS studentid, " +
                     "a.answer_text, a.tutorid, a.answer_date " +
                     "FROM question q " +
                     "LEFT JOIN answer a ON q.questionid = a.questionid " +
                     "ORDER BY q.questionid DESC";

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                // Write CSV headers
                writer.println("Question,Student ID,Answer,Tutor ID,Answer Date");

                // Write data rows
                while (rs.next()) {
                    String question = rs.getString("question_text").replaceAll(",", " ");
                    int studentId = rs.getInt("student_id");
                    String answer = rs.getString("answer_text").replaceAll(",", " ");
                    int tutorId = rs.getInt("tutorid");
                    Timestamp date = rs.getTimestamp("answer_date");

                    writer.printf("\"%s\",%d,\"%s\",%d,%s%n",
                        question, studentId, answer, tutorId, date.toString());
                }
            }

        } catch (Exception e) {
            throw new ServletException("Error generating CSV file: " + e.getMessage(), e);
        }
    }
}
