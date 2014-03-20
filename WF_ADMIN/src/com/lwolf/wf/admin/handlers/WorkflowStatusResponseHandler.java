package com.lwolf.wf.admin.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

public class WorkflowStatusResponseHandler implements ResponseHandler<Status> {

	@Override
	public Status handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			return Status.fromStatusCode(status);
		} else {
			String reason = response.getStatusLine().getReasonPhrase();
			throw new ClientProtocolException("Unexpected response status: " + status, new ServletException(reason));
		}
	}

}
