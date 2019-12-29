package me.midest.hours168.web.controller;

import me.midest.hours168.core.model.Category;
import me.midest.hours168.core.service.CategoryService;
import me.midest.hours168.datatransfer.service.CategoriesTransfer;
import me.midest.hours168.web.util.Mappings;
import me.midest.hours168.web.util.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Controller
public class CategoriesController {

    private static final Logger logger = LoggerFactory.getLogger( CategoriesController.class );

    private final CategoryService categoryService;
    private final Set<CategoriesTransfer> transfers;

    @Autowired
    public CategoriesController( CategoryService categoryService, Set<CategoriesTransfer> transfers ){
        this.categoryService = categoryService;
        this.transfers = transfers;
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

    @PostMapping(Mappings.DOWNLOAD_CATEGORIES)
    public @ResponseBody
    ResponseEntity<ByteArrayResource> downloadCategoriesFile(
            @RequestParam(required = false)     String type
    ){
        try {
            HttpHeaders header = new HttpHeaders();
            header.setContentType( new MediaType( "application", "force-download" ));

            CategoriesTransfer transfer = null;

            // Find appropriate transfer implementation
            if( type != null ) {
                for( CategoriesTransfer tr : transfers )
                    if( tr.returnType().equals( type ) ){
                        transfer = tr;
                        break;
                }
            }
            else if( transfers.size() > 0 )
                transfer = transfers.iterator().next();

            // Log absence of implementation or return file
            if( transfer == null ) logger.error( "No categories transferrers found" );
            else {
                header.set( HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=categories-" + LocalDate.now() + "." + transfer.fileExtension());
                return new ResponseEntity<>( new ByteArrayResource(
                        transfer.getFile( categoryService.getRootCategories())),
                        header,
                        HttpStatus.CREATED
                );
            }

        } catch( IOException e ){
            logger.error( "IO exception: " + e.getMessage());
        }
        return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
    }

    /**
     * Upload <b>new</b> categories.
     * @param file categories file
     * @return redirect
     */
    @PostMapping(Mappings.UPLOAD_CATEGORIES)
    public String uploadCategoriesFile(
            @RequestParam   MultipartFile file,
                            RedirectAttributes redirectAttributes
            ){
        logger.debug( "Uploaded categories file original filename: {}", file.getOriginalFilename());
        logger.debug( "Uploaded categories file content type: {}", file.getContentType());
        String orig = file.getOriginalFilename();
        if( orig != null && orig.contains(".")) {
            String ext = orig.substring( orig.lastIndexOf( "." ) + 1 );
            if( !ext.isEmpty()) for( CategoriesTransfer tr: transfers ){
                if( tr.fileExtension().equals( ext )) try {
                    List<Category> categories = tr.readFromFile( file.getInputStream());
                    logger.debug( "Categories read from file" );
                    categoryService.addOrUpdateCategories( categories );
                    logger.debug( "Categories updated or added" );
                    redirectAttributes.addFlashAttribute( "success",
                            "Categories uploaded successfully." );
                    break;
                } catch( IOException e ) {
                    logger.error( "IO exception on reading uploaded file: {}", e.getMessage());
                    redirectAttributes.addFlashAttribute( "error",
                            "IO exception on reading uploaded file." );
                }
                logger.info( "No appropriate transferrer was found" );
                redirectAttributes.addFlashAttribute( "warning",
                        "No appropriate transferrer was found. Change file extension or try another file." );
            }
        }
        else redirectAttributes.addFlashAttribute( "error",
                "Cannot find uploaded file name or extension." );

        return Mappings.REDIRECT_CATEGORIES;
    }

}
