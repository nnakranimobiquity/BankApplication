package com.mob.casestudy.digitalbanking;

import com.mob.casestudy.digitalbanking.entity.Customer;
import com.mob.casestudy.digitalbanking.enums.CustomerStatus;
import com.mob.casestudy.digitalbanking.enums.Language;
import com.mob.casestudy.digitalbanking.service.CustomerService;
import com.mob.casestudy.digitalbanking.service.SecurityQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class DigitalBankingApplication implements CommandLineRunner {


    @Autowired
    private CustomerService customerService;
    @Autowired
    private SecurityQuestionsService securityQuestionsService;

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

	//TODO: Refactor all the package name from camel case to lower case
	//TODO: Convert application.properties to application.yml
    @Override
    public void run(String... args) throws Exception {
        Customer customer = Customer.builder().userName("kep")
                .firstName("kevin").lastName("patel")
                .phoneNumber("9664847593").email("kevinpatel1142@gmail.com")
                .status(CustomerStatus.ACTIVE).preferredLanguage(Language.EN.toString())
                .externalId("1").createdBy("self").createdOn(LocalDateTime.now())
                .updatedBy("k-win").updatedOn(LocalDateTime.now()).build();
        customerService.saveCustomer(customer);
        securityQuestionsService.addSecurityQuestions();
    }
}
