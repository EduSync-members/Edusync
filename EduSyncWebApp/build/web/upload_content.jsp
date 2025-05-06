<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Upload Content</title>
    <style>
        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background: #f5f7fa;
            color: #333;
        }

        header {
            background: #38a169;
            color: white;
            padding: 20px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        header h1 {
            margin: 0;
            font-size: 1.8em;
        }

        .container {
            max-width: 600px;
            margin: 50px auto;
            padding: 30px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0,0,0,0.1);
        }

        h2 {
            text-align: center;
            color: #38a169;
            margin-bottom: 30px;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: bold;
            margin-bottom: 8px;
            margin-top: 20px;
        }

        input[type="file"], input[type="text"], textarea {
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 1em;
        }

        button {
            margin-top: 30px;
            padding: 12px;
            background-color: #38a169;
            color: white;
            font-size: 1em;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }

        button:hover {
            background-color: #2f855a;
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            text-decoration: none;
            color: #38a169;
            font-weight: bold;
        }

        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<header>
    <h1>Upload Content</h1>
</header>
<div class="container">
    <%
    String message = request.getParameter("message");
        if (message != null) {
    %>
        <script>alert("<%= message.replace("+", " ") %>");</script>
    <%
        }
    %>

    <h2>Upload a File</h2>
    <form action="UploadContentServlet.do" method="post" enctype="multipart/form-data">
        <label for="title">Content Title</label>
        <input type="text" name="title" id="title" required>

        <label for="description">Description</label>
        <textarea name="description" id="description" rows="4"></textarea>

        <label for="file">Select File</label>
        <input type="file" name="file" id="file" required>

        <button type="submit">Upload</button>
    </form>
    <a class="back-link" href="dashboard.jsp">← Back to Dashboard</a>
</div>
</body>
</html>
