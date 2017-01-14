package ensa.ql.projetlibre.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Deliverable.
 */
@Entity
@Table(name = "deliverable")
public class Deliverable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "date_begining", nullable = false)
    private LocalDate dateBegining;

    @Column(name = "date_ending")
    private LocalDate dateEnding;

    @OneToMany(mappedBy = "deliverable")
    @JsonIgnore
    private Set<Documentation> documentations = new HashSet<>();

    @ManyToOne
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Deliverable name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Deliverable description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateBegining() {
        return dateBegining;
    }

    public Deliverable dateBegining(LocalDate dateBegining) {
        this.dateBegining = dateBegining;
        return this;
    }

    public void setDateBegining(LocalDate dateBegining) {
        this.dateBegining = dateBegining;
    }

    public LocalDate getDateEnding() {
        return dateEnding;
    }

    public Deliverable dateEnding(LocalDate dateEnding) {
        this.dateEnding = dateEnding;
        return this;
    }

    public void setDateEnding(LocalDate dateEnding) {
        this.dateEnding = dateEnding;
    }

    public Set<Documentation> getDocumentations() {
        return documentations;
    }

    public Deliverable documentations(Set<Documentation> documentations) {
        this.documentations = documentations;
        return this;
    }

    public Deliverable addDocumentation(Documentation documentation) {
        documentations.add(documentation);
        documentation.setDeliverable(this);
        return this;
    }

    public Deliverable removeDocumentation(Documentation documentation) {
        documentations.remove(documentation);
        documentation.setDeliverable(null);
        return this;
    }

    public void setDocumentations(Set<Documentation> documentations) {
        this.documentations = documentations;
    }

    public Project getProject() {
        return project;
    }

    public Deliverable project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Deliverable deliverable = (Deliverable) o;
        if (deliverable.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, deliverable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Deliverable{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", dateBegining='" + dateBegining + "'" +
            ", dateEnding='" + dateEnding + "'" +
            '}';
    }
}
