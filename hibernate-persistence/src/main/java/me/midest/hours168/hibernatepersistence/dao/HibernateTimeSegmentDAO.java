package me.midest.hours168.hibernatepersistence.dao;

import static me.midest.hours168.hibernatepersistence.dao.QueryLang.*;

import me.midest.hours168.core.dao.TimeSegmentDAO;
import me.midest.hours168.core.model.TimeSegment;
import me.midest.hours168.hibernatepersistence.util.ParamBuilder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class HibernateTimeSegmentDAO extends BaseCRUD<TimeSegment> implements TimeSegmentDAO {

    HibernateTimeSegmentDAO() {
        super( TimeSegment.class );
    }

    @Override
    public TimeSegment getTimeSegment( long id ) {
        return getByID( id );
    }

    @Override
    public long persistTimeSegment( TimeSegment timeSegment ) {
        return saveNew( timeSegment );
    }

    @Override
    public void updateTimeSegment( TimeSegment timeSegment ) {
        update( timeSegment );
    }

    @Override
    public TimeSegment deleteTimeSegment( long id ) {
        return delete( id );
    }

    @Override
    public Collection<TimeSegment> getTimeSegments( long dayId ) {
        String query = "FROM time_segments WHERE day_id=:di ORDER BY positionInDay ASC";
        Map<String, Object> params = ParamBuilder.mapOf(1).put( "di", dayId ).get();
        return getWithParamQuery( query, HQL, params );
    }

    /**
     * {@inheritDoc} Sort by (day id, position in day) in ascending order.
     * @return {@inheritDoc}
     */
    @Override
    public Collection<TimeSegment> getTimeSegments() {
        return getWithQuery( "FROM time_segments ORDER BY day_id, positionInDay ASC", HQL );
    }
}
