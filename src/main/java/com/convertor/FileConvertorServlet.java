package com.convertor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@WebServlet("/FileConvertorServlet")
public class FileConvertorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FileConvertorServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><body><h3>Invalid access. Please upload a file for conversion first.</h3></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filePath = (String) request.getAttribute("filePath");
        String format = (String) request.getAttribute("format");

        File file = new File(filePath);
        String convertedFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + "_converted." + format;

        try {
            if ("docx".equalsIgnoreCase(format)) {
                convertToDocx(file, convertedFilePath);
            } else if ("pdf".equalsIgnoreCase(format)) {
                convertToPdf(file, convertedFilePath);
            } else {
                response.getWriter().println("<html><body><h3>Unsupported file format for conversion.</h3></body></html>");
                return;
            }

            request.setAttribute("downloadPath", "uploads/" + new File(convertedFilePath).getName());
            request.getRequestDispatcher("result.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<html><body><h3>File conversion failed. Please try again.</h3></body></html>");
        }
    }

    private void convertToDocx(File file, String convertedFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(convertedFilePath)) {

            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();

            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = fis.read()) != -1) {
                sb.append((char) ch);
            }
            run.setText(sb.toString());
            document.write(fos);
        }
    }

    private void convertToPdf(File file, String convertedFilePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             PdfWriter writer = new PdfWriter(convertedFilePath);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = fis.read()) != -1) {
                sb.append((char) ch);
            }
            document.add(new Paragraph(sb.toString()));
        }
    }
}
