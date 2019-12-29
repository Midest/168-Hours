package me.midest.hours168.core.service;

import me.midest.hours168.core.model.Category;

import java.util.Collection;

/**
 * Service for category objects operations.
 */
public interface CategoryService {

    /**
     * Get category by its id from repository.
     * @param id unique number of the category
     * @return category object or <code>null</code>
     */
    Category getCategory( long id );

    /**
     * Get all categories with given name (not searching aliases) from repository.
     * @param name name of the category
     * @return collection of category objects (may be empty)
     */
    Collection<Category> getCategories( String name );

    /**
     * Persist category in repository.
     * @param category object to persist
     * @return unique number assigned to it
     */
    long persistCategory( Category category );

    /**
     * Update given category in repository.
     * @param category category with updated fields to find and update in repo
     */
    void updateCategory( Category category );

    /**
     * Set new parent for given category.
     * @param category category
     * @param parent new parent
     * @return old parent or <code>null</code> if there wasn't one
     */
    Category changeCategoryParent( Category category, Category parent );

    /**
     * Persist of update given category in repository.
     * @param category object to add or update
     * @return unique number assigned to it
     */
    long addOrUpdateCategory( Category category );

    /**
     * Persist of update given categories in repository.
     * @param categories collection of objects to add or update
     */
    void addOrUpdateCategories( Collection<Category> categories );

    /**
     * Delete category with its children.
     * @param id unique number of the category
     * @return deleted category object or <code>null</code>
     */
    Category deleteCategoryBranch( long id );

    /**
     * Delete single category and move its children to its parent.
     * @param id unique number of the category
     * @return deleted category object or <code>null</code>
     */
    Category deleteCategorySingle( long id );

    /**
     * Get all categories with given name (search in aliases too).
     * @param alias name or alias
     * @return collection of category objects (may be empty)
     */
    Collection<Category> getCategoryByAlias( String alias );

    /**
     * Get all categories.
     * @return collection of all categories (may be empty)
     */
    Collection<Category> getCategories();

    /**
     * Get collection of only root categories.
     * @return collection of root categories (may be empty)
     */
    Collection<Category> getRootCategories();

    /**
     * Get string representation of branch with given category and all its ancestors.
     * Categories are divided by {@value #PATH_DELIM}.
     * @param category category object
     * @return string representation
     */
    default String buildBranchName( Category category ){
        if( category == null ) return null;
        StringBuilder sb = new StringBuilder( category.getName());
        Category parent = category.getParent();
        while( parent != null ){
            sb.insert( 0, PATH_DELIM ).insert( 0, parent.getName());
            parent = parent.getParent();
        }
        return sb.toString();
    }

    String PATH_DELIM = " > ";

}
