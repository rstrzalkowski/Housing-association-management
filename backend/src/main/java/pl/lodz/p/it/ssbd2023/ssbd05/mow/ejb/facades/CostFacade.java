package pl.lodz.p.it.ssbd2023.ssbd05.mow.ejb.facades;

import static pl.lodz.p.it.ssbd2023.ssbd05.shared.Roles.MANAGER;
import static pl.lodz.p.it.ssbd2023.ssbd05.shared.Roles.OWNER;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd05.entities.mow.Cost;
import pl.lodz.p.it.ssbd2023.ssbd05.exceptions.AppDatabaseException;
import pl.lodz.p.it.ssbd2023.ssbd05.interceptors.GenericFacadeExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd05.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd05.shared.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd05.shared.Page;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Stateless
@DenyAll
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({
    GenericFacadeExceptionsInterceptor.class,
    LoggerInterceptor.class
})
public class CostFacade extends AbstractFacade<Cost> {

    @PersistenceContext(unitName = "ssbd05mowPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CostFacade() {
        super(Cost.class);
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByYear(Year year) throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByYear", Cost.class);
            tq.setParameter("year", year);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByYear, Database Exception", e);
        }
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByMonth(Month month) throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByMonth", Cost.class);
            tq.setParameter("month", month);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByMonth, Database Exception", e);
        }
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByCategoryId(Long categoryId) throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByCategoryId", Cost.class);
            tq.setParameter("categoryId", categoryId);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByCategoryId, Database Exception", e);
        }
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByCategoryName(String categoryName) throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByCategoryName", Cost.class);
            tq.setParameter("categoryName", categoryName);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByCategoryName, Database Exception", e);
        }
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByYearAndMonth(Year year, Month month) throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByYearAndMonth", Cost.class);
            tq.setParameter("year", year);
            tq.setParameter("month", month);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByYearAndMonth, Database Exception", e);
        }
    }

    @PermitAll
    public List<Cost> findByYearAndCategoryId(Year year, Long categoryId) throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByYearAndCategoryId", Cost.class);
            tq.setParameter("year", year);
            tq.setParameter("categoryId", categoryId);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByYearAndCategoryId, Database Exception", e);
        }
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByYearAndCategoryName(Year year, String categoryName) throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByYearAndCategoryName", Cost.class);
            tq.setParameter("year", year);
            tq.setParameter("categoryName", categoryName);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByYearAndCategoryName, Database Exception", e);
        }
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByMonthAndCategoryId(Month month, Long categoryId) throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByMonthAndCategoryId", Cost.class);
            tq.setParameter("month", month);
            tq.setParameter("categoryId", categoryId);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByMonthAndCategoryId, Database Exception", e);
        }
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByMonthAndCategoryName(Month month, String categoryName) throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByMonthAndCategoryName", Cost.class);
            tq.setParameter("month", month);
            tq.setParameter("categoryName", categoryName);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByMonthAndCategoryName, Database Exception", e);
        }
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByYearAndMonthAndCategoryId(Year year, Month month, Long categoryId)
        throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByYearAndMonthAndCategoryId", Cost.class);
            tq.setParameter("year", year);
            tq.setParameter("month", month);
            tq.setParameter("categoryId", categoryId);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByYearAndMonthAndCategoryId, Database Exception", e);
        }
    }

    @RolesAllowed({OWNER, MANAGER})
    public List<Cost> findByYearAndMonthAndCategoryName(Year year, Month month, String categoryName)
        throws AppDatabaseException {
        try {
            TypedQuery<Cost> tq = em.createNamedQuery("Cost.findByYearAndMonthAndCategoryName", Cost.class);
            tq.setParameter("year", year);
            tq.setParameter("month", month);
            tq.setParameter("categoryName", categoryName);
            return tq.getResultList();
        } catch (PersistenceException e) {
            throw new AppDatabaseException("Cost.findByYearAndMonthAndCategoryName, Database Exception", e);
        }
    }

    @RolesAllowed({MANAGER})
    public Page<Cost> findByYearAndMonthAndCategoryName(int page, int pageSize, boolean order, Year year,
                                                        String categoryName) {
        TypedQuery<Cost> tq;

        if (order) {
            tq = em.createNamedQuery("Cost.findByYearAndCategoryNameAsc", Cost.class);
        } else {
            tq = em.createNamedQuery("Cost.findByYearAndCategoryNameDesc", Cost.class);
        }

        tq.setParameter("year", year);
        tq.setParameter("categoryName", categoryName);
        tq.setFirstResult(page * pageSize);
        tq.setMaxResults(pageSize);
        List<Cost> list = tq.getResultList();

        Long count = countCostsByYearAndMonthAndCategoryName(year, categoryName);
        return new Page<>(list, count, pageSize, page);

    }

    @RolesAllowed({MANAGER})
    public Long countCostsByYearAndMonthAndCategoryName(Year year,
                                                        String categoryName) {
        TypedQuery<Long> tq = em.createNamedQuery("Cost.countByYearAndCategoryName", Long.class);
        tq.setParameter("year", year);
        tq.setParameter("categoryName", categoryName);
        return tq.getSingleResult();
    }

    @Override
    @RolesAllowed({MANAGER})
    public List<Cost> findAll() {
        return super.findAll();
    }

    @Override
    @RolesAllowed({MANAGER})
    public Optional<Cost> find(Long id) {
        return super.find(id);
    }

    @RolesAllowed({MANAGER})
    public List<Year> findDistinctYears() {
        TypedQuery<Year> tq = em.createNamedQuery("Cost.findDistinctYears", Year.class);
        return tq.getResultList();
    }

    @RolesAllowed({MANAGER})
    public List<String> findDistinctCategoryNamesFromCosts() {
        TypedQuery<String> tq = em.createNamedQuery("Cost.findDistinctCategoryNames", String.class);
        return tq.getResultList();
    }

    @RolesAllowed(MANAGER)
    public BigDecimal sumConsumptionForCategoryAndMonthBefore(Year year, Long categoryId, Month month) {
        try {
            return em.createNamedQuery("Cost.sumConsumptionForCategoryAndYearAndMonthBefore", BigDecimal.class)
                .setParameter("year", year)
                .setParameter("categoryId", categoryId)
                .setParameter("month", month)
                .getSingleResult();
        } catch (NoResultException nre) {
            return BigDecimal.ZERO;
        }
    }

    @RolesAllowed(MANAGER)
    public BigDecimal sumConsumptionForCategoryAndMonth(Year year, Long categoryId, Month month) {
        try {
            return em.createNamedQuery("Cost.sumConsumptionForCategoryAndYearAndMonth", BigDecimal.class)
                .setParameter("year", year)
                .setParameter("categoryId", categoryId)
                .setParameter("month", month)
                .getSingleResult();
        } catch (NoResultException nre) {
            return BigDecimal.ZERO;
        }
    }
}
