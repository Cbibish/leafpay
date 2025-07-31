package com.leafpay.web.rest;

import com.leafpay.domain.UtilisateurCompte;
import com.leafpay.repository.UtilisateurCompteRepository;
import com.leafpay.service.UtilisateurCompteService;
import com.leafpay.service.dto.CompteDTO;
import com.leafpay.service.dto.UtilisateurCompteDTO;
import com.leafpay.service.dto.UtilisateurCompteDetailDTO;
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
 * REST controller for managing {@link com.leafpay.domain.UtilisateurCompte}.
 */
@RestController
@RequestMapping("/api/utilisateur-comptes")
public class UtilisateurCompteResource {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurCompteResource.class);

    private static final String ENTITY_NAME = "utilisateurCompte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtilisateurCompteService utilisateurCompteService;

    private final UtilisateurCompteRepository utilisateurCompteRepository;

    public UtilisateurCompteResource(
        UtilisateurCompteService utilisateurCompteService,
        UtilisateurCompteRepository utilisateurCompteRepository
    ) {
        this.utilisateurCompteService = utilisateurCompteService;
        this.utilisateurCompteRepository = utilisateurCompteRepository;
    }

    /**
     * {@code POST  /utilisateur-comptes} : Create a new utilisateurCompte.
     *
     * @param utilisateurCompteDTO the utilisateurCompteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new utilisateurCompteDTO, or with status
     *         {@code 400 (Bad Request)} if the utilisateurCompte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UtilisateurCompteDTO> createUtilisateurCompte(@Valid @RequestBody UtilisateurCompteDTO utilisateurCompteDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UtilisateurCompte : {}", utilisateurCompteDTO);
        if (utilisateurCompteDTO.getId() != null) {
            throw new BadRequestAlertException("A new utilisateurCompte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        utilisateurCompteDTO = utilisateurCompteService.save(utilisateurCompteDTO);
        return ResponseEntity.created(new URI("/api/utilisateur-comptes/" + utilisateurCompteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, utilisateurCompteDTO.getId().toString()))
            .body(utilisateurCompteDTO);
    }

    /**
     * {@code PUT  /utilisateur-comptes/:id} : Updates an existing
     * utilisateurCompte.
     *
     * @param id                   the id of the utilisateurCompteDTO to save.
     * @param utilisateurCompteDTO the utilisateurCompteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated utilisateurCompteDTO,
     *         or with status {@code 400 (Bad Request)} if the utilisateurCompteDTO
     *         is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         utilisateurCompteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurCompteDTO> updateUtilisateurCompte(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UtilisateurCompteDTO utilisateurCompteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UtilisateurCompte : {}, {}", id, utilisateurCompteDTO);
        if (utilisateurCompteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateurCompteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurCompteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        utilisateurCompteDTO = utilisateurCompteService.update(utilisateurCompteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisateurCompteDTO.getId().toString()))
            .body(utilisateurCompteDTO);
    }

    /**
     * {@code PATCH  /utilisateur-comptes/:id} : Partial updates given fields of an
     * existing utilisateurCompte, field will ignore if it is null
     *
     * @param id                   the id of the utilisateurCompteDTO to save.
     * @param utilisateurCompteDTO the utilisateurCompteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated utilisateurCompteDTO,
     *         or with status {@code 400 (Bad Request)} if the utilisateurCompteDTO
     *         is not valid,
     *         or with status {@code 404 (Not Found)} if the utilisateurCompteDTO is
     *         not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         utilisateurCompteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UtilisateurCompteDTO> partialUpdateUtilisateurCompte(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UtilisateurCompteDTO utilisateurCompteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UtilisateurCompte partially : {}, {}", id, utilisateurCompteDTO);
        if (utilisateurCompteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateurCompteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurCompteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UtilisateurCompteDTO> result = utilisateurCompteService.partialUpdate(utilisateurCompteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisateurCompteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /utilisateur-comptes} : get all the utilisateurComptes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of utilisateurComptes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UtilisateurCompteDTO>> getAllUtilisateurComptes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of UtilisateurComptes");
        Page<UtilisateurCompteDTO> page = utilisateurCompteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /utilisateur-comptes/:id} : get the "id" utilisateurCompte.
     *
     * @param id the id of the utilisateurCompteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the utilisateurCompteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurCompteDTO> getUtilisateurCompte(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UtilisateurCompte : {}", id);
        Optional<UtilisateurCompteDTO> utilisateurCompteDTO = utilisateurCompteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utilisateurCompteDTO);
    }


    @GetMapping("/utilisateur/{id}/details")
public List<UtilisateurCompteDetailDTO> getComptesDetailByUtilisateur(@PathVariable Long id) {
    List<UtilisateurCompte> utilisateurComptes = utilisateurCompteRepository.findByUtilisateurIdWithCompte(id);

    return utilisateurComptes.stream()
        .map(uc -> new UtilisateurCompteDetailDTO(
                uc.getUtilisateur().getId(),
                uc.getUtilisateur().getNom(),
                uc.getUtilisateur().getPrenom(),
                uc.getUtilisateur().getEmail(),
                uc.getCompte().getId(),
                uc.getCompte().getTypeCompte(),
                uc.getCompte().getSolde(),
                uc.getCompte().getIban(),
                uc.getRoleUtilisateurSurCeCompte()
        ))
        .toList();
}


    /**
     * {@code GET  /utilisateur-comptes/:id} : get the "id" utilisateurCompte. Will
     * return all instances, not only one.
     *
     * @param id the id of the utilisateurCompteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the utilisateurCompteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<UtilisateurCompteDTO>> getByUtilisateurId(@PathVariable Long utilisateurId, Pageable pageable) {
        Page<UtilisateurCompteDTO> page = utilisateurCompteService.findByUtilisateurId(utilisateurId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code DELETE  /utilisateur-comptes/:id} : delete the "id" utilisateurCompte.
     *
     * @param id the id of the utilisateurCompteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateurCompte(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UtilisateurCompte : {}", id);
        utilisateurCompteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
