package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@Controller
public class DemoApplication {

    final AtomicInteger counter = new AtomicInteger(0);

    @RequestMapping("/")
    String hello(Model model) {
        model.addAttribute("hello", this.counter.incrementAndGet());
        return "index";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Configuration
    static class ScriptConfig extends WebMvcConfigurerAdapter {
        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            registry.scriptTemplate()
                    .prefix("classpath:/templates/")
                    .suffix(".ejs");
        }

        @Bean
        ScriptTemplateConfigurer configurer() {
            ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
            configurer.setEngineName("js");
            configurer.setRenderFunction("render");
            configurer.setScripts(
                    "static/ejs.min.js",
                    "static/render.js");
            return configurer;
        }
    }
}
