package me.midest.hours168.web.handler;

import me.midest.hours168.core.dao.exceptions.DeletionException;
import me.midest.hours168.web.util.ViewNames;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger( GlobalExceptionHandler.class );

    @ExceptionHandler(value = DeletionException.class)
    public ModelAndView defaultErrorHandler( Exception e) throws Exception {
        logger.info( "Caught error {}", e.getClass());
        rethrowIfNeeded( e );
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorCode", DeletionException.class.getSimpleName());
        mav.addObject("errorMessage", e.getMessage());
        mav.addObject("errorAdvice",
                "Cannot delete object before removing all references to it.");
        mav.setViewName( ViewNames.ERROR );
        return mav;
    }

    private void rethrowIfNeeded( Exception e ) throws Exception {
        if ( AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;
    }

}
