package me.midest.hours168.hibernatepersistence.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenience builder class for creating parameter
 * maps to use with queries.
 */
public class ParamBuilder {

    private static final int DEFAULT_CAPACITY = 10;

    private final Map<String, Object> map;

    /**
     * Create builder with map with initial capacity of {@value DEFAULT_CAPACITY}.
     * @return builder
     */
    public static ParamBuilder map(){
        return mapOf( DEFAULT_CAPACITY );
    }

    /**
     * Create builder with map with given initial capacity.
     * @param capacity initial capacity
     * @return builder
     */
    public static ParamBuilder mapOf( int capacity){
        return new ParamBuilder( capacity );
    }

    private ParamBuilder(int capacity){
        this.map = new HashMap<>( capacity );
    }

    /**
     * Put named parameter.
     * @param key name
     * @param value value
     * @return this builder
     */
    public ParamBuilder put( String key, Object value ){
        map.put( key, value );
        return this;
    }

    /**
     * Get map with parameters.
     * @return parameter map
     */
    public Map<String, Object> get(){
        return map;
    }

}
