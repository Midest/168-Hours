package me.midest.hours168.core.dao;

import me.midest.hours168.core.model.TimeSegment;

import java.util.Collection;

/**
 * Service for segment objects repository operations.
 */
public interface TimeSegmentDAO {

    /**
     * Get segment by its id.
     * @param id unique number of the segment
     * @return segment object or <code>null</code>
     */
    TimeSegment getTimeSegment( long id );

    /**
     * Persist segment in repository and return its id.
     * @param timeSegment object to persist
     * @return unique number assigned to it
     */
    long persistTimeSegment( TimeSegment timeSegment );

    /**
     * Update segment in repository.
     * @param timeSegment segment to update
     */
    void updateTimeSegment( TimeSegment timeSegment );

    /**
     * Delete segment with given id.
     * @param id unique number of the segment
     * @return deleted segment object or <code>null</code>
     */
    TimeSegment deleteTimeSegment( long id );

    /**
     * Get all segments of a day with a given id.
     * @return collection of all segments of a day (may be empty)
     */
    Collection<TimeSegment> getTimeSegments( long dayId );

    /**
     * Get all segments.
     * @return collection of all segments (may be empty)
     */
    Collection<TimeSegment> getTimeSegments();

}
