package RSP.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import RSP.dao.TripCriteriaDao;
import RSP.dto.TripsQueryCriteria;
import RSP.model.Country;
import RSP.model.Tag;
import RSP.model.Trip;
import RSP.model.TripCriteria;

@Service
@Transactional
public class TripCriteriaService {

    private TripCriteriaDao criteriaDao;
    private TripService tripService;

    @Autowired
    public TripCriteriaService(TripCriteriaDao criteriaDao) {
        this.criteriaDao = criteriaDao;
    }

    public void add(TripCriteria criteria) {
        Objects.requireNonNull(criteria, "trip criteria must not be null");
        criteriaDao.add(criteria);
    }

    public TripCriteria get(int id) throws TripCriteriaNotFoundException {
        TripCriteria criteria = criteriaDao.get(id);
        if (criteria == null) {
            throw new TripCriteriaNotFoundException(id);
        }
        return criteria;
    }

    public List<Trip> getSelectedTrips(int id)
            throws TripCriteriaNotFoundException,
                    InvalidQueryException,
                    InconsistentQueryException {
        TripCriteria criteria = get(id);
        return tripService.getSome(translate(criteria));
    }

    public TripsQueryCriteria translate(TripCriteria criteria) {
        TripsQueryCriteria result = new TripsQueryCriteria();

        List<Country> countries = criteria.getCountries();
        if (countries != null && !countries.isEmpty()) {
            List<String> countryNames = new ArrayList<>(countries.size());
            for (Country country : countries) {
                countryNames.add(country.getName());
            }
            result.setCountry(countryNames);
        }

        List<Tag> tags = criteria.getTags();
        if (tags != null && !tags.isEmpty()) {
            List<String> tagNames = new ArrayList<>(tags.size());
            for (Tag tag : tags) {
                tagNames.add(tag.getName());
            }
            result.setTag(tagNames);
        }

        result.setMinPrice(criteria.getMinPrice());
        result.setMaxPrice(criteria.getMaxPrice());
        result.setMinLength(criteria.getMinDuration());
        result.setMaxLength(criteria.getMaxDuration());
        result.setStartBefore(criteria.getStartBefore());
        result.setStartAfter(criteria.getStartAfter());
        result.setEndBefore(criteria.getEndBefore());
        result.setEndAfter(criteria.getEndAfter());
        result.setInName(criteria.getInName());
        return result;
    }

    public void remove(int id) throws TripCriteriaNotFoundException {
        TripCriteria criteria = criteriaDao.get(id);
        if (criteria == null) {
            throw new TripCriteriaNotFoundException(id);
        }
        criteriaDao.remove(criteria);
    }

    // BULK OPERATIONS

    public List<TripCriteria> getAll() {
        return criteriaDao.getAll();
    }

    public void removeAll() {
        criteriaDao.removeAll();
    }
}
