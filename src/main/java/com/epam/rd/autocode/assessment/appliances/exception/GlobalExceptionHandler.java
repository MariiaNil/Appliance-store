package com.epam.rd.autocode.assessment.appliances.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex) {
        return createErrorView("Error: ", ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApplianceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleApplianceNotFoundException(ApplianceNotFoundException ex) {
        return createErrorView("Error appliance: ", ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleClientNotFoundException(ClientNotFoundException ex) {
        return createErrorView("Error client   : ", ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        return createErrorView("Error employee: ", ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ManufacturerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleManufacturerNotFoundException(ManufacturerNotFoundException ex) {
        return createErrorView("Error manufacturer  : ", ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleOrderNotFoundException(OrderNotFoundException ex) {
        return createErrorView("Error order : ", ex, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception ex) {
        return createErrorView("Internal Server Error: ", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ModelAndView createErrorView(String prefix, Exception ex, HttpStatus status) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", prefix + ex.getMessage());
        mav.setViewName("/errors/error");
        mav.setStatus(status);
        return mav;
    }

    private ModelAndView createErrorView(String message, HttpStatus status) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", message);
        mav.setViewName("/errors/error");
        mav.setStatus(status);
        return mav;
    }
}
