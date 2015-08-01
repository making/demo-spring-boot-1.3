package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@SpringBootApplication
@Controller
public class DemoApplication {

    @RequestMapping("/")
    String index(Model model) {
        model.addAttribute("hello", "Hello! @" + LocalDateTime.now());
        return "index";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}