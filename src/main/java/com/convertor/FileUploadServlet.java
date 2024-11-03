package com.convertor;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/FileUploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
                 maxFileSize = 1024 * 1024 * 10,      // 10 MB
                 maxRequestSize = 1024 * 1024 * 15)   // 15 MB
public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FileUploadServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect to upload.jsp to display the upload form
        request.getRequestDispatcher("upload.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        Part filePart = request.getPart("file");
        String format = request.getParameter("format");

        if (filePart == null || filePart.getSize() == 0 || format == null || format.isEmpty()) {
            response.getWriter().println("<html><body><h3>Please provide both a valid file and a format to convert.</h3></body></html>");
            return;
        }

        String fileName = getFileName(filePart);
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);

        // Ensure upload directory exists
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            response.getWriter().println("<html><body><h3>Failed to create upload directory. Please try again later.</h3></body></html>");
            return;
        }

        // Save the uploaded file
        File file = new File(uploadPath + File.separator + fileName);
        try {
            filePart.write(file.getAbsolutePath());
        } catch (IOException e) {
            response.getWriter().println("<html><body><h3>File upload failed. Please try again.</h3></body></html>");
            return;
        }

        // Set file path and format as attributes to pass to the converter servlet
        request.setAttribute("filePath", file.getAbsolutePath());
        request.setAttribute("format", format);

        // Forward to FileConvertorServlet for file conversion
        request.getRequestDispatcher("/FileConvertorServlet").forward(request, response);
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String cd : contentDisposition.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "").replaceAll("[^a-zA-Z0-9.\\-_]", "_");
            }
        }
        return "uploaded_file";
    }
}
