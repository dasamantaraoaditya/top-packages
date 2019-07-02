package com.toppack.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.toppack.domain.Repository;

import com.toppack.repository.RepositoryRepository;
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
 * REST controller for managing Repository.
 */
@RestController
@RequestMapping("/api")
public class RepositoryResource {

    private final Logger log = LoggerFactory.getLogger(RepositoryResource.class);

    private static final String ENTITY_NAME = "repository";

    private final RepositoryRepository repositoryRepository;

    public RepositoryResource(RepositoryRepository repositoryRepository) {
        this.repositoryRepository = repositoryRepository;
    }

    /**
     * POST  /repositories : Create a new repository.
     *
     * @param repository the repository to create
     * @return the ResponseEntity with status 201 (Created) and with body the new repository, or with status 400 (Bad Request) if the repository has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/repositories")
    @Timed
    public ResponseEntity<Repository> createRepository(@RequestBody Repository repository) throws URISyntaxException {
        log.debug("REST request to save Repository : {}", repository);
        if (repository.getId() != null) {
            throw new BadRequestAlertException("A new repository cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Repository result = repositoryRepository.save(repository);
        return ResponseEntity.created(new URI("/api/repositories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /repositories : Updates an existing repository.
     *
     * @param repository the repository to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated repository,
     * or with status 400 (Bad Request) if the repository is not valid,
     * or with status 500 (Internal Server Error) if the repository couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/repositories")
    @Timed
    public ResponseEntity<Repository> updateRepository(@RequestBody Repository repository) throws URISyntaxException {
        log.debug("REST request to update Repository : {}", repository);
        if (repository.getId() == null) {
            return createRepository(repository);
        }
        Repository result = repositoryRepository.save(repository);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, repository.getId().toString()))
            .body(result);
    }

    /**
     * GET  /repositories : get all the repositories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of repositories in body
     */
    @GetMapping("/repositories")
    @Timed
    public List<Repository> getAllRepositories() {
        log.debug("REST request to get all Repositories");
        return repositoryRepository.findAll();
        }

    /**
     * GET  /repositories/:id : get the "id" repository.
     *
     * @param id the id of the repository to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the repository, or with status 404 (Not Found)
     */
    @GetMapping("/repositories/{id}")
    @Timed
    public ResponseEntity<Repository> getRepository(@PathVariable Long id) {
        log.debug("REST request to get Repository : {}", id);
        Repository repository = repositoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(repository));
    }

    /**
     * DELETE  /repositories/:id : delete the "id" repository.
     *
     * @param id the id of the repository to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/repositories/{id}")
    @Timed
    public ResponseEntity<Void> deleteRepository(@PathVariable Long id) {
        log.debug("REST request to delete Repository : {}", id);
        repositoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
