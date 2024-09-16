package com.cuda.backend.utils;

import java.util.List;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import com.cuda.backend.entities.dto.TweetDTO;
import com.cuda.backend.entities.dto.UserDTO;

import io.hypersistence.utils.hibernate.type.util.ClassImportIntegrator;

public class ClassImportIntegratorIntegratorProvider implements IntegratorProvider{
 
    @Override
    public List<Integrator> getIntegrators(){
        return List.of(
            new ClassImportIntegrator(
                List.of(UserDTO.class,TweetDTO.class)
            )
        );
    }
}
