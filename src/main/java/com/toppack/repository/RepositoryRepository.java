package com.toppack.repository;

import com.toppack.domain.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Repository entity.
 */
@SuppressWarnings("unused")
@org.springframework.stereotype.Repository
public interface RepositoryRepository extends JpaRepository<Repository, Long> {

}
