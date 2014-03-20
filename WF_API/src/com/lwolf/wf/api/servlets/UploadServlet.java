package com.lwolf.wf.api.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import com.lwolf.scud.locators.IdLocator;
import com.lwolf.scud.locators.ResourceLocator;
import com.lwolf.wf.api.auth.RequestAuthenticator;
import com.lwolf.wf.api.locators.TomeeResourceLocator;
import com.lwolf.wf.api.utils.ApplicationProperties;
import com.lwolf.wf.locators.UniqueIdLocator;
import com.lwolf.wf.persistence.DataFactory;
import com.lwolf.wf.repository.KeyRepository;
import com.lwolf.wf.repository.impl.KeyRepositoryImpl;

/**
 * Servlet implementation class Test
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final ResourceLocator resourceLocator = TomeeResourceLocator.locator();
	private final IdLocator idLocator = UniqueIdLocator.load(new UniqueIdLocator(resourceLocator));
	private final DataFactory dataFactory = DataFactory.getDataFactory(DataFactory.FactoryType.JDBC, resourceLocator, idLocator);
	private final KeyRepository keyRepository = new KeyRepositoryImpl(dataFactory.keyDao());
	private final RequestAuthenticator authenticator = new RequestAuthenticator(keyRepository);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Multipart
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// authenticate
		authenticator.authenticate(request);
		
		// create a temp file
		File file = File.createTempFile(UUID.randomUUID().toString(), ".tmp", new File(ApplicationProperties.uploadTempDir()));
		file.deleteOnExit();
		
		// write file to temp file
		Part part = request.getPart("file");
		part.write(file.getAbsolutePath());
		
		String contentType = part.getContentType();
		
		// respond with file name
		response.setContentType(MediaType.TEXT_PLAIN);
		PrintWriter out = response.getWriter();
		out.print(file.getName());
		out.print(";");
		out.print(contentType);
		out.flush();
		out.close();
	}

}
