package com.springboot.MyTodoList.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.springboot.MyTodoList.model.UpdateType;
import com.springboot.MyTodoList.repository.UpdateTypeRepository;

// Marks the class as a Spring service component, 
// allowing it to be automatically detected and instantiated by Spring container
// during component scanning.

@Service
public class UpdateTypeService {
    @Autowired
    private UpdateTypeRepository updateTypeRepository;
    
    public UpdateTypeService(UpdateTypeRepository updateTypeRepository){
        this.updateTypeRepository = updateTypeRepository;
    }
    
    // --------------------- Get All User Type Method ---------------------
    public List<UpdateType> findAllUpdateTypes(){
        return updateTypeRepository.findAll();
    }
    
    // Print All User Type
    public String printUpdateTypeList(UpdateType updateType){
        // Print all information form user type
        String updateTypeInfo =  /*"Id " + updateType.getID().toString() + */ 
        " \nName " + updateType.getName();
        // " \nDescription " + updateType.getDescription();
        
        return updateTypeInfo;
    }   

}
