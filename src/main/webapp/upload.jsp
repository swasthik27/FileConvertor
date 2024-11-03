<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>File Upload</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
            color: #333;
        }
        .container {
            max-width: 600px;
            margin: auto;
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
            color: #4CAF50;
        }
        label {
            display: block;
            margin: 10px 0 5px;
        }
        input[type="file"],
        select,
        button {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #45a049;
        }
        .progress-message {
            text-align: center;
            color: #007bff;
            display: none;
            font-weight: bold;
            margin-top: 10px;
        }
        @media (max-width: 600px) {
            .container {
                padding: 10px;
            }
        }
    </style>
    <script>
        function showProgressMessage() {
            document.getElementById("progressMessage").style.display = "block";
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Upload a File to Convert</h2>
        <form action="FileUploadServlet" method="post" enctype="multipart/form-data" autocomplete="off" onsubmit="showProgressMessage()">
            <label for="file">Choose file:</label>
            <input type="file" name="file" id="file" accept=".txt, .pdf, .docx" required>

            <label for="format">Convert to format:</label>
            <select name="format" id="format" required>
                <option value="txt">Text</option>
                <option value="pdf">PDF</option>
                <option value="docx">Word</option>
            </select>

            <button type="submit">Upload and Convert</button>
        </form>
        <p id="progressMessage" class="progress-message">Uploading and converting, please wait...</p>
    </div>
</body>
</html>
