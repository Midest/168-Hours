package me.midest.hours168.hibernatepersistence.dao;

import me.midest.hours168.core.model.Day;
import me.midest.hours168.core.dao.DayDAO;
import me.midest.hours168.hibernatepersistence.util.ParamBuilder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class HibernateDayDAO extends BaseCRUD<Day> implements DayDAO {

    public HibernateDayDAO(){
        super( Day.class );
    }

    @Override
    public Day getDay( long id ) {
        return getByID( id );
    }

    @Override
    public Day getDay( LocalDate date ) {
        String query = "FROM days WHERE day_date=:dt";
        Map<String, Object> params = ParamBuilder.mapOf(1)
                .put( "dt", date )
                .get();
        List<Day> result = getWithParamQuery( query, params, 1 );
        return result.isEmpty() ? null : result.get( 0 );
    }

    @Override
    public Day getLastDay() {
        String query = "FROM days ORDER BY day_date DESC";
        List<Day> result = getWithQuery( query, 1 );
        return result.isEmpty() ? null : result.get( 0 );
    }

    @Override
    public Day getFirstDay() {
        String query = "FROM days ORDER BY day_date ASC";
        List<Day> result = getWithQuery( query, 1 );
        return result.isEmpty() ? null : result.get( 0 );
    }

    @Override
    public long persistDay( Day day ) {
        return saveNew( day );
    }

    @Override
    public void updateDay( Day day ) {
        update( day );
    }

    @Override
    public Day deleteDay( long id ) {
        return delete( id );
    }

    /**
     * {@inheritDoc} Sort by date in ascending order.
     * @return {@inheritDoc}
     */
    @Override
    public Collection<Day> getDays() {
        return getWithQuery( "FROM days ORDER BY day_date ASC" );
    }

    /**
     * {@inheritDoc} Sort result by date in ascending order.
     * @return {@inheritDoc}
     */
    @Override
    public Collection<Day> getLastDays( int n ) {
        List<Day> days = getWithQuery( "FROM days ORDER BY day_date DESC", n );
        Collections.reverse( days );
        return days;
    }

    /**
     * {@inheritDoc} Sort by date in ascending order.
     * @return {@inheritDoc}
     */
    @Override
    public Collection<Day> getDaysBetween( LocalDate boundary1, LocalDate boundary2 ) {
        LocalDate max = boundary2.isAfter( boundary1 ) ? boundary2 : boundary1;
        LocalDate min = max == boundary2 ? boundary1 : boundary2;
        String query = "FROM days WHERE date BETWEEN :minDt AND :maxDt ORDER BY day_date ASC";
        Map<String, Object> params = ParamBuilder.mapOf(2)
                .put( "minDt", min )
                .put( "maxDt", max )
                .get();
        return getWithParamQuery( query, params );
    }

}
