package me.midest.hours168.datatransfer.service;

import me.midest.hours168.core.model.Category;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public interface CategoriesTransfer extends Transfer {

    /**
     * Read categories list from input stream.
     * @param fileInputStream input stream with file contents
     * @return list of categories read from file (all, not only roots)
     * @throws IOException exception while working with Input/OutputStreams
     */
    List<Category> readFromFile( InputStream fileInputStream ) throws IOException;

    /**
     * Return categories file as byte array.
     * @param categories collection of categories to write in a file
     * @return generated file
     * @throws IOException exception while working with Input/OutputStreams
     */
    byte[] getFile( Collection<Category> categories ) throws IOException;

}
