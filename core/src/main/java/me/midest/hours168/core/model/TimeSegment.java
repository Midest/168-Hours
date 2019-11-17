package me.midest.hours168.core.model;

import javax.persistence.*;

/**
 * Time segment of day. It has day reference, position in day and length.
 * Can also have category (in amount of segments) and commentary.
 */
@Entity(name = "time_segments")
@Table(name="time_segments")
public class TimeSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "day_id")
    private Day day;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "positionInDay")
    private short positionInDay;

    private short segmentLength;

    private String comment;

    private TimeSegment(){}

    public TimeSegment( Day day, short positionInDay, short segmentLength ){
        this.day = day;
        this.positionInDay = positionInDay;
        this.segmentLength = segmentLength;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Day getDay() {
        return day;
    }

    public void setDay( Day day ) {
        this.day = day;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory( Category category ) {
        this.category = category;
    }

    public short getPositionInDay() {
        return positionInDay;
    }

    public void setPositionInDay( short positionInDay ) {
        this.positionInDay = positionInDay;
    }

    public short getSegmentLength() {
        return segmentLength;
    }

    public void setSegmentLength( short segmentLength ) {
        this.segmentLength = segmentLength;
    }

    public String getComment() {
        return comment;
    }

    public void setComment( String comment ) {
        this.comment = comment;
    }

}
