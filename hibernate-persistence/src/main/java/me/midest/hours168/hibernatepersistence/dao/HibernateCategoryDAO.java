package me.midest.hours168.hibernatepersistence.dao;

import me.midest.hours168.core.dao.CategoryDAO;
import me.midest.hours168.core.dao.exceptions.DeletionException;
import me.midest.hours168.core.model.Category;
import me.midest.hours168.hibernatepersistence.util.ParamBuilder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class HibernateCategoryDAO extends BaseCRUD<Category> implements CategoryDAO {

    public HibernateCategoryDAO(){
        super( Category.class );
    }

    @Override
    public Category getCategory( long id ) {
        return getByID( id );
    }

    @Override
    public Collection<Category> getCategories( String name ) {
        String query = "FROM categories WHERE name=:nm";
        Map<String, Object> params = ParamBuilder.mapOf(1).put( "nm", name ).get();
        return getWithParamQuery( query, params );
    }

    @Override
    public long persistCategory( Category category ) {
        return saveNew( category );
    }

    @Override
    public void updateCategory( Category category ) {
        update( category );
    }

    @Override
    public Category deleteCategoryBranch( long id ) {
        if( !canDeleteTree( getCategory( id )))
            throw new DeletionException( id );
        return delete( id );
    }

    @Override
    public Category deleteCategorySingle( long id ) {
        if( !canDelete( id ))
            throw new DeletionException( id );
        Category category = getByID( id );
        if( category != null ){
            String query = "FROM categories WHERE parent_id=:pid";
            Map<String, Object> params = ParamBuilder.mapOf(1)
                    .put( "pid", id )
                    .get();
            Category parent = category.getParent();
            Collection<Category> children = getWithParamQuery( query, params );
            category.setChildren( null );
            children.forEach( c -> {
                c.setParent( parent );
                update( c );
            });
            delete( category );
        }
        return category;
    }

    /**
     * {@inheritDoc} Sort by name in ascending order.
     * @param alias name or alias
     * @return {@inheritDoc}
     * @see #getCategories()
     */
    @Override
    public Collection<Category> getCategoryByAlias( String alias ) {
        Collection<Category> categories = getCategories();
        categories.removeIf( c -> !(c.getName().equals( alias ) || c.getAliases().contains( alias )));
        return categories;
    }

    /**
     * {@inheritDoc} Sort by name in ascending order.
     * @return {@inheritDoc}
     */
    @Override
    public Collection<Category> getCategories() {
        return getWithQuery( "FROM categories ORDER BY name ASC" );
    }

    /**
     * Recursively check if we can delete category with its children.
     * @param root category to check
     * @return {@code true} if the whole tree can be deleted
     */
    private boolean canDeleteTree( Category root ){
        if( canDelete( root.getId()))
            for( Category ch : root.getChildren()){
                if( !canDeleteTree( ch ))
                    return false;
            }
        else return false;
        return true;
    }

    /**
     * Check if category can be deleted (i.e. has no segments reference it).
     * @param id category id to check
     * @return {@code true} if category can be deleted
     */
    private boolean canDelete( long id ){
        String query = "FROM time_segments ts INNER JOIN ts.category c WHERE c.id=:id";
        Map<String, Object> params = ParamBuilder.mapOf(1).put( "id", id ).get();
        return getWithParamQuery( query, params, 1 ).isEmpty();
    }

}
