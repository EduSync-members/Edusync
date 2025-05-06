package za.ac.tut.bl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.*;

@WebServlet("/UploadContentServlet.do")
@MultipartConfig
public class UploadContentServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://172.20.10.3:3306/edusync_db";
    private static final String DB_USER = "kgj";
    private static final String DB_PASSWORD = "kgotso";
    private static final String SAVE_DIR = "uploaded_files";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        Part filePart = request.getPart("file");

        // Get the currently logged-in tutor (user ID should be stored in session)
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("upload_content.jsp?message=User+not+logged+in");
            return;
        }

        // Get filename
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // Save file to disk
        String appPath = request.getServletContext().getRealPath("");
        String savePath = appPath + File.separator + SAVE_DIR;
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        String filePath = savePath + File.separator + fileName;

        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }

        // Save file metadata to database
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String sql = "INSERT INTO academic_content (userid, title, contenturl) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, filePath);  // optionally store relative path instead

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    response.sendRedirect("upload_content.jsp?message=Content+uploaded+successfully");
                } else {
                    response.sendRedirect("upload_content.jsp?message=Failed+to+upload+content");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("upload_content.jsp?message=An+error+occurred");
        }
    }
}
