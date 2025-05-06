<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Ratings</title>
    <style>
        body { margin:0; font-family:'Segoe UI',sans-serif; background:#f5f7fa; color:#333; }
        header { background:#38a169; color:white; padding:20px 40px; display:flex; justify-content:space-between; align-items:center; }
        header h1 { margin:0; font-size:1.8em; }
        .container { max-width:600px; margin:50px auto; padding:30px; background:white; border-radius:12px; box-shadow:0 8px 16px rgba(0,0,0,0.1); }
        h2 { text-align:center; color:#38a169; margin-bottom:30px; }
        .rating-box { padding:15px; background:#f9f9f9; border-radius:8px; margin-bottom:20px; border:1px solid #ddd; }
        .rating-box p { margin:5px 0; }
        .back-link { display:block; text-align:center; margin-top:20px; text-decoration:none; color:#38a169; font-weight:bold; }
        .back-link:hover { text-decoration:underline; }
    </style>
</head>
<body>

<header>
    <h1>Student Ratings</h1>
</header>

<div class="container">
    <h2>Your Reviews</h2>

    <div class="rating-box">
        <p><strong>Student:</strong> Jane Doe</p>
        <p><strong>Rating:</strong> ⭐⭐⭐⭐☆</p>
        <p>"Very helpful with Java concepts!"</p>
    </div>

    <div class="rating-box">
        <p><strong>Student:</strong> Mark Lee</p>
        <p><strong>Rating:</strong> ⭐⭐⭐⭐⭐</p>
        <p>"Explained recursion clearly. Highly recommend!"</p>
    </div>

    <a class="back-link" href="tutor_dashboard.jsp">← Back to Dashboard</a>
</div>

</body>
</html>
