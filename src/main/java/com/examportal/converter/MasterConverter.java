package com.examportal.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class MasterConverter {

    @Autowired
    public ModelMapper modelMapper;
}
