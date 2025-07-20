package com.leafpay.web.rest;

import com.leafpay.repository.AlerteSecuriteRepository;
import com.leafpay.service.AlerteSecuriteService;
import com.leafpay.service.dto.AlerteSecuriteDTO;
import com.leafpay.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.leafpay.domain.AlerteSecurite}.
 */
@RestController
@RequestMapping("/api/alerte-securites")
public class AlerteSecuriteResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteSecuriteResource.class);

    private static final String ENTITY_NAME = "alerteSecurite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlerteSecuriteService alerteSecuriteService;

    private final AlerteSecuriteRepository alerteSecuriteRepository;

    public AlerteSecuriteResource(AlerteSecuriteService alerteSecuriteService, AlerteSecuriteRepository alerteSecuriteRepository) {
        this.alerteSecuriteService = alerteSecuriteService;
        this.alerteSecuriteRepository = alerteSecuriteRepository;
    }

    /**
     * {@code POST  /alerte-securites} : Create a new alerteSecurite.
     *
     * @param alerteSecuriteDTO the alerteSecuriteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alerteSecuriteDTO, or with status {@code 400 (Bad Request)} if the alerteSecurite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlerteSecuriteDTO> createAlerteSecurite(@Valid @RequestBody AlerteSecuriteDTO alerteSecuriteDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlerteSecurite : {}", alerteSecuriteDTO);
        if (alerteSecuriteDTO.getId() != null) {
            throw new BadRequestAlertException("A new alerteSecurite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alerteSecuriteDTO = alerteSecuriteService.save(alerteSecuriteDTO);
        return ResponseEntity.created(new URI("/api/alerte-securites/" + alerteSecuriteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alerteSecuriteDTO.getId().toString()))
            .body(alerteSecuriteDTO);
    }

    /**
     * {@code PUT  /alerte-securites/:id} : Updates an existing alerteSecurite.
     *
     * @param id the id of the alerteSecuriteDTO to save.
     * @param alerteSecuriteDTO the alerteSecuriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerteSecuriteDTO,
     * or with status {@code 400 (Bad Request)} if the alerteSecuriteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alerteSecuriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlerteSecuriteDTO> updateAlerteSecurite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlerteSecuriteDTO alerteSecuriteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlerteSecurite : {}, {}", id, alerteSecuriteDTO);
        if (alerteSecuriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerteSecuriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteSecuriteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alerteSecuriteDTO = alerteSecuriteService.update(alerteSecuriteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerteSecuriteDTO.getId().toString()))
            .body(alerteSecuriteDTO);
    }

    /**
     * {@code PATCH  /alerte-securites/:id} : Partial updates given fields of an existing alerteSecurite, field will ignore if it is null
     *
     * @param id the id of the alerteSecuriteDTO to save.
     * @param alerteSecuriteDTO the alerteSecuriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerteSecuriteDTO,
     * or with status {@code 400 (Bad Request)} if the alerteSecuriteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alerteSecuriteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alerteSecuriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlerteSecuriteDTO> partialUpdateAlerteSecurite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlerteSecuriteDTO alerteSecuriteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlerteSecurite partially : {}, {}", id, alerteSecuriteDTO);
        if (alerteSecuriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerteSecuriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteSecuriteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlerteSecuriteDTO> result = alerteSecuriteService.partialUpdate(alerteSecuriteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerteSecuriteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /alerte-securites} : get all the alerteSecurites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alerteSecurites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlerteSecuriteDTO>> getAllAlerteSecurites(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AlerteSecurites");
        Page<AlerteSecuriteDTO> page = alerteSecuriteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alerte-securites/:id} : get the "id" alerteSecurite.
     *
     * @param id the id of the alerteSecuriteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alerteSecuriteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlerteSecuriteDTO> getAlerteSecurite(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlerteSecurite : {}", id);
        Optional<AlerteSecuriteDTO> alerteSecuriteDTO = alerteSecuriteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alerteSecuriteDTO);
    }

    /**
     * {@code DELETE  /alerte-securites/:id} : delete the "id" alerteSecurite.
     *
     * @param id the id of the alerteSecuriteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlerteSecurite(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlerteSecurite : {}", id);
        alerteSecuriteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
