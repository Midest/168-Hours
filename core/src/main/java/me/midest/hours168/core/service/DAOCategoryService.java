package me.midest.hours168.core.service;

import me.midest.hours168.core.dao.CategoryDAO;
import me.midest.hours168.core.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @implNote uses {@link CategoryDAO}
 */
@Service
public class DAOCategoryService implements CategoryService {

    private final CategoryDAO categoryDAO;

    @Autowired
    public DAOCategoryService( CategoryDAO categoryDAO ){
        this.categoryDAO = categoryDAO;
    }

    @Override
    public Category getCategory( long id ) {
        return categoryDAO.getCategory( id );
    }

    @Override
    public Collection<Category> getCategories( String name ) {
        return categoryDAO.getCategories( name );
    }

    @Override
    public long persistCategory( Category category ) {
        return categoryDAO.persistCategory( category );
    }

    @Override
    public void updateCategory( Category category ) {
        categoryDAO.updateCategory( category );
    }

    @Override
    public Category changeCategoryParent( Category category, Category parent ) {
        Category old = category.getParent();
        category.setParent( parent );
        categoryDAO.updateCategory( category );
        return old;
    }

    @Override
    public long addOrUpdateCategory( Category category ) {
        long id = category.getId();
        if( id <= 0 )
            id = persistCategory( category );
        else
            updateCategory( category );
        return id;
    }

    @Override
    public Category deleteCategoryBranch( long id ) {
        return categoryDAO.deleteCategoryBranch( id );
    }

    @Override
    public Category deleteCategorySingle( long id ) {
        return categoryDAO.deleteCategorySingle( id );
    }

    @Override
    public Collection<Category> getCategoryByAlias( String alias ) {
        return categoryDAO.getCategoryByAlias( alias );
    }

    @Override
    public Collection<Category> getCategories() {
        return categoryDAO.getCategories();
    }
}
