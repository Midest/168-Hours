package me.midest.hours168.core.service;

import me.midest.hours168.core.dao.DayDAO;
import me.midest.hours168.core.model.Day;
import me.midest.hours168.core.model.TimeSegmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

/**
 * @implNote Uses {@link DayDAO}
 */
@Service
public class DAODayService implements DayService {

    private final DayDAO dayDAO;

    @Autowired
    public DAODayService( DayDAO dayDAO ){
        this.dayDAO = dayDAO;
    }

    /**
     * {@inheritDoc}
     * Use current date.
     * @return
     */
    @Override
    public Day createDay() {
        return createDay( LocalDate.now());
    }

    @Override
    public Day createDay( TimeSegmentType segmentType ) {
        return new Day( segmentType );
    }

    @Override
    public Day createDay( LocalDate date ) {
        return new Day( date );
    }

    @Override
    public Day createDay( TimeSegmentType segmentType, LocalDate date ) {
        return new Day( date, segmentType );
    }

    @Override
    public Day getDay( long id ) {
        return dayDAO.getDay( id );
    }

    @Override
    public Day getDay( LocalDate date ) {
        return dayDAO.getDay( date );
    }

    @Override
    public Day getLastDay() {
        return dayDAO.getLastDay();
    }

    @Override
    public Day getFirstDay() {
        return dayDAO.getFirstDay();
    }

    @Override
    public long persistDay( Day day ) {
        return dayDAO.persistDay( day );
    }

    @Override
    public void updateDay( Day day ) {
        dayDAO.updateDay( day );
    }

    /**
     * {@inheritDoc}
     * @param day day to add or update
     * @return id of inserted or updated day or -1
     * if day was not inserted because of date duplication
     */
    @Override
    public long addOrUpdateDay( Day day ) {
        Day byDate = dayDAO.getDay( day.getDate());
        if( day.getId() <= 0 && byDate == null )
            return dayDAO.persistDay( day );
        else if( day.getId() > 0 ){
            dayDAO.updateDay( day );
            return day.getId();
        }
        else return -1;
    }

    /**
     * {@inheritDoc}
     * Default type is used.
     * @return
     */
    @Override
    public Day persistNewDay(){
        return persistNewDay( TimeSegmentType.DEFAULT );
    }

    /**
     * {@inheritDoc}
     * If there's already days in repository, use date next to last used.
     * Otherwise use current date.
     * @param type
     * @return
     */
    @Override
    public Day persistNewDay( TimeSegmentType type ){
        Day last = dayDAO.getLastDay();
        LocalDate date = last == null ? LocalDate.now() : last.getDate().plus( 1, ChronoUnit.DAYS );
        long id = persistDay( new Day( date, type ));
        return getDay( id );
    }

    @Override
    public Day deleteDay( long id ) {
        return dayDAO.deleteDay( id );
    }

    @Override
    public Collection<Day> getDays() {
        return dayDAO.getDays();
    }

    @Override
    public Collection<Day> getLastDays( int n ) {
        return dayDAO.getLastDays(n);
    }

    @Override
    public Collection<Day> getDaysBetween( LocalDate boundary1, LocalDate boundary2 ) {
        return dayDAO.getDaysBetween( boundary1, boundary2 );
    }

}
