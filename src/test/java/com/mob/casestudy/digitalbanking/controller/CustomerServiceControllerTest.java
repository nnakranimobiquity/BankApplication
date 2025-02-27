package com.mob.casestudy.digitalbanking.controller;

import com.mob.casestudy.digitalbanking.dto.CustomerDto;
import com.mob.casestudy.digitalbanking.dto.GetAllSecurityQuestionDto;
import com.mob.casestudy.digitalbanking.dto.SecurityQuestionsDto;
import com.mob.casestudy.digitalbanking.entity.Customer;
import com.mob.casestudy.digitalbanking.entity.SecurityQuestions;
import com.mob.casestudy.digitalbanking.enums.CustomerStatus;
import com.mob.casestudy.digitalbanking.exceptionresponse.DataNotFoundException;
import com.mob.casestudy.digitalbanking.repository.CustomerRepository;
import com.mob.casestudy.digitalbanking.service.CustomerService;
import com.mob.casestudy.digitalbanking.service.SecurityQuestionsService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class CustomerServiceControllerTest {


    @InjectMocks
    CustomerServiceController customerServiceController;
    @Mock
    CustomerService customerService;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    SecurityQuestionsService securityQuestionsService;

    @Test
    void updateCustomer_Data_And_Response_Ok() {
        String customerName = "kevin";
        CustomerDto customerDto = new CustomerDto();
        ResponseEntity responseEntity = customerServiceController.updateCustomer(customerDto, customerName);
        ResponseEntity expectedResponse = new ResponseEntity<>("User Updated:" + customerName, HttpStatus.OK);
        Assertions.assertThat(expectedResponse).usingRecursiveComparison().isEqualTo(responseEntity);
    }

    @Test
    void retrieveCustomer() {
        Customer customer = Customer.builder().userName("kep")
                .firstName("kevin").lastName("patel")
                .phoneNumber("9664847593").email("kevinpatel1142@gmail.com")
                .status(CustomerStatus.ACTIVE).preferredLanguage("EN")
                .externalId("1").createdBy("self").createdOn(LocalDateTime.now())
                .updatedBy("k-win").updatedOn(LocalDateTime.now()).build();
        CustomerDto customerDto = customer.toDto();

        Mockito.when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        ResponseEntity responseEntity = customerServiceController.getCustomers();
        ResponseEntity expectedResponse = new ResponseEntity<>(customerDto, HttpStatus.OK);
        Assertions.assertThat(expectedResponse).usingRecursiveComparison().isEqualTo(responseEntity);
    }

    @Test
    void findByName() {
        String userName = "kep";
        Customer customer = Customer.builder().userName("kep")
                .firstName("kevin").lastName("patel")
                .phoneNumber("9664847593").email("kevinpatel1142@gmail.com")
                .status(CustomerStatus.ACTIVE).preferredLanguage("EN")
                .externalId("1").createdBy("self").createdOn(LocalDateTime.now())
                .updatedBy("k-win").updatedOn(LocalDateTime.now()).build();
        CustomerDto customerDto = customer.toDto();
        Mockito.when(customerRepository.findByUserName(userName)).thenReturn(Optional.of(customer));
        ResponseEntity responseEntity = customerServiceController.findByName(userName);
        ResponseEntity expectedResponse = new ResponseEntity<>(customerDto, HttpStatus.OK);
        Assertions.assertThat(expectedResponse).usingRecursiveComparison().isEqualTo(responseEntity);
    }

    @Test
    void findByName_If_User_Is_Empty_Throw_Exception() {
        String userName = "kep";
        Customer customer = Customer.builder().userName("kep")
                .firstName("kevin").lastName("patel")
                .phoneNumber("9664847593").email("kevinpatel1142@gmail.com")
                .status(CustomerStatus.ACTIVE).preferredLanguage("EN")
                .externalId("1").createdBy("self").createdOn(LocalDateTime.now())
                .updatedBy("k-win").updatedOn(LocalDateTime.now()).build();
        CustomerDto customerDto = customer.toDto();
        Mockito.when(customerRepository.findByUserName(userName)).thenReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(DataNotFoundException.class,
                () -> customerServiceController.findByName(userName));
    }

    @Test
    void getAllSecurityQuestions() {
        SecurityQuestions questions = SecurityQuestions.builder()
                .securityQuestionText("What is Your Favourite Car").build();
        SecurityQuestionsDto dto = questions.toDto();
        List<SecurityQuestionsDto> securityQuestionsDtos = securityQuestionsService.retrieveAllQuestions();
        Mockito.when(securityQuestionsDtos)
                .thenReturn(Collections.singletonList(dto));
        GetAllSecurityQuestionDto getAllSecurityQuestionDto
                = new GetAllSecurityQuestionDto(Collections.singletonList(dto));

        ResponseEntity<Object> allSecurityQuestions = customerServiceController.getAllSecurityQuestions();
        ResponseEntity expectedResponse = new ResponseEntity<>(getAllSecurityQuestionDto, HttpStatus.OK);
        Assertions.assertThat(expectedResponse).usingRecursiveComparison().isEqualTo(allSecurityQuestions);
    }
}