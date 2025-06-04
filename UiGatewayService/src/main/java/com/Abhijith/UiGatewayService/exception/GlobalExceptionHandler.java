package com.Abhijith.UiGatewayService.exception;

import feign.FeignException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * This class centralizes error handling across all controllers.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle Feign client exceptions that occur when calling microservices.
     * These typically happen when a service is down or returns an error.
     */
    @ExceptionHandler(FeignException.class)
    public String handleFeignException(FeignException ex, HttpServletRequest request, Model model) {
        // Determine the appropriate status code
        int statusCode = ex.status() > 0 ? ex.status() : HttpStatus.SERVICE_UNAVAILABLE.value();
        log.error("Feign exception", ex);
        // Set error attributes for the error page
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, statusCode);
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, getServiceErrorMessage(ex));
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, ex);
        
        return "forward:/error";
    }
    
    /**
     * Handle all other exceptions not specifically handled elsewhere.
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, HttpServletRequest request, Model model) {
        // Set error attributes for the error page
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "An unexpected error occurred: " + ex.getMessage());
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, ex);
        log.error(ex.getMessage(), ex);
        return "forward:/error";
    }
    
    /**
     * Generate a user-friendly error message based on the Feign exception.
     */
    private String getServiceErrorMessage(FeignException ex) {
        String message = "A service is currently unavailable.";
        
        // Extract service name from the URL if possible
        String url = ex.request().url();
        if (url != null) {
            if (url.contains("applications")) {
                message = "Application service is currently unavailable.";
                
            } else if (url.contains("jobs")) {
                message = "Job service is currently unavailable.";
            } else if (url.contains("resume")) {
                message = "Resume service is currently unavailable.";
            }
        }
        log.error(message, ex);
        return message + " Please try again later.";
    }
}
