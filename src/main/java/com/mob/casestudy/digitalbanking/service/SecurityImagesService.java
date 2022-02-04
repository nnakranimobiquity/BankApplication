package com.mob.casestudy.digitalbanking.service;

import com.mob.casestudy.digitalbanking.compositekey.CustomerImage;
import com.mob.casestudy.digitalbanking.dto.CustomerSecurityImagesDto;
import com.mob.casestudy.digitalbanking.entity.Customer;
import com.mob.casestudy.digitalbanking.entity.CustomerSecurityImages;
import com.mob.casestudy.digitalbanking.entity.SecurityImages;
import com.mob.casestudy.digitalbanking.exceptionresponse.DataNotFoundException;
import com.mob.casestudy.digitalbanking.repository.CustomerRepository;
import com.mob.casestudy.digitalbanking.repository.CustomerSecurityImageRepository;
import com.mob.casestudy.digitalbanking.repository.SecurityImageRepository;
import com.mob.casestudy.digitalbanking.requestbody.CustomerSecurityImageRequestBody;
import com.mob.casestudy.digitalbanking.validator.CustomerDetailValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.mob.casestudy.digitalbanking.errorcodes.CustomisedErrorCodesAndDescription.*;


//csi
//si
@Service
public class SecurityImagesService {

    private final CustomerRepository customerRepository;
    private final CustomerSecurityImageRepository customerSecurityImageRepository;
    private final SecurityImageRepository securityImageRepository;
    private final EntityManager entityManager;
    private final CustomerDetailValidation customerDetailValidation;
    private final CustomerService customerService;

    @Autowired
    public SecurityImagesService(CustomerRepository customerRepository, CustomerSecurityImageRepository customerSecurityImageRepository, SecurityImageRepository securityImageRepository, EntityManager entityManager, CustomerDetailValidation customerDetailValidation,CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerSecurityImageRepository = customerSecurityImageRepository;
        this.securityImageRepository = securityImageRepository;
        this.entityManager = entityManager;
        this.customerDetailValidation = customerDetailValidation;
        this.customerService = customerService;
    }

    public CustomerSecurityImagesDto getSecurityImages(String userName) {
        Optional<Customer> customerResultOptional = customerRepository.findByUserName(userName);
        if (customerResultOptional.isEmpty()) {
            throw new DataNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND_DESCRIPTION);
        }
        Customer customer = customerResultOptional.get();
        return customer.getCustomerSecurityImages().toDto();
    }

    public Customer findCustomerByName(String userName) {
        Optional<Customer> customerResultOptional = customerRepository.findByUserName(userName);
        if (customerResultOptional.isEmpty()) {
            throw new DataNotFoundException(CUSTOMER_NOT_IN_TABLE, CUSTOMER_NOT_IN_TABLE_DESCRIPTION);
        }
        return customerResultOptional.get();
    }

    public void getCustomerSecurityImageAndDelete(Customer customer) {
        CustomerSecurityImages customerSecurityImages = customer.getCustomerSecurityImages();
        customerSecurityImageRepository.delete(customerSecurityImages);
        customerSecurityImageRepository.flush();
        entityManager.clear();
    }


    public SecurityImages findSecurityImageByIdFromRequestBody(CustomerSecurityImageRequestBody customerSecurityImageRequestBody) {
        Optional<SecurityImages> imageResult = securityImageRepository.findById(customerSecurityImageRequestBody.getSecurityImageId());
        if (imageResult.isEmpty()) {
            throw new DataNotFoundException(CUSTOMER_SECURITY_IMAGE_NOT_IN_TABLE, CUSTOMER_SECURITY_IMAGE_NOT_IN_TABLE_DESCRIPTION);
        }
        return imageResult.get();
    }

    @Transactional
    public void validateCustomerSecurityImageAndUpdate(String userName, CustomerSecurityImageRequestBody customerSecurityImageRequestBody) {
        customerDetailValidation.validateCustomerImageCaption(customerSecurityImageRequestBody);
        Customer customer = findCustomerByName(userName);
        // Customer customer1 = customerService.findCustomer(userName,CUSTOMER_NOT_IN_TABLE,CUSTOMER_NOT_IN_TABLE_DESCRIPTION);
        getCustomerSecurityImageAndDelete(customer);
        SecurityImages securityImageResult = findSecurityImageByIdFromRequestBody(customerSecurityImageRequestBody);
        updateCustomerSecurityImage(securityImageResult, customerSecurityImageRequestBody, customer);
    }

    public void updateCustomerSecurityImage(SecurityImages securityImageResult,CustomerSecurityImageRequestBody customerSecurityImageRequestBody,Customer customer) {
        String securityImageCaption = customerSecurityImageRequestBody.getSecurityImageCaption();
        CustomerSecurityImages customerSecurityImages = CustomerSecurityImages.builder().customerImage(new CustomerImage())
                .customer(customer)
                .securityImages(securityImageResult)
                .securityImageCaption(securityImageCaption).build();
//        CustomerSecurityImages customerSecurityImages = getCustomerSecurityImages(customer,securityImageResult,customerSecurityImageRequestBody.getSecurityImageCaption()); 
        customerSecurityImageRepository.save(customerSecurityImages);
    }

    private CustomerSecurityImages getCustomerSecurityImages(Customer customer, SecurityImages securityImage,String caption){
       return CustomerSecurityImages.builder().customerImage(new CustomerImage())
                .customer(customer)
                .securityImages(securityImage)
                .securityImageCaption(caption).build();
    }
}
