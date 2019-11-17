package me.midest.hours168.core.dao;

import me.midest.hours168.core.model.Category;

import java.util.Collection;

/**
 * Service for category objects repository operations.
 */
public interface CategoryDAO {

    /**
     * Get category by its id.
     * @param id unique number of the category
     * @return category object or <code>null</code>
     */
    Category getCategory( long id );

    /**
     * Get all categories with given name (not searching aliases).
     * @param name name of the category
     * @return collection of category objects (may be empty)
     */
    Collection<Category> getCategories( String name );

    /**
     * Persist category in repository.
     * @param category category to persist
     * @return unique number assigned to that category
     */
    long persistCategory( Category category );

    /**
     * Update given category in repository.
     * @param category category with updated fields to find and update in repo
     */
    void updateCategory( Category category );

    /**
     * Delete category with its children.
     * @param id unique number of the category
     * @return deleted category object
     */
    Category deleteCategoryBranch( long id );

    /**
     * Delete single category and move its children to its parent.
     * @param id unique number of the category
     * @return deleted category object
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

}
