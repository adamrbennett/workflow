package com.lwolf.wf.admin.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.lwolf.wf.admin.http.WorkflowRequest;
import com.lwolf.wf.admin.utils.ApplicationProperties;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Multipart
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			// get the incoming file
			Part part = request.getPart("file");
			ContentType contentType = ContentType.create(part.getContentType());
			
			// build the entity
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			entityBuilder.addBinaryBody("file", part.getInputStream(), contentType, "");
			
			// execute request
			HttpResponse httpResponse = WorkflowRequest.Post(ApplicationProperties.workflowApiUpload(), entityBuilder.build()).execute().returnResponse();
			
			// return the response to the client
			IOUtils.copy(httpResponse.getEntity().getContent(), response.getWriter());
			
		} catch (ParseException ex) {
			throw new ServletException(ex);
		}
	}

}
