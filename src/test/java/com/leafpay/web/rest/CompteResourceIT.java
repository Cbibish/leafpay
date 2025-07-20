package com.leafpay.web.rest;

import static com.leafpay.domain.CompteAsserts.*;
import static com.leafpay.web.rest.TestUtil.createUpdateProxyForBean;
import static com.leafpay.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leafpay.IntegrationTest;
import com.leafpay.domain.Compte;
import com.leafpay.repository.CompteRepository;
import com.leafpay.service.dto.CompteDTO;
import com.leafpay.service.mapper.CompteMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompteResourceIT {

    private static final String DEFAULT_TYPE_COMPTE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_COMPTE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SOLDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SOLDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PLAFOND_TRANSACTION = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLAFOND_TRANSACTION = new BigDecimal(2);

    private static final Integer DEFAULT_LIMITE_RETRAITS_MENSUELS = 1;
    private static final Integer UPDATED_LIMITE_RETRAITS_MENSUELS = 2;

    private static final BigDecimal DEFAULT_TAUX_INTERET = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAUX_INTERET = new BigDecimal(2);

    private static final Instant DEFAULT_DATE_OUVERTURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OUVERTURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FERMETURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FERMETURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/comptes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private CompteMapper compteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteMockMvc;

    private Compte compte;

    private Compte insertedCompte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compte createEntity() {
        return new Compte()
            .typeCompte(DEFAULT_TYPE_COMPTE)
            .solde(DEFAULT_SOLDE)
            .plafondTransaction(DEFAULT_PLAFOND_TRANSACTION)
            .limiteRetraitsMensuels(DEFAULT_LIMITE_RETRAITS_MENSUELS)
            .tauxInteret(DEFAULT_TAUX_INTERET)
            .dateOuverture(DEFAULT_DATE_OUVERTURE)
            .dateFermeture(DEFAULT_DATE_FERMETURE)
            .statut(DEFAULT_STATUT)
            .iban(DEFAULT_IBAN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compte createUpdatedEntity() {
        return new Compte()
            .typeCompte(UPDATED_TYPE_COMPTE)
            .solde(UPDATED_SOLDE)
            .plafondTransaction(UPDATED_PLAFOND_TRANSACTION)
            .limiteRetraitsMensuels(UPDATED_LIMITE_RETRAITS_MENSUELS)
            .tauxInteret(UPDATED_TAUX_INTERET)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateFermeture(UPDATED_DATE_FERMETURE)
            .statut(UPDATED_STATUT)
            .iban(UPDATED_IBAN);
    }

    @BeforeEach
    void initTest() {
        compte = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompte != null) {
            compteRepository.delete(insertedCompte);
            insertedCompte = null;
        }
    }

    @Test
    @Transactional
    void createCompte() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);
        var returnedCompteDTO = om.readValue(
            restCompteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompteDTO.class
        );

        // Validate the Compte in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompte = compteMapper.toEntity(returnedCompteDTO);
        assertCompteUpdatableFieldsEquals(returnedCompte, getPersistedCompte(returnedCompte));

        insertedCompte = returnedCompte;
    }

    @Test
    @Transactional
    void createCompteWithExistingId() throws Exception {
        // Create the Compte with an existing ID
        compte.setId(1L);
        CompteDTO compteDTO = compteMapper.toDto(compte);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllComptes() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        // Get all the compteList
        restCompteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeCompte").value(hasItem(DEFAULT_TYPE_COMPTE)))
            .andExpect(jsonPath("$.[*].solde").value(hasItem(sameNumber(DEFAULT_SOLDE))))
            .andExpect(jsonPath("$.[*].plafondTransaction").value(hasItem(sameNumber(DEFAULT_PLAFOND_TRANSACTION))))
            .andExpect(jsonPath("$.[*].limiteRetraitsMensuels").value(hasItem(DEFAULT_LIMITE_RETRAITS_MENSUELS)))
            .andExpect(jsonPath("$.[*].tauxInteret").value(hasItem(sameNumber(DEFAULT_TAUX_INTERET))))
            .andExpect(jsonPath("$.[*].dateOuverture").value(hasItem(DEFAULT_DATE_OUVERTURE.toString())))
            .andExpect(jsonPath("$.[*].dateFermeture").value(hasItem(DEFAULT_DATE_FERMETURE.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)));
    }

    @Test
    @Transactional
    void getCompte() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        // Get the compte
        restCompteMockMvc
            .perform(get(ENTITY_API_URL_ID, compte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compte.getId().intValue()))
            .andExpect(jsonPath("$.typeCompte").value(DEFAULT_TYPE_COMPTE))
            .andExpect(jsonPath("$.solde").value(sameNumber(DEFAULT_SOLDE)))
            .andExpect(jsonPath("$.plafondTransaction").value(sameNumber(DEFAULT_PLAFOND_TRANSACTION)))
            .andExpect(jsonPath("$.limiteRetraitsMensuels").value(DEFAULT_LIMITE_RETRAITS_MENSUELS))
            .andExpect(jsonPath("$.tauxInteret").value(sameNumber(DEFAULT_TAUX_INTERET)))
            .andExpect(jsonPath("$.dateOuverture").value(DEFAULT_DATE_OUVERTURE.toString()))
            .andExpect(jsonPath("$.dateFermeture").value(DEFAULT_DATE_FERMETURE.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN));
    }

    @Test
    @Transactional
    void getNonExistingCompte() throws Exception {
        // Get the compte
        restCompteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompte() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compte
        Compte updatedCompte = compteRepository.findById(compte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompte are not directly saved in db
        em.detach(updatedCompte);
        updatedCompte
            .typeCompte(UPDATED_TYPE_COMPTE)
            .solde(UPDATED_SOLDE)
            .plafondTransaction(UPDATED_PLAFOND_TRANSACTION)
            .limiteRetraitsMensuels(UPDATED_LIMITE_RETRAITS_MENSUELS)
            .tauxInteret(UPDATED_TAUX_INTERET)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateFermeture(UPDATED_DATE_FERMETURE)
            .statut(UPDATED_STATUT)
            .iban(UPDATED_IBAN);
        CompteDTO compteDTO = compteMapper.toDto(updatedCompte);

        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompteToMatchAllProperties(updatedCompte);
    }

    @Test
    @Transactional
    void putNonExistingCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteWithPatch() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compte using partial update
        Compte partialUpdatedCompte = new Compte();
        partialUpdatedCompte.setId(compte.getId());

        partialUpdatedCompte
            .typeCompte(UPDATED_TYPE_COMPTE)
            .solde(UPDATED_SOLDE)
            .limiteRetraitsMensuels(UPDATED_LIMITE_RETRAITS_MENSUELS)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateFermeture(UPDATED_DATE_FERMETURE)
            .statut(UPDATED_STATUT);

        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompte))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompte, compte), getPersistedCompte(compte));
    }

    @Test
    @Transactional
    void fullUpdateCompteWithPatch() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compte using partial update
        Compte partialUpdatedCompte = new Compte();
        partialUpdatedCompte.setId(compte.getId());

        partialUpdatedCompte
            .typeCompte(UPDATED_TYPE_COMPTE)
            .solde(UPDATED_SOLDE)
            .plafondTransaction(UPDATED_PLAFOND_TRANSACTION)
            .limiteRetraitsMensuels(UPDATED_LIMITE_RETRAITS_MENSUELS)
            .tauxInteret(UPDATED_TAUX_INTERET)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateFermeture(UPDATED_DATE_FERMETURE)
            .statut(UPDATED_STATUT)
            .iban(UPDATED_IBAN);

        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompte))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompteUpdatableFieldsEquals(partialUpdatedCompte, getPersistedCompte(partialUpdatedCompte));
    }

    @Test
    @Transactional
    void patchNonExistingCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompte() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compte
        restCompteMockMvc
            .perform(delete(ENTITY_API_URL_ID, compte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compteRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Compte getPersistedCompte(Compte compte) {
        return compteRepository.findById(compte.getId()).orElseThrow();
    }

    protected void assertPersistedCompteToMatchAllProperties(Compte expectedCompte) {
        assertCompteAllPropertiesEquals(expectedCompte, getPersistedCompte(expectedCompte));
    }

    protected void assertPersistedCompteToMatchUpdatableProperties(Compte expectedCompte) {
        assertCompteAllUpdatablePropertiesEquals(expectedCompte, getPersistedCompte(expectedCompte));
    }
}
