package me.midest.hours168.core.service;

import me.midest.hours168.core.model.Category;
import me.midest.hours168.core.model.Day;
import me.midest.hours168.core.model.TimeSegment;
import me.midest.hours168.core.model.TimeSegmentType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service for segment objects operations.
 */
public interface TimeSegmentService {

    /**
     * Update category of segment.
     * @param segmentId unique number of segment to update
     * @param categoryId unique number of category to update segment with
     * @return unique number of replaced category
     */
    long updateSegmentCategory( long segmentId, long categoryId );

    /**
     * Update category and comment of segment.
     * @param segmentId unique number of segment to update
     * @param categoryId unique number of category to update segment with
     * @param comment new comment
     * @return unique number of replaced category
     */
    long updateSegment( long segmentId, long categoryId, String comment );

    /**
     * Calculate lowest common multiple for number of segments in given days (from type).
     * @param days collection of days
     * @return LCM
     */
    static int lowestCommonMultiple( Collection<Day> days ) {
        byte[] r = days.parallelStream()
                .map( d -> d.getSegmentType().lengthFactorization() )
                .reduce( new byte[]{ 0, 0, 0 }, ( f1, f2 ) ->
                        new byte[]{
                                (byte) Math.max( f1[0], f2[0] ),
                                (byte) Math.max( f1[1], f2[1] ),
                                (byte) Math.max( f1[2], f2[2] ) }
                );
        return TimeSegmentType.length(r);
    }

    /**
     * Calculate highest possible length, that is a factor for lengths of segments in given days.
     * @param days collection of days to process
     * @return segment length
     */
    static int highestPeriodLength( Collection<Day> days ) {
        byte[] r = days.parallelStream()
                .map( d -> d.getSegmentType().lengthFactorization() )
                .reduce( new byte[]{ Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE }, ( f1, f2 ) ->
                        new byte[]{
                                (byte) Math.min( f1[0], f2[0] ),
                                (byte) Math.min( f1[1], f2[1] ),
                                (byte) Math.min( f1[2], f2[2] ) }
                );
        return TimeSegmentType.length(r);
    }

    /**
     * Translate time segments to equal length chunks based on LCM for number of segments.
     * @param days list of days
     * @return table of info {@link Chunk chunk's}
     * @see #lowestCommonMultiple(Collection)
     * @see Chunk
     */
    static List<List<Chunk>> segmentsInfo( List<Day> days ){
        int chunkLength = highestPeriodLength( days );
        int count = TimeSegmentType.DAY_LENGTH / chunkLength;
        List<List<Chunk>> res = new ArrayList<>( days.size());
        for( Day d : days ){
            List<Chunk> ch = new ArrayList<>( count );
            res.add( ch );
            int chunksPerSegmentUnit = d.getSegmentType().getLength() / chunkLength;
            for( TimeSegment s : d.getSegments()){
                int n = chunksPerSegmentUnit * s.getSegmentLength();
                ch.add( new Chunk(
                        s.getId(),
                        (short) (s.getPositionInDay() * chunksPerSegmentUnit),
                        (short) (s.getSegmentLength() * chunksPerSegmentUnit),
                        s.getCategory(),
                        s.getComment() ) );
                while( --n > 0 ) {
                    ch.add( Chunk.EMPTY );
                }
            }
        }
        return res;
    }

    /**
     * Divide day by equal length time periods and return them as a list
     * in <code>HH:mm</code> time format.
     * <br>E.g. <code>["00:00-08:00", "08:00-16:00", "16:00-24:00"]</code>
     * @param periodsCount number of periods
     * @return list of time periods
     */
    static List<String> timePeriods( int periodsCount ){
        List<String> res = new ArrayList<>( periodsCount );
        int minutesPerPeriod = TimeSegmentType.DAY_LENGTH / periodsCount;
        LocalTime t = LocalTime.MIDNIGHT;
        LocalTime t2 = t.plus( minutesPerPeriod, ChronoUnit.MINUTES );
        DateTimeFormatter tf = DateTimeFormatter.ofPattern( "HH:mm" );
        res.add( tf.format( t ) + "-" + ( t2.equals( LocalTime.MIDNIGHT ) ? "24:00" : tf.format( t2 ) ));
        for( int i = 1; i < periodsCount; i++ ){
            t = t2;
            t2 = t.plus( minutesPerPeriod, ChronoUnit.MINUTES );
            res.add( tf.format( t ) + "-" + ( t2.equals( LocalTime.MIDNIGHT ) ? "24:00" : tf.format( t2 ) ));
        }
        return res;
    }

    /**
     * Convenience class for time segments representation.
     */
    class Chunk{
        private final long segmentId;
        private final short position;
        private final short length;
        private final Category category;
        private final String comment;

        static final Chunk EMPTY = new Chunk( -1, (short)-1, (short)1, null, null );

        Chunk( long segmentId, short position, short length, Category category, String comment ) {
            this.segmentId = segmentId;
            this.position = position;
            this.length = length;
            this.category = category;
            this.comment = comment;
        }

        public long getSegmentId(){
            return segmentId;
        }

        public short getPosition() {
            return position;
        }

        public short getLength() {
            return length;
        }

        public Category getCategory() {
            return category;
        }

        public String getComment() {
            return comment;
        }
    }

}
