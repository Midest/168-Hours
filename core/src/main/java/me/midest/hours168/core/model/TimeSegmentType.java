package me.midest.hours168.core.model;

import me.midest.hours168.core.utils.FormattingUtils;

/**
 * Day segmentation types. Types are all divisors of {@link #DAY_LENGTH day length}.
 */
public enum TimeSegmentType {

    /*
     * Name is length in minutes
     */

    M0001(0,0,0), M0002(0,0,1), M0003(0,1,0),
    M0004(0,0,2), M0005(1,0,0), M0006(0,1,1),
    M0008(0,0,3), M0009(0,2,0), M0010(1,0,1),
    M0012(0,1,2), M0015(1,1,0), M0016(0,0,4),
    M0018(0,2,1), M0020(1,0,2), M0024(0,1,3),
    M0030(1,1,1), M0032(0,0,5), M0036(0,2,2),
    M0040(1,0,3), M0045(1,2,0), M0048(0,1,4),
    M0060(1,1,2), M0072(0,2,3), M0080(1,0,4),
    M0090(1,2,1), M0096(0,1,5), M0120(1,1,3),
    M0144(0,2,4), M0160(1,0,5), M0180(1,2,2),
    M0240(1,1,4), M0288(0,2,5), M0360(1,2,3),
    M0480(1,1,5), M0720(1,2,4), M1440(1,2,5);

    public static final TimeSegmentType DEFAULT = M0030;
    public static final short DAY_LENGTH = 1440;

    /*
     * toString() parts
     */

    private static final String STRING_PREFIX = "";
    private static final String STRING_POSTFIX = "";
    private static final String STRING_HOURS = "h ";
    private static final String STRING_MINUTES = "m";

    private final short length;
    private final byte[] v235 = new byte[3];

    TimeSegmentType(int five, int three, int two){
        this.v235[0] = (byte) two;
        this.v235[1] = (byte) three;
        this.v235[2] = (byte) five;
        this.length = (short)length(v235);
    }

    /**
     * Length of segment represented by array of
     * prime factors' powers (for 2's, 3's and 5's).
     * @param r array of prime factors' powers
     * @return product of primes to corresponding powers
     */
    public static int length( byte[] r ) {
        if( r.length < 3 ) return -1;
        int l = 1;
        for( int i = 0; i < r[0]; i++, l *= 2 ) {}
        for( int i = 0; i < r[1]; i++, l *= 3 ) {}
        for( int i = 0; i < r[2]; i++, l *= 5 ) {}
        return (short)l;
    }

    /**
     * Get segment length/
     * @return length
     */
    public short getLength() {
        return length;
    }

    /**
     * Factorization of segment length.
     * @return array of prime factors' powers.
     */
    public byte[] lengthFactorization() {
        return v235;
    }

    /**
     * Return string of length in hours-minutes format. E.g. <code>"01h 12m"</code>.
     * @return string representation of segment length
     */
    @Override
    public String toString() {
        int hours = length/60;
        int minutes = length%60;
        return STRING_PREFIX
                + FormattingUtils.twoDigits( hours ) + STRING_HOURS
                + FormattingUtils.twoDigits( minutes ) + STRING_MINUTES
                + STRING_POSTFIX;
    }
}
