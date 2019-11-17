package me.midest.hours168.web.controller;

import me.midest.hours168.core.model.Category;
import me.midest.hours168.core.service.CategoryService;
import me.midest.hours168.web.util.Mappings;
import me.midest.hours168.web.util.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CategoriesController {

    private static final Logger logger = LoggerFactory.getLogger( CategoriesController.class );

    private final CategoryService categoryService;

    @Autowired
    public CategoriesController( CategoryService categoryService ){
        this.categoryService = categoryService;
        logger.debug( "Categories controller created" );
    }

    @GetMapping(Mappings.CATEGORIES)
    public String categories( Model model ){
        Collection<Category> categories = categoryService.getCategories();
        Collection<Category> roots = new ArrayList<>( categories );
        roots.removeIf( c -> c.getParent() != null );

        // Build sorted categories map of (branchName, id)
        Map<String, Long> categoriesMap = new TreeMap<>();
        categories.forEach( c -> categoriesMap.put( categoryService.buildBranchName( c ), c.getId()));

        model.addAttribute( "categoriesList", categoriesMap );
        model.addAttribute( "categoriesRoots", roots );
        return ViewNames.CATEGORIES;
    }

    @RequestMapping(value = Mappings.NEW_CATEGORY, method = { RequestMethod.GET, RequestMethod.POST })
    public String addCategory(
            @RequestParam                   String categoryName,
            @RequestParam(required = false) String aliasesString,
            @RequestParam                   long parentId
    ){
        if( categoryName != null && !categoryName.trim().isEmpty() ) {
            Category category = new Category( categoryName.trim() );
            Category parent = categoryService.getCategory( parentId );
            if( aliasesString != null && !aliasesString.trim().isEmpty())
                category.setAliasesString( aliasesString );
            category.setParent( parent );
            categoryService.persistCategory( category );
        }
        return Mappings.REDIRECT_CATEGORIES;
    }

    @RequestMapping(value = Mappings.DELETE_CATEGORY, method = { RequestMethod.GET, RequestMethod.POST })
    public String deleteCategory(
            @RequestParam                           String id,
            @RequestParam(defaultValue = "false")   boolean deleteBranch
    ){
        long idL = -1;
        try {
            idL = Long.parseLong( id );
        } catch( NumberFormatException ex ){
            logger.info( "No number found in {}", id );
        }

        if( idL > 0 ) {
            if( deleteBranch )
                categoryService.deleteCategoryBranch( idL );
            else
                categoryService.deleteCategorySingle( idL );
        }

        return Mappings.REDIRECT_CATEGORIES;
    }

}
