package me.midest.hours168.core.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(name="days")
@Table(
        name="days",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"day_date"})
)
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "smallint")
    private TimeSegmentType segmentType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "day")
    private List<TimeSegment> segments;

    @Column(name = "day_date")
    private LocalDate date;

    Day(){}

    /**
     * Create day with given date and default type.
     * @param date date that this day represents
     * @see TimeSegmentType#DEFAULT
     */
    public Day( LocalDate date ){
        this( date, TimeSegmentType.DEFAULT );
    }

    /**
     * Create day with current date and given type.
     * @param type day segmentation type
     * @see TimeSegmentType#DEFAULT
     */
    public Day( TimeSegmentType type ){
        this( LocalDate.now(), type );
    }

    /**
     * Create day with given date and given type.
     * @param type day segmentation type
     * @see TimeSegmentType#DEFAULT
     */
    public Day( LocalDate date, TimeSegmentType type ){
        this.date = date;
        this.segmentType = type;
        this.segments = Collections.unmodifiableList( createListFromType( segmentType ));
    }

    public long getId() {
        return id;
    }

    public TimeSegmentType getSegmentType() {
        return segmentType;
    }

    public List<TimeSegment> getSegments() {
        return segments;
    }

    public LocalDate getDate() {
        return date;
    }

    private List<TimeSegment> createListFromType( TimeSegmentType segmentType ) {
        short count = (short) ( TimeSegmentType.DAY_LENGTH / segmentType.getLength());
        List<TimeSegment> list = new ArrayList<>( 1 );
        for( int i = 1; i <= count; i++ )
            list.add( new TimeSegment( Day.this, (short)i, (short)1 )); //TODO segments of different lengths
        return list;
    }

}
