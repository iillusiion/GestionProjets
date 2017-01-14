package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Projet.
 */
@Entity
@Table(name = "projet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Projet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "libelle_projet", nullable = false)
    private String libelleProjet;

    @NotNull
    @Column(name = "type_projet", nullable = false)
    private String typeProjet;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleProjet() {
        return libelleProjet;
    }

    public Projet libelleProjet(String libelleProjet) {
        this.libelleProjet = libelleProjet;
        return this;
    }

    public void setLibelleProjet(String libelleProjet) {
        this.libelleProjet = libelleProjet;
    }

    public String getTypeProjet() {
        return typeProjet;
    }

    public Projet typeProjet(String typeProjet) {
        this.typeProjet = typeProjet;
        return this;
    }

    public void setTypeProjet(String typeProjet) {
        this.typeProjet = typeProjet;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public Projet dateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Projet dateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public User getUser() {
        return user;
    }

    public Projet user(User user) {
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
        Projet projet = (Projet) o;
        if(projet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Projet{" +
            "id=" + id +
            ", libelleProjet='" + libelleProjet + "'" +
            ", typeProjet='" + typeProjet + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            '}';
    }
}
