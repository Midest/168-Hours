package me.midest.hours168.core.dao;

import me.midest.hours168.core.model.Day;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Service for day objects repository operations.
 */
public interface DayDAO {

    /**
     * Get day by its id.
     * @param id unique number of the day
     * @return day object or <code>null</code>
     */
    Day getDay( long id );

    /**
     * Get day by date.
     * @param date date for searching
     * @return day object associated with that date of <code>null</code>
     */
    Day getDay( LocalDate date );

    /**
     * Get day with highest date.
     * @return day object or <code>null</code> if no days in repository
     */
    Day getLastDay();

    /**
     * Get day with lowest date.
     * @return day object or <code>null</code> if no days in repository
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
