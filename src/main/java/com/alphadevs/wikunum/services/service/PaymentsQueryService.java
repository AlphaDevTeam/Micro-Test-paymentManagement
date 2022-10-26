package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.*; // for static metamodels
import com.alphadevs.wikunum.services.domain.Payments;
import com.alphadevs.wikunum.services.repository.PaymentsRepository;
import com.alphadevs.wikunum.services.service.criteria.PaymentsCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Payments} entities in the database.
 * The main input is a {@link PaymentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Payments} or a {@link Page} of {@link Payments} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentsQueryService extends QueryService<Payments> {

    private final Logger log = LoggerFactory.getLogger(PaymentsQueryService.class);

    private final PaymentsRepository paymentsRepository;

    public PaymentsQueryService(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    /**
     * Return a {@link List} of {@link Payments} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Payments> findByCriteria(PaymentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Payments> specification = createSpecification(criteria);
        return paymentsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Payments} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Payments> findByCriteria(PaymentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Payments> specification = createSpecification(criteria);
        return paymentsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Payments> specification = createSpecification(criteria);
        return paymentsRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Payments> createSpecification(PaymentsCriteria criteria) {
        Specification<Payments> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Payments_.id));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Payments_.notes));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), Payments_.transactionDate));
            }
            if (criteria.getCashAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCashAmount(), Payments_.cashAmount));
            }
            if (criteria.getCreditAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditAmount(), Payments_.creditAmount));
            }
            if (criteria.getCreditCardAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditCardAmount(), Payments_.creditCardAmount));
            }
            if (criteria.getTransactionID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionID(), Payments_.transactionID));
            }
            if (criteria.getLocationCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocationCode(), Payments_.locationCode));
            }
            if (criteria.getTenantCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTenantCode(), Payments_.tenantCode));
            }
        }
        return specification;
    }
}
