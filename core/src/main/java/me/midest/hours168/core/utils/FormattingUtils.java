package me.midest.hours168.core.utils;

public final class FormattingUtils {

    /**
     * Return two digit string representation of a given number.
     * @param val number
     * @return two digit string representation of a given number
     */
    public static String twoDigits( int val ){
        assert val >= 0 && val < 100;
        if( val < 10 ) return "0" + val;
        else return String.valueOf( val );
    }

}
