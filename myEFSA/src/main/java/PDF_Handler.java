import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@WebServlet("/PDF_Handler")
public class PDF_Handler extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                // Parse the request to get file items.
                List<FileItem> fileItems = upload.parseRequest(request);

                // Process the uploaded items
                for (FileItem fileItem : fileItems) {
                    if (!fileItem.isFormField()) {
                        // Process the PDF file
                        InputStream pdfStream = fileItem.getInputStream();
                        String pdfText = extractTextFromPDF(pdfStream);

                        // Send the PDF text back to the client
                        response.getWriter().write(pdfText);
                    }
                }
            } catch (FileUploadException e) {
                throw new ServletException("Cannot parse multipart request.", e);
            }
        }
    }

    private String extractTextFromPDF(InputStream pdfStream) {
    	try {
            // Load the PDF document
            PDDocument document = PDDocument.load(pdfStream);

            // Create a PDFTextStripper object
            PDFTextStripper pdfStripper = new PDFTextStripper();

            // Extract text from the PDF document
            String pdfText = pdfStripper.getText(document);

            // Close the PDF document
            document.close();
            
            return pdfText;
        } catch (IOException e) {
            // Handle the exception (e.g., log it or return an error message)
            e.printStackTrace();
            return "Error extracting text from PDF";
        }
    }
}

