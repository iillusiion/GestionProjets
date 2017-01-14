package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A RendezVous.
 */
@Entity
@Table(name = "rendez_vous")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RendezVous implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "ordre_du_jour", nullable = false)
    private String ordreDuJour;

    @ManyToOne
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public RendezVous date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public RendezVous description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrdreDuJour() {
        return ordreDuJour;
    }

    public RendezVous ordreDuJour(String ordreDuJour) {
        this.ordreDuJour = ordreDuJour;
        return this;
    }

    public void setOrdreDuJour(String ordreDuJour) {
        this.ordreDuJour = ordreDuJour;
    }

    public User getUser() {
        return user;
    }

    public RendezVous user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RendezVous rendezVous = (RendezVous) o;
        if(rendezVous.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rendezVous.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RendezVous{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", description='" + description + "'" +
            ", ordreDuJour='" + ordreDuJour + "'" +
            '}';
    }
}
