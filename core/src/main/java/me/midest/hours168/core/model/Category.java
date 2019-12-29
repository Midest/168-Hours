package me.midest.hours168.core.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Category of time segment. It has name, can also have alternative names (aliases), parent and children.
 */
@Entity(name = "categories")
@Table(name="categories")
public class Category {

    public static final String ALIAS_DELIMITER = ";";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String aliasesString;

    @Transient
    private List<String> aliases;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @OrderBy("name")
    private List<Category> children;

    Category(){}

    public Category( String name ){
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getAliasesString() {
        return aliasesString;
    }

    /**
     * Set aliases and aliasesString from string with aliases.
     * @param aliasesString string of alternative names
     * @see #ALIAS_DELIMITER delimeter for aliases
     */
    public void setAliasesString( String aliasesString ) {
        if( aliasesString == null ) aliasesString = "";
        this.aliasesString = aliasesString;
        if( this.aliases == null )
            this.aliases = new ArrayList<>();
        this.aliases.clear();
        this.aliases.addAll( Arrays.asList( aliasesString.split( "\\s*" + ALIAS_DELIMITER + "\\s*" )));
    }

    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Set aliases and aliasesString from the list of aliases.
     * @param aliases list of alternative names
     * @see #ALIAS_DELIMITER delimeter for aliases
     */
    public void setAliases( List<String> aliases ) {
        this.aliases = aliases;
        this.aliasesString = String.join( ALIAS_DELIMITER, aliases );
    }

    public Category getParent() {
        return parent;
    }

    public void setParent( Category parent ) {
        this.parent = parent;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren( List<Category> children ) {
        this.children = children;
    }
}
