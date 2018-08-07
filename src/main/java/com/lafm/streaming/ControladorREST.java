package com.lafm.streaming;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ControladorREST {
	
	@RequestMapping(value = "/videos", method = RequestMethod.GET)
	public ResponseJSON<List<String>> listVideos() {
		
		List<String> lista = new ArrayList<String>();
		
		lista.add("prueba");
		lista.add("prueba1");
		lista.add("prueba2");
		
		return new ResponseJSON<List<String>>(true, "Success", lista);
	}
	
	@RequestMapping(value = "/video", method = RequestMethod.GET)
    public StreamingResponseBody getVideo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
		String nameVideo = request.getParameter("name");

		response.setContentType("video/mp4");
        
		response.setHeader("Content-Disposition", "attachment; filename=\"" + nameVideo + ".mp4\"");
        
		InputStream inputStream = new FileInputStream(new File("C:\\" + nameVideo + ".mp4"));
        
		return outputStream -> {
            
			int nRead;
            byte[] data = new byte[1024];
            
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, nRead);
            }
            
            inputStream.close();
            
        };
        
    }

	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public ResponseJSON<String> upload(HttpServletRequest request) throws IOException {
		try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                // Inform user about invalid request
                ResponseJSON<String> responseObject = new ResponseJSON<String>(false, "Not a multipart request.", "");
                return responseObject;
            }

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(); 

            // Parse the request
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                InputStream stream = item.openStream();
                if (!item.isFormField()) {
                	
                    String filename = item.getName();
                    
                    // Process the input stream
                    OutputStream out = new FileOutputStream("C:\\videos\\" + filename);
                    IOUtils.copy(stream, out);
                    stream.close();
                    out.close();  
                    
                }
            }
        } catch (FileUploadException e) {
            return new ResponseJSON<String>(false, "File upload error", e.toString());
        } catch (IOException e) {
        	return new ResponseJSON<String>(false, "Internal server IO error", e.toString());
        }

		return new ResponseJSON<String>(true, "Success", "");
	 }
	
}
