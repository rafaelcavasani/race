package br.com.race.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ControllerAdvice
public class HandleExceptionController {

    @ExceptionHandler(Exception.class)
    public ModelAndView exception(HttpServletRequest request, Exception exception){
        ModelAndView model = new ModelAndView("error");
        model.addObject("error", exception.getMessage());
        exception.printStackTrace();
        return model;
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView ioException(HttpServletRequest request, Exception exception){
        ModelAndView model = new ModelAndView("error");
        model.addObject("error", exception.getMessage());
        exception.printStackTrace();
        return model;
    }
}
