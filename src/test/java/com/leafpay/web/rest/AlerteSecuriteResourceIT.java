package com.leafpay.web.rest;

import static com.leafpay.domain.AlerteSecuriteAsserts.*;
import static com.leafpay.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leafpay.IntegrationTest;
import com.leafpay.domain.AlerteSecurite;
import com.leafpay.repository.AlerteSecuriteRepository;
import com.leafpay.service.dto.AlerteSecuriteDTO;
import com.leafpay.service.mapper.AlerteSecuriteMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AlerteSecuriteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlerteSecuriteResourceIT {

    private static final String DEFAULT_TYPE_ALERTE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_ALERTE = "BBBBBBBBBB";

    private static final String DEFAULT_NIVEAU_SEVERITE = "AAAAAAAAAA";
    private static final String UPDATED_NIVEAU_SEVERITE = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_EST_TRAITEE = false;
    private static final Boolean UPDATED_EST_TRAITEE = true;

    private static final String ENTITY_API_URL = "/api/alerte-securites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlerteSecuriteRepository alerteSecuriteRepository;

    @Autowired
    private AlerteSecuriteMapper alerteSecuriteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlerteSecuriteMockMvc;

    private AlerteSecurite alerteSecurite;

    private AlerteSecurite insertedAlerteSecurite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlerteSecurite createEntity() {
        return new AlerteSecurite()
            .typeAlerte(DEFAULT_TYPE_ALERTE)
            .niveauSeverite(DEFAULT_NIVEAU_SEVERITE)
            .timestamp(DEFAULT_TIMESTAMP)
            .estTraitee(DEFAULT_EST_TRAITEE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlerteSecurite createUpdatedEntity() {
        return new AlerteSecurite()
            .typeAlerte(UPDATED_TYPE_ALERTE)
            .niveauSeverite(UPDATED_NIVEAU_SEVERITE)
            .timestamp(UPDATED_TIMESTAMP)
            .estTraitee(UPDATED_EST_TRAITEE);
    }

    @BeforeEach
    void initTest() {
        alerteSecurite = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlerteSecurite != null) {
            alerteSecuriteRepository.delete(insertedAlerteSecurite);
            insertedAlerteSecurite = null;
        }
    }

    @Test
    @Transactional
    void createAlerteSecurite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlerteSecurite
        AlerteSecuriteDTO alerteSecuriteDTO = alerteSecuriteMapper.toDto(alerteSecurite);
        var returnedAlerteSecuriteDTO = om.readValue(
            restAlerteSecuriteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteSecuriteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlerteSecuriteDTO.class
        );

        // Validate the AlerteSecurite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlerteSecurite = alerteSecuriteMapper.toEntity(returnedAlerteSecuriteDTO);
        assertAlerteSecuriteUpdatableFieldsEquals(returnedAlerteSecurite, getPersistedAlerteSecurite(returnedAlerteSecurite));

        insertedAlerteSecurite = returnedAlerteSecurite;
    }

    @Test
    @Transactional
    void createAlerteSecuriteWithExistingId() throws Exception {
        // Create the AlerteSecurite with an existing ID
        alerteSecurite.setId(1L);
        AlerteSecuriteDTO alerteSecuriteDTO = alerteSecuriteMapper.toDto(alerteSecurite);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlerteSecuriteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteSecuriteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlerteSecurite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlerteSecurites() throws Exception {
        // Initialize the database
        insertedAlerteSecurite = alerteSecuriteRepository.saveAndFlush(alerteSecurite);

        // Get all the alerteSecuriteList
        restAlerteSecuriteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerteSecurite.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeAlerte").value(hasItem(DEFAULT_TYPE_ALERTE)))
            .andExpect(jsonPath("$.[*].niveauSeverite").value(hasItem(DEFAULT_NIVEAU_SEVERITE)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].estTraitee").value(hasItem(DEFAULT_EST_TRAITEE)));
    }

    @Test
    @Transactional
    void getAlerteSecurite() throws Exception {
        // Initialize the database
        insertedAlerteSecurite = alerteSecuriteRepository.saveAndFlush(alerteSecurite);

        // Get the alerteSecurite
        restAlerteSecuriteMockMvc
            .perform(get(ENTITY_API_URL_ID, alerteSecurite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alerteSecurite.getId().intValue()))
            .andExpect(jsonPath("$.typeAlerte").value(DEFAULT_TYPE_ALERTE))
            .andExpect(jsonPath("$.niveauSeverite").value(DEFAULT_NIVEAU_SEVERITE))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.estTraitee").value(DEFAULT_EST_TRAITEE));
    }

    @Test
    @Transactional
    void getNonExistingAlerteSecurite() throws Exception {
        // Get the alerteSecurite
        restAlerteSecuriteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlerteSecurite() throws Exception {
        // Initialize the database
        insertedAlerteSecurite = alerteSecuriteRepository.saveAndFlush(alerteSecurite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerteSecurite
        AlerteSecurite updatedAlerteSecurite = alerteSecuriteRepository.findById(alerteSecurite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlerteSecurite are not directly saved in db
        em.detach(updatedAlerteSecurite);
        updatedAlerteSecurite
            .typeAlerte(UPDATED_TYPE_ALERTE)
            .niveauSeverite(UPDATED_NIVEAU_SEVERITE)
            .timestamp(UPDATED_TIMESTAMP)
            .estTraitee(UPDATED_EST_TRAITEE);
        AlerteSecuriteDTO alerteSecuriteDTO = alerteSecuriteMapper.toDto(updatedAlerteSecurite);

        restAlerteSecuriteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alerteSecuriteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteSecuriteDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlerteSecurite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlerteSecuriteToMatchAllProperties(updatedAlerteSecurite);
    }

    @Test
    @Transactional
    void putNonExistingAlerteSecurite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteSecurite.setId(longCount.incrementAndGet());

        // Create the AlerteSecurite
        AlerteSecuriteDTO alerteSecuriteDTO = alerteSecuriteMapper.toDto(alerteSecurite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteSecuriteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alerteSecuriteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteSecuriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteSecurite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlerteSecurite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteSecurite.setId(longCount.incrementAndGet());

        // Create the AlerteSecurite
        AlerteSecuriteDTO alerteSecuriteDTO = alerteSecuriteMapper.toDto(alerteSecurite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteSecuriteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteSecuriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteSecurite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlerteSecurite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteSecurite.setId(longCount.incrementAndGet());

        // Create the AlerteSecurite
        AlerteSecuriteDTO alerteSecuriteDTO = alerteSecuriteMapper.toDto(alerteSecurite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteSecuriteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteSecuriteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlerteSecurite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlerteSecuriteWithPatch() throws Exception {
        // Initialize the database
        insertedAlerteSecurite = alerteSecuriteRepository.saveAndFlush(alerteSecurite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerteSecurite using partial update
        AlerteSecurite partialUpdatedAlerteSecurite = new AlerteSecurite();
        partialUpdatedAlerteSecurite.setId(alerteSecurite.getId());

        partialUpdatedAlerteSecurite.niveauSeverite(UPDATED_NIVEAU_SEVERITE).estTraitee(UPDATED_EST_TRAITEE);

        restAlerteSecuriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerteSecurite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerteSecurite))
            )
            .andExpect(status().isOk());

        // Validate the AlerteSecurite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteSecuriteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlerteSecurite, alerteSecurite),
            getPersistedAlerteSecurite(alerteSecurite)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlerteSecuriteWithPatch() throws Exception {
        // Initialize the database
        insertedAlerteSecurite = alerteSecuriteRepository.saveAndFlush(alerteSecurite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerteSecurite using partial update
        AlerteSecurite partialUpdatedAlerteSecurite = new AlerteSecurite();
        partialUpdatedAlerteSecurite.setId(alerteSecurite.getId());

        partialUpdatedAlerteSecurite
            .typeAlerte(UPDATED_TYPE_ALERTE)
            .niveauSeverite(UPDATED_NIVEAU_SEVERITE)
            .timestamp(UPDATED_TIMESTAMP)
            .estTraitee(UPDATED_EST_TRAITEE);

        restAlerteSecuriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerteSecurite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerteSecurite))
            )
            .andExpect(status().isOk());

        // Validate the AlerteSecurite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteSecuriteUpdatableFieldsEquals(partialUpdatedAlerteSecurite, getPersistedAlerteSecurite(partialUpdatedAlerteSecurite));
    }

    @Test
    @Transactional
    void patchNonExistingAlerteSecurite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteSecurite.setId(longCount.incrementAndGet());

        // Create the AlerteSecurite
        AlerteSecuriteDTO alerteSecuriteDTO = alerteSecuriteMapper.toDto(alerteSecurite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteSecuriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alerteSecuriteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alerteSecuriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteSecurite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlerteSecurite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteSecurite.setId(longCount.incrementAndGet());

        // Create the AlerteSecurite
        AlerteSecuriteDTO alerteSecuriteDTO = alerteSecuriteMapper.toDto(alerteSecurite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteSecuriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alerteSecuriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteSecurite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlerteSecurite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteSecurite.setId(longCount.incrementAndGet());

        // Create the AlerteSecurite
        AlerteSecuriteDTO alerteSecuriteDTO = alerteSecuriteMapper.toDto(alerteSecurite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteSecuriteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alerteSecuriteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlerteSecurite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlerteSecurite() throws Exception {
        // Initialize the database
        insertedAlerteSecurite = alerteSecuriteRepository.saveAndFlush(alerteSecurite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alerteSecurite
        restAlerteSecuriteMockMvc
            .perform(delete(ENTITY_API_URL_ID, alerteSecurite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alerteSecuriteRepository.count();
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

    protected AlerteSecurite getPersistedAlerteSecurite(AlerteSecurite alerteSecurite) {
        return alerteSecuriteRepository.findById(alerteSecurite.getId()).orElseThrow();
    }

    protected void assertPersistedAlerteSecuriteToMatchAllProperties(AlerteSecurite expectedAlerteSecurite) {
        assertAlerteSecuriteAllPropertiesEquals(expectedAlerteSecurite, getPersistedAlerteSecurite(expectedAlerteSecurite));
    }

    protected void assertPersistedAlerteSecuriteToMatchUpdatableProperties(AlerteSecurite expectedAlerteSecurite) {
        assertAlerteSecuriteAllUpdatablePropertiesEquals(expectedAlerteSecurite, getPersistedAlerteSecurite(expectedAlerteSecurite));
    }
}
