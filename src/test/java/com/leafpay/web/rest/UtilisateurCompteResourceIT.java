package com.leafpay.web.rest;

import static com.leafpay.domain.UtilisateurCompteAsserts.*;
import static com.leafpay.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leafpay.IntegrationTest;
import com.leafpay.domain.UtilisateurCompte;
import com.leafpay.repository.UtilisateurCompteRepository;
import com.leafpay.service.dto.UtilisateurCompteDTO;
import com.leafpay.service.mapper.UtilisateurCompteMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link UtilisateurCompteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UtilisateurCompteResourceIT {

    private static final String DEFAULT_ROLE_UTILISATEUR_SUR_CE_COMPTE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_UTILISATEUR_SUR_CE_COMPTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utilisateur-comptes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UtilisateurCompteRepository utilisateurCompteRepository;

    @Autowired
    private UtilisateurCompteMapper utilisateurCompteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilisateurCompteMockMvc;

    private UtilisateurCompte utilisateurCompte;

    private UtilisateurCompte insertedUtilisateurCompte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilisateurCompte createEntity() {
        return new UtilisateurCompte().roleUtilisateurSurCeCompte(DEFAULT_ROLE_UTILISATEUR_SUR_CE_COMPTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilisateurCompte createUpdatedEntity() {
        return new UtilisateurCompte().roleUtilisateurSurCeCompte(UPDATED_ROLE_UTILISATEUR_SUR_CE_COMPTE);
    }

    @BeforeEach
    void initTest() {
        utilisateurCompte = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUtilisateurCompte != null) {
            utilisateurCompteRepository.delete(insertedUtilisateurCompte);
            insertedUtilisateurCompte = null;
        }
    }

    @Test
    @Transactional
    void createUtilisateurCompte() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UtilisateurCompte
        UtilisateurCompteDTO utilisateurCompteDTO = utilisateurCompteMapper.toDto(utilisateurCompte);
        var returnedUtilisateurCompteDTO = om.readValue(
            restUtilisateurCompteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurCompteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UtilisateurCompteDTO.class
        );

        // Validate the UtilisateurCompte in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUtilisateurCompte = utilisateurCompteMapper.toEntity(returnedUtilisateurCompteDTO);
        assertUtilisateurCompteUpdatableFieldsEquals(returnedUtilisateurCompte, getPersistedUtilisateurCompte(returnedUtilisateurCompte));

        insertedUtilisateurCompte = returnedUtilisateurCompte;
    }

    @Test
    @Transactional
    void createUtilisateurCompteWithExistingId() throws Exception {
        // Create the UtilisateurCompte with an existing ID
        utilisateurCompte.setId(1L);
        UtilisateurCompteDTO utilisateurCompteDTO = utilisateurCompteMapper.toDto(utilisateurCompte);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilisateurCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurCompteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurCompte in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUtilisateurComptes() throws Exception {
        // Initialize the database
        insertedUtilisateurCompte = utilisateurCompteRepository.saveAndFlush(utilisateurCompte);

        // Get all the utilisateurCompteList
        restUtilisateurCompteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateurCompte.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleUtilisateurSurCeCompte").value(hasItem(DEFAULT_ROLE_UTILISATEUR_SUR_CE_COMPTE)));
    }

    @Test
    @Transactional
    void getUtilisateurCompte() throws Exception {
        // Initialize the database
        insertedUtilisateurCompte = utilisateurCompteRepository.saveAndFlush(utilisateurCompte);

        // Get the utilisateurCompte
        restUtilisateurCompteMockMvc
            .perform(get(ENTITY_API_URL_ID, utilisateurCompte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilisateurCompte.getId().intValue()))
            .andExpect(jsonPath("$.roleUtilisateurSurCeCompte").value(DEFAULT_ROLE_UTILISATEUR_SUR_CE_COMPTE));
    }

    @Test
    @Transactional
    void getNonExistingUtilisateurCompte() throws Exception {
        // Get the utilisateurCompte
        restUtilisateurCompteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUtilisateurCompte() throws Exception {
        // Initialize the database
        insertedUtilisateurCompte = utilisateurCompteRepository.saveAndFlush(utilisateurCompte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateurCompte
        UtilisateurCompte updatedUtilisateurCompte = utilisateurCompteRepository.findById(utilisateurCompte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUtilisateurCompte are not directly saved in db
        em.detach(updatedUtilisateurCompte);
        updatedUtilisateurCompte.roleUtilisateurSurCeCompte(UPDATED_ROLE_UTILISATEUR_SUR_CE_COMPTE);
        UtilisateurCompteDTO utilisateurCompteDTO = utilisateurCompteMapper.toDto(updatedUtilisateurCompte);

        restUtilisateurCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurCompteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurCompteDTO))
            )
            .andExpect(status().isOk());

        // Validate the UtilisateurCompte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUtilisateurCompteToMatchAllProperties(updatedUtilisateurCompte);
    }

    @Test
    @Transactional
    void putNonExistingUtilisateurCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateurCompte.setId(longCount.incrementAndGet());

        // Create the UtilisateurCompte
        UtilisateurCompteDTO utilisateurCompteDTO = utilisateurCompteMapper.toDto(utilisateurCompte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurCompteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurCompteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurCompte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilisateurCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateurCompte.setId(longCount.incrementAndGet());

        // Create the UtilisateurCompte
        UtilisateurCompteDTO utilisateurCompteDTO = utilisateurCompteMapper.toDto(utilisateurCompte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurCompteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurCompte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilisateurCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateurCompte.setId(longCount.incrementAndGet());

        // Create the UtilisateurCompte
        UtilisateurCompteDTO utilisateurCompteDTO = utilisateurCompteMapper.toDto(utilisateurCompte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurCompteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurCompteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilisateurCompte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilisateurCompteWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisateurCompte = utilisateurCompteRepository.saveAndFlush(utilisateurCompte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateurCompte using partial update
        UtilisateurCompte partialUpdatedUtilisateurCompte = new UtilisateurCompte();
        partialUpdatedUtilisateurCompte.setId(utilisateurCompte.getId());

        partialUpdatedUtilisateurCompte.roleUtilisateurSurCeCompte(UPDATED_ROLE_UTILISATEUR_SUR_CE_COMPTE);

        restUtilisateurCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateurCompte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisateurCompte))
            )
            .andExpect(status().isOk());

        // Validate the UtilisateurCompte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurCompteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUtilisateurCompte, utilisateurCompte),
            getPersistedUtilisateurCompte(utilisateurCompte)
        );
    }

    @Test
    @Transactional
    void fullUpdateUtilisateurCompteWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisateurCompte = utilisateurCompteRepository.saveAndFlush(utilisateurCompte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateurCompte using partial update
        UtilisateurCompte partialUpdatedUtilisateurCompte = new UtilisateurCompte();
        partialUpdatedUtilisateurCompte.setId(utilisateurCompte.getId());

        partialUpdatedUtilisateurCompte.roleUtilisateurSurCeCompte(UPDATED_ROLE_UTILISATEUR_SUR_CE_COMPTE);

        restUtilisateurCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateurCompte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisateurCompte))
            )
            .andExpect(status().isOk());

        // Validate the UtilisateurCompte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurCompteUpdatableFieldsEquals(
            partialUpdatedUtilisateurCompte,
            getPersistedUtilisateurCompte(partialUpdatedUtilisateurCompte)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUtilisateurCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateurCompte.setId(longCount.incrementAndGet());

        // Create the UtilisateurCompte
        UtilisateurCompteDTO utilisateurCompteDTO = utilisateurCompteMapper.toDto(utilisateurCompte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilisateurCompteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisateurCompteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurCompte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilisateurCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateurCompte.setId(longCount.incrementAndGet());

        // Create the UtilisateurCompte
        UtilisateurCompteDTO utilisateurCompteDTO = utilisateurCompteMapper.toDto(utilisateurCompte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisateurCompteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisateurCompte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilisateurCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateurCompte.setId(longCount.incrementAndGet());

        // Create the UtilisateurCompte
        UtilisateurCompteDTO utilisateurCompteDTO = utilisateurCompteMapper.toDto(utilisateurCompte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurCompteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(utilisateurCompteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilisateurCompte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilisateurCompte() throws Exception {
        // Initialize the database
        insertedUtilisateurCompte = utilisateurCompteRepository.saveAndFlush(utilisateurCompte);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the utilisateurCompte
        restUtilisateurCompteMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilisateurCompte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return utilisateurCompteRepository.count();
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

    protected UtilisateurCompte getPersistedUtilisateurCompte(UtilisateurCompte utilisateurCompte) {
        return utilisateurCompteRepository.findById(utilisateurCompte.getId()).orElseThrow();
    }

    protected void assertPersistedUtilisateurCompteToMatchAllProperties(UtilisateurCompte expectedUtilisateurCompte) {
        assertUtilisateurCompteAllPropertiesEquals(expectedUtilisateurCompte, getPersistedUtilisateurCompte(expectedUtilisateurCompte));
    }

    protected void assertPersistedUtilisateurCompteToMatchUpdatableProperties(UtilisateurCompte expectedUtilisateurCompte) {
        assertUtilisateurCompteAllUpdatablePropertiesEquals(
            expectedUtilisateurCompte,
            getPersistedUtilisateurCompte(expectedUtilisateurCompte)
        );
    }
}
