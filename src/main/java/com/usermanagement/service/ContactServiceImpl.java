package com.usermanagement.service;

import com.usermanagement.constants.Messages;
import com.usermanagement.dto.ContactDTO;
import com.usermanagement.dto.VerifyMailDTO;
import com.usermanagement.entity.Contact;
import com.usermanagement.exception.custom.InternalServerException;
import com.usermanagement.exception.custom.ResourceNotFoundException;
import com.usermanagement.repository.IContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class ContactServiceImpl implements IContactService{

    @Autowired
    private IContactRepository contactRepository;





    @Autowired
    private ContactConverter contactConverter;

    private @Value("${email}") String supportMail;

    private @Value("${contact.us.template}") String contactUsTemplate;






    public void save(ContactDTO contactDTO) throws ResourceNotFoundException, InternalServerException {
        try {
            String errorFields = "";
            if (StringUtils.isBlank(contactDTO.getName())) {
                errorFields = errorFields + " Name";
            }
            if (StringUtils.isBlank(contactDTO.getEmail())) {
                errorFields = errorFields + " Email";
            }
            if (StringUtils.isBlank(contactDTO.getPhoneNUmber())) {
                errorFields = errorFields + " PhoneNUmber";
            }
            if (StringUtils.isBlank(contactDTO.getMessage())) {
                errorFields = errorFields + " Message";
            }
            if (!StringUtils.isBlank(errorFields)) {
                throw new ResourceNotFoundException("Fields: " + errorFields + " shouldn't be empty");
            }
            Contact contact = contactConverter.convert(contactDTO);
            contactRepository.save(contact);

            mailService.sendEmail(Collections.singletonList(supportMail),
                    contactDTO.getEmail(), "Enquiry from " + contact.getName(),
                    contactUsTemplate, VerifyMailDTO.builder()
                            .username(contactDTO.getName())
                            .email(contactDTO.getEmail())
                            .enquiryId(String.valueOf(contact.getId()))
                            .phone(contactDTO.getPhonenUmber())
                            .message(contactDTO.getMessage())
                            .build());
        } catch (DataAccessException e) {
            log.error(Messages.UNABLE_TO_ACCESS_DATA, e);
        }
    }
}
