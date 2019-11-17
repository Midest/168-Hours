package me.midest.hours168.web.controller;

import me.midest.hours168.core.model.Day;
import me.midest.hours168.core.model.TimeSegmentType;
import me.midest.hours168.core.service.CategoryService;
import me.midest.hours168.core.service.DayService;
import me.midest.hours168.core.service.TimeSegmentService;
import me.midest.hours168.web.util.Mappings;
import me.midest.hours168.web.util.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

@Controller
@SessionAttributes(names = {"from", "to"})
public class TimetableController {

    private static final Logger logger = LoggerFactory.getLogger( TimetableController.class );

    @Autowired
    private ObjectFactory<HttpSession> httpSessionFactory;

    private final DayService dayService;
    private final TimeSegmentService segmentService;
    private final CategoryService categoryService;

    @Autowired
    public TimetableController( DayService dayService, TimeSegmentService segmentService, CategoryService categoryService ){
        this.dayService = dayService;
        this.segmentService = segmentService;
        this.categoryService = categoryService;
        logger.debug( "Timetable controller created" );
    }

    @GetMapping(Mappings.HOME)
    public String home(){
        return Mappings.REDIRECT_TIMETABLE;
    }

    @GetMapping(Mappings.TIMETABLE)
    public String timetable(
            @RequestParam( required = false ) LocalDate from,
            @RequestParam( required = false ) LocalDate to,
            Model model
    ){
        // If no params given, try to get them from session
        if( from == null && to == null ) {
            from = getAttribute( "from" );
            to = getAttribute( "to" );
            logger.info( "No time interval params given. Using {}, {} from session", from, to );
        }

        // Get days based on params
        Collection<Day> days;
        if( from == null && to == null ) {
            int last = 7;
            days = dayService.getLastDays( last );
            logger.info( "No time interval params found. Showing last {} days", last );
        }
        else if( from == null ){
            int last = 6;
            from = to.minus( last, ChronoUnit.DAYS );
            days = dayService.getDaysBetween( from, to );
            logger.info( "No 'from' param found. Showing given day ({}) and days in previous week", from );
        }
        else if( to == null ){
            int last = 6;
            to = from.plus( last, ChronoUnit.DAYS );
            days = dayService.getDaysBetween( from, to );
            logger.info( "No 'to' param found. Showing given day ({}) and days in next week", to );
        }
        else{
            days = dayService.getDaysBetween( from, to );
            logger.info( "Time interval params found. Using {}, {}", from, to );
        }

        // Update session attributes
        model.addAttribute( "from", from );
        model.addAttribute( "to", to );

        // Add params
        if( !days.isEmpty()) {
            int periodLength = TimeSegmentService.highestPeriodLength( days );
            int periodCount = TimeSegmentType.DAY_LENGTH / periodLength;

            // Build categories map of (branchName, id)
            TreeMap<String, Long> categoriesMap = new TreeMap<>();
            categoryService.getCategories().forEach( c -> categoriesMap.put( categoryService.buildBranchName( c ), c.getId() ) );

            model.addAttribute( "days", days );
            model.addAttribute( "firstDay", dayService.getFirstDay());
            model.addAttribute( "lastDay", dayService.getLastDay());
            model.addAttribute( "count", periodCount );
            model.addAttribute( "categoriesList", categoriesMap );
            model.addAttribute( "timeperiods", TimeSegmentService.timePeriods( periodCount ) );
            model.addAttribute( "segmentschunks", TimeSegmentService.segmentsInfo( new ArrayList<>( days ) ) );
        }
        return ViewNames.TIMETABLE;
    }

    @RequestMapping(value = Mappings.SHOW_DATES, method = { RequestMethod.GET, RequestMethod.POST })
    public String showDates(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Model model,
            SessionStatus sessionStatus
    ){
        if( from == null && to == null )
            sessionStatus.setComplete();
        model.addAttribute( "from", from );
        model.addAttribute( "to", to );
        return Mappings.REDIRECT_TIMETABLE;
    }

    @RequestMapping(value = Mappings.NEW_DAY, method = { RequestMethod.GET, RequestMethod.POST })
    public String addDay(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String typeId
    ){
        // Segment type
        TimeSegmentType type = TimeSegmentType.DEFAULT;
        if( typeId != null ) try{
            type = TimeSegmentType.values()[Integer.valueOf( typeId )];
        } catch( Exception ex ){
            logger.debug( "Wrong TimeSegmentType ordinal = {}", typeId );
        }

        // Creation
        if( date == null )
            dayService.persistNewDay( type );
        else
            dayService.addOrUpdateDay( dayService.createDay( type, date ) );

        return Mappings.REDIRECT_TIMETABLE;
    }

    @RequestMapping(value = Mappings.DELETE_DAY, method = { RequestMethod.GET, RequestMethod.POST })
    public String deleteDay( @RequestParam long id ){
        dayService.deleteDay( id );
        return Mappings.REDIRECT_TIMETABLE;
    }

    @PostMapping(Mappings.UPDATE_TIMESEGMENT)
    public String updateTimeSegment(
            @RequestParam long segmentId,
            @RequestParam long categoryId,
            @RequestParam String comment
    ){
        segmentService.updateSegment( segmentId, categoryId, comment );
        return Mappings.REDIRECT_TIMETABLE;
    }

    /**
     * Get attribute from http session.
     * @param name name of the param
     * @return attribute or {@code null}
     */
    @SuppressWarnings("unchecked")
    private <T> T getAttribute( String name ){
        HttpSession session = httpSessionFactory.getObject();
        Object obj = session.getAttribute( name );
        if( obj == null ) return null;
        else try{
            return (T)obj;
        } catch( ClassCastException e ){
            logger.error( "Wrong variable type given for reading session param: {}", e.getMessage() );
            return null;
        }
    }

}
