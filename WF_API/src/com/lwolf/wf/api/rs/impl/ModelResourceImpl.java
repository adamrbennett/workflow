package com.lwolf.wf.api.rs.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.lwolf.scud.locators.DataSourceLocator;
import com.lwolf.scud.locators.IdLocator;
import com.lwolf.wf.api.auth.RequestAuthenticator;
import com.lwolf.wf.api.locators.TomeeResourceLocator;
import com.lwolf.wf.api.rs.ModelResource;
import com.lwolf.wf.api.utils.ApplicationProperties;
import com.lwolf.wf.dto.ModelDto;
import com.lwolf.wf.exceptions.ServiceException;
import com.lwolf.wf.locators.UniqueIdLocator;
import com.lwolf.wf.persistence.DataFactory;
import com.lwolf.wf.repository.KeyRepository;
import com.lwolf.wf.repository.ModelRepository;
import com.lwolf.wf.repository.ProcessRepository;
import com.lwolf.wf.repository.impl.KeyRepositoryImpl;
import com.lwolf.wf.repository.impl.ModelRepositoryImpl;
import com.lwolf.wf.repository.impl.ProcessRepositoryImpl;
import com.lwolf.wf.services.ModelService;
import com.lwolf.wf.services.impl.ModelServiceImpl;

@Path("/model")
public class ModelResourceImpl implements ModelResource {
	
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String CONTENT_TYPE = HttpHeaders.CONTENT_TYPE;
	
	private final DataSourceLocator dataSourceLocator = TomeeResourceLocator.locator();
	private final IdLocator idLocator = UniqueIdLocator.load(new UniqueIdLocator(TomeeResourceLocator.locator()));
	private final DataFactory dataFactory = DataFactory.getDataFactory(DataFactory.FactoryType.JDBC, dataSourceLocator, idLocator);
	
	private final KeyRepository keyRepository = new KeyRepositoryImpl(dataFactory.keyDao());
	private final ModelRepository modelRepository = new ModelRepositoryImpl(dataFactory.modelDao());
	private final ProcessRepository processRepository = new ProcessRepositoryImpl(dataFactory.processDao());
	private final ModelService service = new ModelServiceImpl(modelRepository, processRepository, TomeeResourceLocator.locator());
	
	private final RequestAuthenticator requestAuthenticator = new RequestAuthenticator(keyRepository);
	
	public List<ModelDto> list(HttpServletRequest request) {
		try {
			requestAuthenticator.authenticate(request);
			return service.list();
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ModelDto create(HttpServletRequest request, ModelDto model) {
		try {
			
			requestAuthenticator.authenticate(request, model);
			
			if (model.tempName != null && !model.tempName.equals("")) {
				File file = new File(ApplicationProperties.uploadTempDir() + "/" + model.tempName);
				model.fileData = new BufferedInputStream(new FileInputStream(file));
			}
			service.add(model);
			
			return model;
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (FileNotFoundException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ModelDto read(HttpServletRequest request, Integer modelId) {
		try {
			
			requestAuthenticator.authenticate(request);
			
			ModelDto model = new ModelDto(modelId);
			
			service.get(model);
			
			if (model.modelId == null) {
				throw new WebApplicationException(Status.NOT_FOUND);
			} else {
				return model;
			}
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ModelDto update(HttpServletRequest request, Integer modelId, ModelDto model) {
		try {
			
			requestAuthenticator.authenticate(request, model);
			
			model.modelId = modelId;
			
			if (model.tempName != null && !model.tempName.equals("")) {
				File file = new File(ApplicationProperties.uploadTempDir() + "/" + model.tempName);
				model.fileData = new BufferedInputStream(new FileInputStream(file));
			}
			
			service.update(model);
			
			return model;
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (FileNotFoundException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (JsonMappingException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (JsonGenerationException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ModelDto delete(HttpServletRequest request, Integer modelId) {
		try {
			
			requestAuthenticator.authenticate(request);
			
			ModelDto model = new ModelDto(modelId);
			service.remove(model);
			
			return model;
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public Response getFile(HttpServletRequest request, Integer modelId) {
		try {
			
			requestAuthenticator.authenticate(request);
			
			ModelDto model = new ModelDto(modelId);
			
			service.get(model);
			
			ResponseBuilder builder = Response.ok(model.fileData);
			builder = builder.header(CONTENT_DISPOSITION, "attachment; filename=" + model.fileName);
			builder = builder.header(CONTENT_TYPE, model.fileType);
			
			return builder.build();
		} catch (ServiceException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}

}
