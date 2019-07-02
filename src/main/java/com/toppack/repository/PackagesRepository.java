package com.toppack.repository;

import com.toppack.domain.Packages;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Packages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackagesRepository extends JpaRepository<Packages, Long> {

	@Query(value = "SELECT * FROM toppack.packages p group by p.name order by count(p.name) desc limit 10", nativeQuery = true)
	List<Packages> getTopPackages();

}
