package me.midest.hours168.core.service;

import me.midest.hours168.core.model.Day;
import me.midest.hours168.core.model.TimeSegmentType;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Service for day objects operations.
 */
public interface DayService {

    /**
     * Create new day with default values.
     * @return new day object
     */
    Day createDay();

    /**
     * Create new day with given segment type and default date.
     * @return new day object
     */
    Day createDay( TimeSegmentType segmentType );

    /**
     * Create new day with given date and default segment type.
     * @return new day object
     */
    Day createDay( LocalDate date );

    /**
     * Create new day with given segment type and date.
     * @return new day object
     */
    Day createDay( TimeSegmentType segmentType, LocalDate date );

    /**
     * Get day by its id.
     * @param id unique number of the day
     * @return day object or <code>null</code>
     */
    Day getDay( long id );

    /**
     * Get day by date.
     * @param date date for searching
     * @return day object associated with that date or <code>null</code>
     */
    Day getDay( LocalDate date );

    /**
     * Get day with highest date.
     * @return day object or <code>null</code>
     */
    Day getLastDay();

    /**
     * Get day with lowest date.
     * @return day object or <code>null</code>
     */
    Day getFirstDay();

    /**
     * Persist day in repository and return its id.
     * @param day day to persist
     * @return unique number assigned to that day
     */
    long persistDay( Day day );

    /**
     * Update day in repository.
     * @param day day to update
     */
    void updateDay( Day day );

    /**
     * Persist new day or update existing one (if id is set).
     * @param day day to persist or update
     * @return unique number assigned to that day or <code>-1</code>
     */
    long addOrUpdateDay( Day day );

    /**
     * Persist new day.
     * @return new day object
     */
    Day persistNewDay();

    /**
     * Persist new day with given type.
     * @return new day object
     */
    Day persistNewDay( TimeSegmentType type );

    /**
     * Delete day with given id.
     * @param id unique number of the day
     * @return deleted day object
     */
    Day deleteDay( long id );

    /**
     * Get all days.
     * @return collection of all days (may be empty)
     */
    Collection<Day> getDays();

    /**
     * Get last {@code n} days.
     * @param n max number of days returned
     * @return collection of last {@code n} days (may be empty)
     */
    Collection<Day> getLastDays( int n );

    /**
     * Get days between two dates.
     * @param boundary1 boundary date (inclusive)
     * @param boundary2 another boundary date (inclusive)
     * @return collection of days between two given dates (may be empty)
     */
    Collection<Day> getDaysBetween( LocalDate boundary1, LocalDate boundary2 );

}
