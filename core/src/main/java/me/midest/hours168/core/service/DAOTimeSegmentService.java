package me.midest.hours168.core.service;

import me.midest.hours168.core.dao.CategoryDAO;
import me.midest.hours168.core.dao.TimeSegmentDAO;
import me.midest.hours168.core.model.Category;
import me.midest.hours168.core.model.TimeSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @implNote uses {@link TimeSegmentDAO} and {@link CategoryDAO}
 */
@Service
public class DAOTimeSegmentService implements TimeSegmentService {

    private final TimeSegmentDAO timeSegmentDAO;
    private final CategoryDAO categoryDAO;

    @Autowired
    public DAOTimeSegmentService( TimeSegmentDAO timeSegmentDAO, CategoryDAO categoryDAO ){
        this.timeSegmentDAO = timeSegmentDAO;
        this.categoryDAO = categoryDAO;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc} or <code>-1</code> if there was
     * no category or no segment was updated
     */
    @Override
    public long updateSegmentCategory( long segmentId, long categoryId ) {
        TimeSegment s = timeSegmentDAO.getTimeSegment( segmentId );
        long id = -1;
        if( s != null ){
            Category cat = s.getCategory();
            if( cat != null )
                id = cat.getId();
            Category newCat = categoryDAO.getCategory( categoryId );
            s.setCategory( newCat );
            timeSegmentDAO.updateTimeSegment( s );
        }
        return id;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc} or <code>-1</code> if there was
     * no category or no segment was updated
     */
    @Override
    public long updateSegment( long segmentId, long categoryId, String comment ) {
        TimeSegment s = timeSegmentDAO.getTimeSegment( segmentId );
        long id = -1;
        if( s != null ){
            Category cat = s.getCategory();
            if( cat != null )
                id = cat.getId();
            Category newCat = categoryDAO.getCategory( categoryId );
            s.setCategory( newCat );
            s.setComment( comment );
            timeSegmentDAO.updateTimeSegment( s );
        }
        return id;
    }

}
