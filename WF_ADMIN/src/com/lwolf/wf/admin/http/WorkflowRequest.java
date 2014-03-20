package com.lwolf.wf.admin.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.lwolf.wf.admin.auth.impl.WorkflowPayload;
import com.lwolf.wf.admin.auth.impl.WorkflowRequestSigner;
import com.lwolf.wf.admin.exceptions.SigningException;
import com.lwolf.wf.admin.utils.ApplicationProperties;
import com.lwolf.wf.dto.DataTransferObject;

public class WorkflowRequest {
	
	private static final String CONTENT_MD5 = "Content-MD5";
	private static final String CONTENT_TYPE = HttpHeaders.CONTENT_TYPE;
	private static final String DATE = HttpHeaders.DATE;
	private static final String AUTHORIZATION = HttpHeaders.AUTHORIZATION;
	
	private static final String MD5 = "MD5";

	private static final WorkflowRequestSigner signer = new WorkflowRequestSigner();
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private final Request _request;
	
	private WorkflowRequest(Request request) {
		_request = request;
	}
	
	public static WorkflowRequest Get(String uri) {
		try {
			
			String path = getPath(uri);
			
			Request request = Request.Get(getUri(path));
			setHeaders(request, HttpMethod.GET, path);
			
			return new WorkflowRequest(request);
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (NoSuchAlgorithmException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (SigningException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (URISyntaxException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public static WorkflowRequest Post(String uri, DataTransferObject dto) {
		try {
			
			HttpEntity entity = null;
			if (dto != null) {
				entity = new ByteArrayEntity(mapper.writeValueAsBytes(dto), ContentType.APPLICATION_JSON);
			}
			
			String path = getPath(uri);
			
			Request request = Request.Post(getUri(path));
			setHeaders(request, entity, HttpMethod.POST, path);
			
			if (entity != null)
				request.body(entity);
			
			return new WorkflowRequest(request);
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (NoSuchAlgorithmException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (SigningException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (URISyntaxException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public static WorkflowRequest Post(String uri, HttpEntity entity) {
		try {
			
			String path = getPath(uri);
			
			Request request = Request.Post(getUri(path));
			setHeaders(request, entity, HttpMethod.POST, path);
			
			if (entity != null)
				request.body(entity);
			
			return new WorkflowRequest(request);
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (NoSuchAlgorithmException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (SigningException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (URISyntaxException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public static WorkflowRequest Put(String uri, DataTransferObject dto) {
		try {
			
			HttpEntity entity = null;
			if (dto != null) {
				entity = new ByteArrayEntity(mapper.writeValueAsBytes(dto), ContentType.APPLICATION_JSON);
			}
			
			String path = getPath(uri);
			
			Request request = Request.Put(getUri(path));
			setHeaders(request, entity, HttpMethod.PUT, path);
			
			if (entity != null)
				request.body(entity);
			
			return new WorkflowRequest(request);
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (NoSuchAlgorithmException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (SigningException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (URISyntaxException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public static WorkflowRequest Delete(String uri) {
		try {
			
			String path = getPath(uri);
			
			Request request = Request.Delete(getUri(path));
			setHeaders(request, HttpMethod.DELETE, path);
			
			return new WorkflowRequest(request);
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (NoSuchAlgorithmException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (SigningException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (URISyntaxException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	private static String getPath(String uri) throws IOException {
		String path = new StringBuilder(ApplicationProperties.workflowApiContextRoot()).append(uri).toString();
		path = path.replaceAll("//", "/");
		return path;
	}
	
	private static URI getUri(String path) throws IOException, URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(ApplicationProperties.workflowApiScheme());
		uriBuilder.setHost(ApplicationProperties.workflowApiHost());
		uriBuilder.setPort(ApplicationProperties.workflowApiPort());
		uriBuilder.setPath(path);
		return uriBuilder.build();
	}
	
	private static void setHeaders(Request request, String method, String path) throws IllegalStateException, IOException, NoSuchAlgorithmException, SigningException {
		setHeaders(request, null, method, path);
	}
	
	private static void setHeaders(Request request, HttpEntity entity, String method, String path) throws IllegalStateException, IOException, NoSuchAlgorithmException, SigningException {
		String contentType = MediaType.TEXT_PLAIN;
		byte[] contentBytes = new byte[] {};
		
		if (entity != null) {
			Header header = entity.getContentType();
			if (header != null)
				contentType = header.getValue();

			try {
				contentBytes = EntityUtils.toByteArray(entity);
			} catch (UnsupportedOperationException ex) {}
		}
		
		String contentMD5 = md5(contentBytes);
		String date = new Date().toString();
		
		WorkflowPayload payload = new WorkflowPayload();
		payload.setMethod(method);
		payload.setUri(path);
		payload.setProtocol("HTTP/1.1");
		payload.setContentMD5(contentMD5);
		payload.setContentType(contentType);
		payload.setDate(date);
		
		StringBuilder authorization = new StringBuilder();
		authorization.append("WF ").append(ApplicationProperties.workflowApiKey());
		authorization.append(":").append(signer.sign(payload));
		
		request.addHeader(CONTENT_MD5, contentMD5);
		request.addHeader(CONTENT_TYPE, contentType);
		request.addHeader(DATE, date);
		request.addHeader(AUTHORIZATION, authorization.toString());
	}
	
	private static String md5(byte[] value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance(MD5);
		return Base64.encodeBase64String(digest.digest(value));
	}
	
	public Response execute() {
		try {
			return _request.execute();
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public <T> T execute(ResponseHandler<T> handler) {
		try {
			return _request.execute().handleResponse(handler);
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}

}
