package com.alphadevs.wikunum.services.service;

import com.alphadevs.wikunum.services.domain.Payments;
import com.alphadevs.wikunum.services.repository.PaymentsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Payments}.
 */
@Service
@Transactional
public class PaymentsService {

    private final Logger log = LoggerFactory.getLogger(PaymentsService.class);

    private final PaymentsRepository paymentsRepository;

    public PaymentsService(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    /**
     * Save a payments.
     *
     * @param payments the entity to save.
     * @return the persisted entity.
     */
    public Payments save(Payments payments) {
        log.debug("Request to save Payments : {}", payments);
        return paymentsRepository.save(payments);
    }

    /**
     * Update a payments.
     *
     * @param payments the entity to save.
     * @return the persisted entity.
     */
    public Payments update(Payments payments) {
        log.debug("Request to update Payments : {}", payments);
        return paymentsRepository.save(payments);
    }

    /**
     * Partially update a payments.
     *
     * @param payments the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Payments> partialUpdate(Payments payments) {
        log.debug("Request to partially update Payments : {}", payments);

        return paymentsRepository
            .findById(payments.getId())
            .map(existingPayments -> {
                if (payments.getNotes() != null) {
                    existingPayments.setNotes(payments.getNotes());
                }
                if (payments.getTransactionDate() != null) {
                    existingPayments.setTransactionDate(payments.getTransactionDate());
                }
                if (payments.getCashAmount() != null) {
                    existingPayments.setCashAmount(payments.getCashAmount());
                }
                if (payments.getCreditAmount() != null) {
                    existingPayments.setCreditAmount(payments.getCreditAmount());
                }
                if (payments.getCreditCardAmount() != null) {
                    existingPayments.setCreditCardAmount(payments.getCreditCardAmount());
                }
                if (payments.getTransactionID() != null) {
                    existingPayments.setTransactionID(payments.getTransactionID());
                }
                if (payments.getLocationCode() != null) {
                    existingPayments.setLocationCode(payments.getLocationCode());
                }
                if (payments.getTenantCode() != null) {
                    existingPayments.setTenantCode(payments.getTenantCode());
                }

                return existingPayments;
            })
            .map(paymentsRepository::save);
    }

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Payments> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentsRepository.findAll(pageable);
    }

    /**
     * Get one payments by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Payments> findOne(Long id) {
        log.debug("Request to get Payments : {}", id);
        return paymentsRepository.findById(id);
    }

    /**
     * Delete the payments by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Payments : {}", id);
        paymentsRepository.deleteById(id);
    }
}
