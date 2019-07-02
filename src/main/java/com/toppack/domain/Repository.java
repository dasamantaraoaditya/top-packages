package com.toppack.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Repository.
 */
@Entity
@Table(name = "repository")
public class Repository implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "stars")
    private Integer stars;

    @Column(name = "forks")
    private Integer forks;

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

    public Repository name(String name) {
        this.name = name;
        return this;
    }

	public void setName(String name) {
        this.name = name;
    }

    public Integer getStars() {
        return stars;
    }

    public Repository stars(Integer stars) {
        this.stars = stars;
        return this;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getForks() {
        return forks;
    }

    public Repository forks(Integer forks) {
        this.forks = forks;
        return this;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
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
        Repository repository = (Repository) o;
        if (repository.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), repository.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Repository{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", stars='" + getStars() + "'" +
            ", forks='" + getForks() + "'" +
            "}";
    }
}
