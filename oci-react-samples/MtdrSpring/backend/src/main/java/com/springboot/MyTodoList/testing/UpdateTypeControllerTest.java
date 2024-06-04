package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.controller.UpdateTypeController;
import com.springboot.MyTodoList.model.UpdateType;
import com.springboot.MyTodoList.service.UpdateTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateTypeControllerTest {

    @InjectMocks
    UpdateTypeController updateTypeController;

    @Mock
    UpdateTypeService updateTypeService;

    @Test
    public void testFindUpdateType() {
        UpdateType updateType1 = new UpdateType();
        updateType1.setID(1L);
        updateType1.setName("UpdateType 1");
        updateType1.setDescription("Description 1");

        UpdateType updateType2 = new UpdateType();
        updateType2.setID(2L);
        updateType2.setName("UpdateType 2");
        updateType2.setDescription("Description 2");

        List<UpdateType> updateTypeList = Arrays.asList(updateType1, updateType2);

        when(updateTypeService.findAllUpdateTypes()).thenReturn(updateTypeList);

        ResponseEntity<String> response = updateTypeController.findUpdateType();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("UpdateType{ID=1, name='UpdateType 1', description='Description 1'}\nUpdateType{ID=2, name='UpdateType 2', description='Description 2'}\n", response.getBody());
    }
}