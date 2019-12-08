package me.midest.hours168.web.controller;

import me.midest.hours168.web.util.Mappings;
import me.midest.hours168.web.util.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorsController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger( ErrorsController.class );

    @RequestMapping(value = Mappings.ERROR, method = { RequestMethod.GET, RequestMethod.POST })
    public String error( HttpServletRequest httpRequest, Model model ){
        int code = getErrorCode( httpRequest );
        logger.info( "Caught some error: {}", code );
        model.addAttribute( "errorCode", code );
        model.addAttribute( "errorMessage", getErrorMessage( httpRequest ));
        model.addAttribute( "errorAdvice", "Let me know, I'll look into it. :)" );
        return ViewNames.ERROR;
    }

    private String getErrorMessage( HttpServletRequest req ) {
        int code = getErrorCode( req );
        switch( code ){
            case 404: return "Can't find such page.";
            case 400: return "Wrong request. Most likely invalid or absent parameters.";
            case 500: return "Internal error. Probably an uncaught exception. :\\";
            default: return "Can't find any useful information :(";
        }
    }

    @Override
    public String getErrorPath() {
        return Mappings.ERROR;
    }

    private static int getErrorCode( HttpServletRequest req ){
        Object status = req.getAttribute( RequestDispatcher.ERROR_STATUS_CODE );
        return status != null ?
                Integer.valueOf(status.toString()) : -1;
    }

}
