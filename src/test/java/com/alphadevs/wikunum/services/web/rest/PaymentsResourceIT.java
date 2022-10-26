package com.alphadevs.wikunum.services.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alphadevs.wikunum.services.IntegrationTest;
import com.alphadevs.wikunum.services.domain.Payments;
import com.alphadevs.wikunum.services.repository.PaymentsRepository;
import com.alphadevs.wikunum.services.service.criteria.PaymentsCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PaymentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentsResourceIT {

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_TRANSACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_CASH_AMOUNT = 1D;
    private static final Double UPDATED_CASH_AMOUNT = 2D;
    private static final Double SMALLER_CASH_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_CREDIT_AMOUNT = 1D;
    private static final Double UPDATED_CREDIT_AMOUNT = 2D;
    private static final Double SMALLER_CREDIT_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_CREDIT_CARD_AMOUNT = 1D;
    private static final Double UPDATED_CREDIT_CARD_AMOUNT = 2D;
    private static final Double SMALLER_CREDIT_CARD_AMOUNT = 1D - 1D;

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentsMockMvc;

    private Payments payments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payments createEntity(EntityManager em) {
        Payments payments = new Payments()
            .notes(DEFAULT_NOTES)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .cashAmount(DEFAULT_CASH_AMOUNT)
            .creditAmount(DEFAULT_CREDIT_AMOUNT)
            .creditCardAmount(DEFAULT_CREDIT_CARD_AMOUNT)
            .transactionID(DEFAULT_TRANSACTION_ID)
            .locationCode(DEFAULT_LOCATION_CODE)
            .tenantCode(DEFAULT_TENANT_CODE);
        return payments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payments createUpdatedEntity(EntityManager em) {
        Payments payments = new Payments()
            .notes(UPDATED_NOTES)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .cashAmount(UPDATED_CASH_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .creditCardAmount(UPDATED_CREDIT_CARD_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);
        return payments;
    }

    @BeforeEach
    public void initTest() {
        payments = createEntity(em);
    }

    @Test
    @Transactional
    void createPayments() throws Exception {
        int databaseSizeBeforeCreate = paymentsRepository.findAll().size();
        // Create the Payments
        restPaymentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isCreated());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeCreate + 1);
        Payments testPayments = paymentsList.get(paymentsList.size() - 1);
        assertThat(testPayments.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testPayments.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testPayments.getCashAmount()).isEqualTo(DEFAULT_CASH_AMOUNT);
        assertThat(testPayments.getCreditAmount()).isEqualTo(DEFAULT_CREDIT_AMOUNT);
        assertThat(testPayments.getCreditCardAmount()).isEqualTo(DEFAULT_CREDIT_CARD_AMOUNT);
        assertThat(testPayments.getTransactionID()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testPayments.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testPayments.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void createPaymentsWithExistingId() throws Exception {
        // Create the Payments with an existing ID
        payments.setId(1L);

        int databaseSizeBeforeCreate = paymentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isBadRequest());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTransactionIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentsRepository.findAll().size();
        // set the field null
        payments.setTransactionID(null);

        // Create the Payments, which fails.

        restPaymentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isBadRequest());

        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentsRepository.findAll().size();
        // set the field null
        payments.setLocationCode(null);

        // Create the Payments, which fails.

        restPaymentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isBadRequest());

        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTenantCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentsRepository.findAll().size();
        // set the field null
        payments.setTenantCode(null);

        // Create the Payments, which fails.

        restPaymentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isBadRequest());

        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList
        restPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payments.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].cashAmount").value(hasItem(DEFAULT_CASH_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(DEFAULT_CREDIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].creditCardAmount").value(hasItem(DEFAULT_CREDIT_CARD_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionID").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));
    }

    @Test
    @Transactional
    void getPayments() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get the payments
        restPaymentsMockMvc
            .perform(get(ENTITY_API_URL_ID, payments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payments.getId().intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.cashAmount").value(DEFAULT_CASH_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.creditAmount").value(DEFAULT_CREDIT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.creditCardAmount").value(DEFAULT_CREDIT_CARD_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.transactionID").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.locationCode").value(DEFAULT_LOCATION_CODE))
            .andExpect(jsonPath("$.tenantCode").value(DEFAULT_TENANT_CODE));
    }

    @Test
    @Transactional
    void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        Long id = payments.getId();

        defaultPaymentsShouldBeFound("id.equals=" + id);
        defaultPaymentsShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentsShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where notes equals to DEFAULT_NOTES
        defaultPaymentsShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the paymentsList where notes equals to UPDATED_NOTES
        defaultPaymentsShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPaymentsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultPaymentsShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the paymentsList where notes equals to UPDATED_NOTES
        defaultPaymentsShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPaymentsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where notes is not null
        defaultPaymentsShouldBeFound("notes.specified=true");

        // Get all the paymentsList where notes is null
        defaultPaymentsShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByNotesContainsSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where notes contains DEFAULT_NOTES
        defaultPaymentsShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the paymentsList where notes contains UPDATED_NOTES
        defaultPaymentsShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPaymentsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where notes does not contain DEFAULT_NOTES
        defaultPaymentsShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the paymentsList where notes does not contain UPDATED_NOTES
        defaultPaymentsShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultPaymentsShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the paymentsList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultPaymentsShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultPaymentsShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the paymentsList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultPaymentsShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where transactionDate is not null
        defaultPaymentsShouldBeFound("transactionDate.specified=true");

        // Get all the paymentsList where transactionDate is null
        defaultPaymentsShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCashAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where cashAmount equals to DEFAULT_CASH_AMOUNT
        defaultPaymentsShouldBeFound("cashAmount.equals=" + DEFAULT_CASH_AMOUNT);

        // Get all the paymentsList where cashAmount equals to UPDATED_CASH_AMOUNT
        defaultPaymentsShouldNotBeFound("cashAmount.equals=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCashAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where cashAmount in DEFAULT_CASH_AMOUNT or UPDATED_CASH_AMOUNT
        defaultPaymentsShouldBeFound("cashAmount.in=" + DEFAULT_CASH_AMOUNT + "," + UPDATED_CASH_AMOUNT);

        // Get all the paymentsList where cashAmount equals to UPDATED_CASH_AMOUNT
        defaultPaymentsShouldNotBeFound("cashAmount.in=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCashAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where cashAmount is not null
        defaultPaymentsShouldBeFound("cashAmount.specified=true");

        // Get all the paymentsList where cashAmount is null
        defaultPaymentsShouldNotBeFound("cashAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCashAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where cashAmount is greater than or equal to DEFAULT_CASH_AMOUNT
        defaultPaymentsShouldBeFound("cashAmount.greaterThanOrEqual=" + DEFAULT_CASH_AMOUNT);

        // Get all the paymentsList where cashAmount is greater than or equal to UPDATED_CASH_AMOUNT
        defaultPaymentsShouldNotBeFound("cashAmount.greaterThanOrEqual=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCashAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where cashAmount is less than or equal to DEFAULT_CASH_AMOUNT
        defaultPaymentsShouldBeFound("cashAmount.lessThanOrEqual=" + DEFAULT_CASH_AMOUNT);

        // Get all the paymentsList where cashAmount is less than or equal to SMALLER_CASH_AMOUNT
        defaultPaymentsShouldNotBeFound("cashAmount.lessThanOrEqual=" + SMALLER_CASH_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCashAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where cashAmount is less than DEFAULT_CASH_AMOUNT
        defaultPaymentsShouldNotBeFound("cashAmount.lessThan=" + DEFAULT_CASH_AMOUNT);

        // Get all the paymentsList where cashAmount is less than UPDATED_CASH_AMOUNT
        defaultPaymentsShouldBeFound("cashAmount.lessThan=" + UPDATED_CASH_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCashAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where cashAmount is greater than DEFAULT_CASH_AMOUNT
        defaultPaymentsShouldNotBeFound("cashAmount.greaterThan=" + DEFAULT_CASH_AMOUNT);

        // Get all the paymentsList where cashAmount is greater than SMALLER_CASH_AMOUNT
        defaultPaymentsShouldBeFound("cashAmount.greaterThan=" + SMALLER_CASH_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditAmount equals to DEFAULT_CREDIT_AMOUNT
        defaultPaymentsShouldBeFound("creditAmount.equals=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the paymentsList where creditAmount equals to UPDATED_CREDIT_AMOUNT
        defaultPaymentsShouldNotBeFound("creditAmount.equals=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditAmount in DEFAULT_CREDIT_AMOUNT or UPDATED_CREDIT_AMOUNT
        defaultPaymentsShouldBeFound("creditAmount.in=" + DEFAULT_CREDIT_AMOUNT + "," + UPDATED_CREDIT_AMOUNT);

        // Get all the paymentsList where creditAmount equals to UPDATED_CREDIT_AMOUNT
        defaultPaymentsShouldNotBeFound("creditAmount.in=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditAmount is not null
        defaultPaymentsShouldBeFound("creditAmount.specified=true");

        // Get all the paymentsList where creditAmount is null
        defaultPaymentsShouldNotBeFound("creditAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditAmount is greater than or equal to DEFAULT_CREDIT_AMOUNT
        defaultPaymentsShouldBeFound("creditAmount.greaterThanOrEqual=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the paymentsList where creditAmount is greater than or equal to UPDATED_CREDIT_AMOUNT
        defaultPaymentsShouldNotBeFound("creditAmount.greaterThanOrEqual=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditAmount is less than or equal to DEFAULT_CREDIT_AMOUNT
        defaultPaymentsShouldBeFound("creditAmount.lessThanOrEqual=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the paymentsList where creditAmount is less than or equal to SMALLER_CREDIT_AMOUNT
        defaultPaymentsShouldNotBeFound("creditAmount.lessThanOrEqual=" + SMALLER_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditAmount is less than DEFAULT_CREDIT_AMOUNT
        defaultPaymentsShouldNotBeFound("creditAmount.lessThan=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the paymentsList where creditAmount is less than UPDATED_CREDIT_AMOUNT
        defaultPaymentsShouldBeFound("creditAmount.lessThan=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditAmount is greater than DEFAULT_CREDIT_AMOUNT
        defaultPaymentsShouldNotBeFound("creditAmount.greaterThan=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the paymentsList where creditAmount is greater than SMALLER_CREDIT_AMOUNT
        defaultPaymentsShouldBeFound("creditAmount.greaterThan=" + SMALLER_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditCardAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditCardAmount equals to DEFAULT_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldBeFound("creditCardAmount.equals=" + DEFAULT_CREDIT_CARD_AMOUNT);

        // Get all the paymentsList where creditCardAmount equals to UPDATED_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldNotBeFound("creditCardAmount.equals=" + UPDATED_CREDIT_CARD_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditCardAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditCardAmount in DEFAULT_CREDIT_CARD_AMOUNT or UPDATED_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldBeFound("creditCardAmount.in=" + DEFAULT_CREDIT_CARD_AMOUNT + "," + UPDATED_CREDIT_CARD_AMOUNT);

        // Get all the paymentsList where creditCardAmount equals to UPDATED_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldNotBeFound("creditCardAmount.in=" + UPDATED_CREDIT_CARD_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditCardAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditCardAmount is not null
        defaultPaymentsShouldBeFound("creditCardAmount.specified=true");

        // Get all the paymentsList where creditCardAmount is null
        defaultPaymentsShouldNotBeFound("creditCardAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditCardAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditCardAmount is greater than or equal to DEFAULT_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldBeFound("creditCardAmount.greaterThanOrEqual=" + DEFAULT_CREDIT_CARD_AMOUNT);

        // Get all the paymentsList where creditCardAmount is greater than or equal to UPDATED_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldNotBeFound("creditCardAmount.greaterThanOrEqual=" + UPDATED_CREDIT_CARD_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditCardAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditCardAmount is less than or equal to DEFAULT_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldBeFound("creditCardAmount.lessThanOrEqual=" + DEFAULT_CREDIT_CARD_AMOUNT);

        // Get all the paymentsList where creditCardAmount is less than or equal to SMALLER_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldNotBeFound("creditCardAmount.lessThanOrEqual=" + SMALLER_CREDIT_CARD_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditCardAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditCardAmount is less than DEFAULT_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldNotBeFound("creditCardAmount.lessThan=" + DEFAULT_CREDIT_CARD_AMOUNT);

        // Get all the paymentsList where creditCardAmount is less than UPDATED_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldBeFound("creditCardAmount.lessThan=" + UPDATED_CREDIT_CARD_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreditCardAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where creditCardAmount is greater than DEFAULT_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldNotBeFound("creditCardAmount.greaterThan=" + DEFAULT_CREDIT_CARD_AMOUNT);

        // Get all the paymentsList where creditCardAmount is greater than SMALLER_CREDIT_CARD_AMOUNT
        defaultPaymentsShouldBeFound("creditCardAmount.greaterThan=" + SMALLER_CREDIT_CARD_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIDIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where transactionID equals to DEFAULT_TRANSACTION_ID
        defaultPaymentsShouldBeFound("transactionID.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the paymentsList where transactionID equals to UPDATED_TRANSACTION_ID
        defaultPaymentsShouldNotBeFound("transactionID.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIDIsInShouldWork() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where transactionID in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultPaymentsShouldBeFound("transactionID.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the paymentsList where transactionID equals to UPDATED_TRANSACTION_ID
        defaultPaymentsShouldNotBeFound("transactionID.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where transactionID is not null
        defaultPaymentsShouldBeFound("transactionID.specified=true");

        // Get all the paymentsList where transactionID is null
        defaultPaymentsShouldNotBeFound("transactionID.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIDContainsSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where transactionID contains DEFAULT_TRANSACTION_ID
        defaultPaymentsShouldBeFound("transactionID.contains=" + DEFAULT_TRANSACTION_ID);

        // Get all the paymentsList where transactionID contains UPDATED_TRANSACTION_ID
        defaultPaymentsShouldNotBeFound("transactionID.contains=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionIDNotContainsSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where transactionID does not contain DEFAULT_TRANSACTION_ID
        defaultPaymentsShouldNotBeFound("transactionID.doesNotContain=" + DEFAULT_TRANSACTION_ID);

        // Get all the paymentsList where transactionID does not contain UPDATED_TRANSACTION_ID
        defaultPaymentsShouldBeFound("transactionID.doesNotContain=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByLocationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where locationCode equals to DEFAULT_LOCATION_CODE
        defaultPaymentsShouldBeFound("locationCode.equals=" + DEFAULT_LOCATION_CODE);

        // Get all the paymentsList where locationCode equals to UPDATED_LOCATION_CODE
        defaultPaymentsShouldNotBeFound("locationCode.equals=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentsByLocationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where locationCode in DEFAULT_LOCATION_CODE or UPDATED_LOCATION_CODE
        defaultPaymentsShouldBeFound("locationCode.in=" + DEFAULT_LOCATION_CODE + "," + UPDATED_LOCATION_CODE);

        // Get all the paymentsList where locationCode equals to UPDATED_LOCATION_CODE
        defaultPaymentsShouldNotBeFound("locationCode.in=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentsByLocationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where locationCode is not null
        defaultPaymentsShouldBeFound("locationCode.specified=true");

        // Get all the paymentsList where locationCode is null
        defaultPaymentsShouldNotBeFound("locationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByLocationCodeContainsSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where locationCode contains DEFAULT_LOCATION_CODE
        defaultPaymentsShouldBeFound("locationCode.contains=" + DEFAULT_LOCATION_CODE);

        // Get all the paymentsList where locationCode contains UPDATED_LOCATION_CODE
        defaultPaymentsShouldNotBeFound("locationCode.contains=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentsByLocationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where locationCode does not contain DEFAULT_LOCATION_CODE
        defaultPaymentsShouldNotBeFound("locationCode.doesNotContain=" + DEFAULT_LOCATION_CODE);

        // Get all the paymentsList where locationCode does not contain UPDATED_LOCATION_CODE
        defaultPaymentsShouldBeFound("locationCode.doesNotContain=" + UPDATED_LOCATION_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentsByTenantCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where tenantCode equals to DEFAULT_TENANT_CODE
        defaultPaymentsShouldBeFound("tenantCode.equals=" + DEFAULT_TENANT_CODE);

        // Get all the paymentsList where tenantCode equals to UPDATED_TENANT_CODE
        defaultPaymentsShouldNotBeFound("tenantCode.equals=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentsByTenantCodeIsInShouldWork() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where tenantCode in DEFAULT_TENANT_CODE or UPDATED_TENANT_CODE
        defaultPaymentsShouldBeFound("tenantCode.in=" + DEFAULT_TENANT_CODE + "," + UPDATED_TENANT_CODE);

        // Get all the paymentsList where tenantCode equals to UPDATED_TENANT_CODE
        defaultPaymentsShouldNotBeFound("tenantCode.in=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentsByTenantCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where tenantCode is not null
        defaultPaymentsShouldBeFound("tenantCode.specified=true");

        // Get all the paymentsList where tenantCode is null
        defaultPaymentsShouldNotBeFound("tenantCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByTenantCodeContainsSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where tenantCode contains DEFAULT_TENANT_CODE
        defaultPaymentsShouldBeFound("tenantCode.contains=" + DEFAULT_TENANT_CODE);

        // Get all the paymentsList where tenantCode contains UPDATED_TENANT_CODE
        defaultPaymentsShouldNotBeFound("tenantCode.contains=" + UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentsByTenantCodeNotContainsSomething() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList where tenantCode does not contain DEFAULT_TENANT_CODE
        defaultPaymentsShouldNotBeFound("tenantCode.doesNotContain=" + DEFAULT_TENANT_CODE);

        // Get all the paymentsList where tenantCode does not contain UPDATED_TENANT_CODE
        defaultPaymentsShouldBeFound("tenantCode.doesNotContain=" + UPDATED_TENANT_CODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentsShouldBeFound(String filter) throws Exception {
        restPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payments.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].cashAmount").value(hasItem(DEFAULT_CASH_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(DEFAULT_CREDIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].creditCardAmount").value(hasItem(DEFAULT_CREDIT_CARD_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionID").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].locationCode").value(hasItem(DEFAULT_LOCATION_CODE)))
            .andExpect(jsonPath("$.[*].tenantCode").value(hasItem(DEFAULT_TENANT_CODE)));

        // Check, that the count call also returns 1
        restPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentsShouldNotBeFound(String filter) throws Exception {
        restPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayments() throws Exception {
        // Get the payments
        restPaymentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayments() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();

        // Update the payments
        Payments updatedPayments = paymentsRepository.findById(payments.getId()).get();
        // Disconnect from session so that the updates on updatedPayments are not directly saved in db
        em.detach(updatedPayments);
        updatedPayments
            .notes(UPDATED_NOTES)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .cashAmount(UPDATED_CASH_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .creditCardAmount(UPDATED_CREDIT_CARD_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);

        restPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPayments))
            )
            .andExpect(status().isOk());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
        Payments testPayments = paymentsList.get(paymentsList.size() - 1);
        assertThat(testPayments.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPayments.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPayments.getCashAmount()).isEqualTo(UPDATED_CASH_AMOUNT);
        assertThat(testPayments.getCreditAmount()).isEqualTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testPayments.getCreditCardAmount()).isEqualTo(UPDATED_CREDIT_CARD_AMOUNT);
        assertThat(testPayments.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testPayments.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testPayments.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void putNonExistingPayments() throws Exception {
        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();
        payments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayments() throws Exception {
        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();
        payments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayments() throws Exception {
        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();
        payments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentsWithPatch() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();

        // Update the payments using partial update
        Payments partialUpdatedPayments = new Payments();
        partialUpdatedPayments.setId(payments.getId());

        partialUpdatedPayments
            .notes(UPDATED_NOTES)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .creditCardAmount(UPDATED_CREDIT_CARD_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID);

        restPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayments))
            )
            .andExpect(status().isOk());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
        Payments testPayments = paymentsList.get(paymentsList.size() - 1);
        assertThat(testPayments.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPayments.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPayments.getCashAmount()).isEqualTo(DEFAULT_CASH_AMOUNT);
        assertThat(testPayments.getCreditAmount()).isEqualTo(DEFAULT_CREDIT_AMOUNT);
        assertThat(testPayments.getCreditCardAmount()).isEqualTo(UPDATED_CREDIT_CARD_AMOUNT);
        assertThat(testPayments.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testPayments.getLocationCode()).isEqualTo(DEFAULT_LOCATION_CODE);
        assertThat(testPayments.getTenantCode()).isEqualTo(DEFAULT_TENANT_CODE);
    }

    @Test
    @Transactional
    void fullUpdatePaymentsWithPatch() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();

        // Update the payments using partial update
        Payments partialUpdatedPayments = new Payments();
        partialUpdatedPayments.setId(payments.getId());

        partialUpdatedPayments
            .notes(UPDATED_NOTES)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .cashAmount(UPDATED_CASH_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .creditCardAmount(UPDATED_CREDIT_CARD_AMOUNT)
            .transactionID(UPDATED_TRANSACTION_ID)
            .locationCode(UPDATED_LOCATION_CODE)
            .tenantCode(UPDATED_TENANT_CODE);

        restPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayments))
            )
            .andExpect(status().isOk());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
        Payments testPayments = paymentsList.get(paymentsList.size() - 1);
        assertThat(testPayments.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPayments.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPayments.getCashAmount()).isEqualTo(UPDATED_CASH_AMOUNT);
        assertThat(testPayments.getCreditAmount()).isEqualTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testPayments.getCreditCardAmount()).isEqualTo(UPDATED_CREDIT_CARD_AMOUNT);
        assertThat(testPayments.getTransactionID()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testPayments.getLocationCode()).isEqualTo(UPDATED_LOCATION_CODE);
        assertThat(testPayments.getTenantCode()).isEqualTo(UPDATED_TENANT_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingPayments() throws Exception {
        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();
        payments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayments() throws Exception {
        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();
        payments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayments() throws Exception {
        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();
        payments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayments() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        int databaseSizeBeforeDelete = paymentsRepository.findAll().size();

        // Delete the payments
        restPaymentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, payments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
