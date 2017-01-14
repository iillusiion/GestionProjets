package com.projet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "date_deposition", nullable = false)
    private LocalDate dateDeposition;

    @NotNull
    @Column(name = "type_document", nullable = false)
    private String typeDocument;

    @ManyToOne
    private Projet projet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Document libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public Document description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDeposition() {
        return dateDeposition;
    }

    public Document dateDeposition(LocalDate dateDeposition) {
        this.dateDeposition = dateDeposition;
        return this;
    }

    public void setDateDeposition(LocalDate dateDeposition) {
        this.dateDeposition = dateDeposition;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public Document typeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
        return this;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public Projet getProjet() {
        return projet;
    }

    public Document projet(Projet projet) {
        this.projet = projet;
        return this;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Document document = (Document) o;
        if(document.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, document.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Document{" +
            "id=" + id +
            ", libelle='" + libelle + "'" +
            ", description='" + description + "'" +
            ", dateDeposition='" + dateDeposition + "'" +
            ", typeDocument='" + typeDocument + "'" +
            '}';
    }
}
