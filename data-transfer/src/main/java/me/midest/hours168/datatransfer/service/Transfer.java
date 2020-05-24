package me.midest.hours168.datatransfer.service;

public interface Transfer {
    /**
     * Should return qualifier name for the class. By default tries to return prefix
     * (part to first uppercase letter after first char) in lowercase from classname.
     * @return qualifier name for this implementation or {@code null}
     */
    default String returnType(){
        final String interfaceName = "Transfer";
        String name = getClass().getSimpleName();
        int index = 1;
        if( name.contains( interfaceName )) {
            while( name.length() > index && Character.isLowerCase( name.charAt( index ) ) )
                index++;
            return name.substring( 0, index ).toLowerCase();
        }
        else return null;
    }

    /**
     * Should return file extension (without dot). By default returns {@link #returnType() type}.
     * @return file extension of transfer file type for this implementation
     */
    default String fileExtension(){
        return returnType();
    }
}
