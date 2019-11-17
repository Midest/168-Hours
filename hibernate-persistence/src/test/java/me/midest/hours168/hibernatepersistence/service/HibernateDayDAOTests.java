package me.midest.hours168.hibernatepersistence.service;

import me.midest.hours168.core.dao.DayDAO;
import me.midest.hours168.core.model.Day;
import me.midest.hours168.hibernatepersistence.HibernateSpringConfig;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { HibernateSpringConfig.class })
public class HibernateDayDAOTests {

    private static final Logger logger = LoggerFactory.getLogger( HibernateDayDAOTests.class );

    @Autowired
    private DayDAO hibernateDayDAO;

    @Before
    public void setUp(){
        hibernateDayDAO.getDays().forEach( d -> hibernateDayDAO.deleteDay( d.getId()));
    }

    @Test
    public void test(){
        Day day = new Day( LocalDate.now());
        long id = hibernateDayDAO.persistDay( day );
        Day day2 = hibernateDayDAO.getDay( id );
        logger.debug( "day is {}", day.getDate() );
        assertEquals( day.getId(), day2.getId() );
        assertEquals( day.getDate(), day2.getDate());
    }

    @Test
    public void testBetween(){
        // -5
        Day day0 = new Day( LocalDate.now().minus( 5, ChronoUnit.DAYS ));
        hibernateDayDAO.persistDay( day0 );
        // 0
        Day day = new Day( LocalDate.now());
        hibernateDayDAO.persistDay( day );
        // +1
        Day day2 = new Day( LocalDate.now().plus( 1, ChronoUnit.DAYS ));
        hibernateDayDAO.persistDay( day2 );

        // [0,7]
        Collection<Day> days = hibernateDayDAO.getDaysBetween(
                LocalDate.now(),
                LocalDate.now().plus( 7, ChronoUnit.DAYS )
        );

        logger.debug( "size is {}", days.size());
        assertEquals( 2, days.size());
    }

}
