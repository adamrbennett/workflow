package com.lwolf.wf.api.rs.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.wf.api.auth.RequestAuthenticator;
import com.lwolf.wf.api.locators.TomeeResourceLocator;
import com.lwolf.wf.api.rs.InputResource;
import com.lwolf.wf.dto.InputFileDto;
import com.lwolf.wf.exceptions.ServiceException;
import com.lwolf.wf.locators.UniqueIdLocator;
import com.lwolf.wf.persistence.DataFactory;
import com.lwolf.wf.repository.InputRepository;
import com.lwolf.wf.repository.KeyRepository;
import com.lwolf.wf.repository.impl.InputRepositoryImpl;
import com.lwolf.wf.repository.impl.KeyRepositoryImpl;
import com.lwolf.wf.services.InputService;
import com.lwolf.wf.services.impl.InputServiceImpl;

@Path("/process/{processId}/task/{taskId}/input")
public class InputResourceImpl implements InputResource {
	
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String CONTENT_TYPE = HttpHeaders.CONTENT_TYPE;
	
	private final DataSourceLocator dataSourceLocator = TomeeResourceLocator.locator();
	private final IdLocator idLocator = UniqueIdLocator.load(new UniqueIdLocator(TomeeResourceLocator.locator()));
	private final DataFactory dataFactory = DataFactory.getDataFactory(DataFactory.FactoryType.JDBC, dataSourceLocator, idLocator);
	
	private final KeyRepository keyRepository = new KeyRepositoryImpl(dataFactory.keyDao());
	private final InputRepository inputRepository = new InputRepositoryImpl(dataFactory.inputDao(), dataFactory.inputFileDao());
	private final InputService service = new InputServiceImpl(inputRepository, TomeeResourceLocator.locator());
	
	private final RequestAuthenticator requestAuthenticator = new RequestAuthenticator(keyRepository);

	@Override
	public Response getFile(
		HttpServletRequest request,
		Integer processId,
		Integer taskId,
		Integer inputId,
		Integer fileId
	) {
		try {
			
			requestAuthenticator.authenticate(request);
			
			InputFileDto file = new InputFileDto(processId, taskId, inputId, fileId);
			
			service.get(file);
			
			ResponseBuilder builder = Response.ok(file.fileData);
			builder = builder.header(CONTENT_DISPOSITION, "attachment; filename=" + file.fileName);
			builder = builder.header(CONTENT_TYPE, file.fileType);
			
			return builder.build();
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}

}
