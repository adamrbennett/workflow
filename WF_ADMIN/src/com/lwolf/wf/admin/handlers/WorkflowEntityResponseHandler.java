package com.lwolf.wf.admin.handlers;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

public class WorkflowEntityResponseHandler<E> implements ResponseHandler<E> {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private final Class<E> _elementClass;
	
	public WorkflowEntityResponseHandler(Class<E> elementClass) {
		_elementClass = elementClass;
	}

	@Override
	public E handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			} else {
				String responseText = EntityUtils.toString(entity);
				return mapper.readValue(
					responseText,
					TypeFactory.defaultInstance().constructType(_elementClass)
				);
			}
		} else {
			String reason = response.getStatusLine().getReasonPhrase();
			throw new ClientProtocolException("Unexpected response status: " + status, new ServletException(reason));
		}
	}

}
