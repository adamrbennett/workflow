package com.lwolf.wf.test.unit;

import java.io.BufferedInputStream;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.lwolf.wf.dto.ModelDto;
import com.lwolf.wf.persistence.ModelDao;
import com.lwolf.wf.persistence.ProcessDao;
import com.lwolf.wf.persistence.entities.ModelEntity;
import com.lwolf.wf.persistence.jdbc.JdbcModelDao;
import com.lwolf.wf.persistence.jdbc.JdbcProcessDao;
import com.lwolf.wf.repository.ModelRepository;
import com.lwolf.wf.repository.ProcessRepository;
import com.lwolf.wf.repository.impl.ModelRepositoryImpl;
import com.lwolf.wf.repository.impl.ProcessRepositoryImpl;
import com.lwolf.wf.services.ModelService;
import com.lwolf.wf.services.impl.ModelServiceImpl;
import com.lwolf.wf.test.locators.TestResourceLocator;

public class ModelUT {
	
	private final ArgumentCaptor<ModelEntity> modelEntityArg = ArgumentCaptor.forClass(ModelEntity.class);
	private final ModelDao modelDaoMock = mock(JdbcModelDao.class);
	private final ProcessDao processDaoMock = mock(JdbcProcessDao.class);
	
	private final ModelRepository modelRepository = new ModelRepositoryImpl(modelDaoMock);
	private final ProcessRepository processRepository = new ProcessRepositoryImpl(processDaoMock);
	private final ModelService modelService = new ModelServiceImpl(modelRepository, processRepository, new TestResourceLocator());
	
	@Test
	public void shouldCreateModel() throws Exception {
		// given
		ModelDto model = new ModelDto();
		model.name = "shouldCreateModel";
		model.fileName = "test.html";
		model.fileType = "text/html";
		model.fileData = new BufferedInputStream(getClass().getResourceAsStream("/com/lwolf/wf/test/resources/test.html"));
		
		// when
		modelService.add(model);
		
		// then
		verify(modelDaoMock).create(modelEntityArg.capture());
		assertEquals("ModelDao.create called with improper name value", "shouldCreateModel", modelEntityArg.getValue().name);
	}
	
	@Test
	public void shouldReadModel() throws Exception {
		// given
		ModelDto model = new ModelDto(new Integer(100));
		
		// when
		modelService.get(model);
		
		// then
		verify(modelDaoMock).read(modelEntityArg.capture());
		assertEquals("ModelDao.read called with improper modelId value", new Integer(100), modelEntityArg.getValue().modelId);
	}
	
	@Test
	public void shouldUpdateModel() throws Exception {
		// given
		ModelDto model = new ModelDto(new Integer(100));
		
		// when
		modelService.update(model);
		
		// then
		verify(modelDaoMock).update(modelEntityArg.capture());
		assertEquals("ModelDao.update called with improper modelId value", new Integer(100), modelEntityArg.getValue().modelId);
	}
	
	@Test
	public void shouldDeleteModel() throws Exception {
		// given
		ModelDto model = new ModelDto(new Integer(100));
		
		// when
		modelService.remove(model);
		
		// then
		verify(modelDaoMock).delete(modelEntityArg.capture());
		assertEquals("ModelDao.delete called with improper modelId value", new Integer(100), modelEntityArg.getValue().modelId);
	}

}
