package com.lambdaschool.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@EnableWebMvc
@SpringBootApplication
public class SchoolApplication
{

    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(SchoolApplication.class, args);

        DispatcherServlet dispatcher = (DispatcherServlet)ctx.getBean("dispatcherServlet");
        dispatcher.setThrowExceptionIfNoHandlerFound(true);
    }

}
