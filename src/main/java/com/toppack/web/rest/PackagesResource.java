package com.toppack.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.toppack.domain.Packages;

import com.toppack.repository.PackagesRepository;
import com.toppack.web.rest.errors.BadRequestAlertException;
import com.toppack.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Packages.
 */
@RestController
@RequestMapping("/api")
public class PackagesResource {

    private final Logger log = LoggerFactory.getLogger(PackagesResource.class);

    private static final String ENTITY_NAME = "packages";

    private final PackagesRepository packagesRepository;

    public PackagesResource(PackagesRepository packagesRepository) {
        this.packagesRepository = packagesRepository;
    }

    /**
     * POST  /packages : Create a new packages.
     *
     * @param packages the packages to create
     * @return the ResponseEntity with status 201 (Created) and with body the new packages, or with status 400 (Bad Request) if the packages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/packages")
    @Timed
    public ResponseEntity<Packages> createPackages(@RequestBody Packages packages) throws URISyntaxException {
        log.debug("REST request to save Packages : {}", packages);
        if (packages.getId() != null) {
            throw new BadRequestAlertException("A new packages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Packages result = packagesRepository.save(packages);
        return ResponseEntity.created(new URI("/api/packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /packages : Updates an existing packages.
     *
     * @param packages the packages to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated packages,
     * or with status 400 (Bad Request) if the packages is not valid,
     * or with status 500 (Internal Server Error) if the packages couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/packages")
    @Timed
    public ResponseEntity<Packages> updatePackages(@RequestBody Packages packages) throws URISyntaxException {
        log.debug("REST request to update Packages : {}", packages);
        if (packages.getId() == null) {
            return createPackages(packages);
        }
        Packages result = packagesRepository.save(packages);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, packages.getId().toString()))
            .body(result);
    }

    /**
     * GET  /packages : get all the packages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of packages in body
     */
    @GetMapping("/packages")
    @Timed
    public List<Packages> getAllPackages() {
        log.debug("REST request to get all Packages");
        return packagesRepository.findAll();
        }

    /**
     * GET  /packages/:id : get the "id" packages.
     *
     * @param id the id of the packages to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the packages, or with status 404 (Not Found)
     */
    @GetMapping("/packages/{id}")
    @Timed
    public ResponseEntity<Packages> getPackages(@PathVariable Long id) {
        log.debug("REST request to get Packages : {}", id);
        Packages packages = packagesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(packages));
    }

    /**
     * DELETE  /packages/:id : delete the "id" packages.
     *
     * @param id the id of the packages to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/packages/{id}")
    @Timed
    public ResponseEntity<Void> deletePackages(@PathVariable Long id) {
        log.debug("REST request to delete Packages : {}", id);
        packagesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
