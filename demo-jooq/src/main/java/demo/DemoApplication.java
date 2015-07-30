package demo;

import demo.domain.Customer;
import demo.domain.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class DemoApplication {
    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping("/")
    List<Customer> customers() {
        return customerRepository.findAll();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}