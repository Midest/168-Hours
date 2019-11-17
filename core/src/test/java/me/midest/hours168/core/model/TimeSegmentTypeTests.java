package me.midest.hours168.core.model;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class TimeSegmentTypeTests {

    private static final Logger logger = LoggerFactory.getLogger( TimeSegmentTypeTests.class );

    @Test
    public void lengthValues(){
        for( TimeSegmentType t : TimeSegmentType.values()){
            short lName = Short.parseShort( t.name().substring( 1 ));
            short lVal = t.getLength();
            logger.debug( "Testing length values for {}: {}", t.name(), t.getLength());
            assertEquals( "Wrong value for " + t.name(), lName, lVal );
        }
    }

}
