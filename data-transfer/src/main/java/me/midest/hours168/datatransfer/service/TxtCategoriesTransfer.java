package me.midest.hours168.datatransfer.service;

import me.midest.hours168.core.model.Category;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service for reading and creating plain text file (txt) with categories.<br/><br/>
 * Format (without spaces):<br/>
 * <code>
 * [Id] \t | \t [Name] \t | \t [Aliases]<br/><br/>
 * </code>
 * Example:<br/>
 * <code>
 * 5	|	Internet	|	I;Int<br/>
 * 7	|	Recreation	|	Relax<br/>
 * &nbsp;     11	|	Videogames	|<br/>
 * 6	|	Books	|<br/>
 * </code><br>
 * Subcategories are placed below parent category and moved right with tab character.
 * Level defined by number of tab characters at the beginning of the line.
 */
@Service
public class TxtCategoriesTransfer implements CategoriesTransfer {

    private static final String DELIMITER = "\t|\t";
    private static final byte[] STEP = "\t".getBytes( StandardCharsets.UTF_8 );
    private static final byte[] LINE = "\r\n".getBytes( StandardCharsets.UTF_8 );

    @Override
    public List<Category> readFromFile( InputStream fileInputStream ) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] b = new byte[512];
        for( int i; 0 < (i = fileInputStream.read(b)); outputStream.write(b,0, i ));
        String text = outputStream.toString( StandardCharsets.UTF_8.name());
        return readFromText( text );
    }

    protected List<Category> readFromText( String text ) {
        String[] lines = text.split( "\r?\n" );
        List<Category> list = new ArrayList<>();
        List<Integer> levels = new ArrayList<>();

        // Read categories and their levels
        for( String line : lines ){
            levels.add( countTabs( line ));
            String[] parts = line.split( "\t\\|\t" );
            if( parts.length > 1 ){
                Category c = new Category( parts[1].trim());
                c.setId( parseId( parts[0].trim() ));
                if( parts.length > 2 )
                    c.setAliasesString( parts[2].trim());
                list.add( c );
            }
        }

        // Set categories relations
        setRelations( list, levels );
        return list;
    }

    /**
     * Recursively set relations of categories (multiple roots).
     * @param list list of categories
     * @param levels category level in a hierarchy tree
     */
    private void setRelations( List<Category> list, List<Integer> levels ) {
        if( list.size() < 2 )
            return;

        int from = 0;
        int rootLevel = levels.get( from );
        for( int i = 1; i < list.size(); i++ ){
            if( rootLevel >= levels.get(i)){
                setRelations( list, levels, from, i );
                from = i;
                rootLevel = levels.get(i);  // if first root wasn't on 0 level
            }
        }
        setRelations( list, levels, from, list.size());
    }

    /**
     * Recursively set relations of categories (single root).
     * @param list list of categories
     * @param levels category level in a hierarchy tree
     * @param from index (inclusive)
     * @param to index (exclusive)
     */
    private void setRelations( List<Category> list, List<Integer> levels, int from, int to ) {
        if( to-from < 2 )  // no children
            return;

        int blockFrom = from+1;
        int levelFrom = levels.get( blockFrom );
        Category root = list.get( from );
        for( int i = blockFrom+1; i < to; i++ ){
            if( levelFrom >= levels.get(i)){
                setRelation( root, list.get( blockFrom ));
                setRelations( list, levels, blockFrom, i );
                blockFrom = i;
                levelFrom = levels.get(i);
            }
        }
        // last block
        setRelation( root, list.get( blockFrom ));
        setRelations( list, levels, blockFrom, to );
    }

    private void setRelation( Category parent, Category child ){
        child.setParent( parent );
        List<Category> children = parent.getChildren();
        if( children == null ) children = new ArrayList<>();
        children.add( child );
        parent.setChildren( children );
    }

    private int countTabs( String line ){
        int tabs = 0;
        for( int i = 0; i < line.length(); i++ )
            if( '\t' == line.charAt(i)) tabs++;
            else return tabs;
        return tabs;
    }

    private long parseId( String id ){
        try{
            return Long.parseLong( id );
        } catch( NumberFormatException ex ){
            return -1;
        }
    }

    @Override
    public byte[] getFile( Collection<Category> categories ) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int level = 0;
        for( Category c : categories )
            writeCategory( outputStream, level, c );
        return outputStream.toByteArray();
    }

    private void writeCategory( ByteArrayOutputStream outputStream, int level, Category c ) throws IOException {
        for( int i = 0; i < level; i++ ){
            outputStream.write( STEP );
        }
        outputStream.write( categoryToString( c ).getBytes( StandardCharsets.UTF_8 ));
        outputStream.write( LINE );
        level++;
        for( Category c1 : c.getChildren())
            writeCategory( outputStream, level, c1 );
    }

    private String categoryToString( Category c ){
        String aliases = c.getAliasesString();
        if( aliases == null ) aliases = "";
        return c.getId() + DELIMITER + c.getName() + DELIMITER + aliases;
    }

}
