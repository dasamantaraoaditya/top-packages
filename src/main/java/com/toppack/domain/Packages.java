package com.toppack.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Packages.
 */
@Entity
@Table(name = "packages")
public class Packages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private Repository repository;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Packages name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Repository getRepository() {
        return repository;
    }

    public Packages repository(Repository repository) {
        this.repository = repository;
        return this;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Packages packages = (Packages) o;
        if (packages.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), packages.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Packages{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
