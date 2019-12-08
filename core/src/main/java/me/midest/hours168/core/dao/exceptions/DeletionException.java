package me.midest.hours168.core.dao.exceptions;

/**
 * Exception for when object deletion is not possible.
 *
 * Currently only for categories. They cannot be deleted
 * if time segments references them. This behavior will probably change
 * in future releases (better ask and change reference of segments
 * then deny deletion altogether).
 */
public class DeletionException extends RuntimeException {

    public DeletionException( long id ){
        super( "Cannot delete Category with id = " + id );
    }

}
