package ensa.ql.projetlibre.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Documentation> documentations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Tag title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Tag projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Tag addProject(Project project) {
        projects.add(project);
        project.getTags().add(this);
        return this;
    }

    public Tag removeProject(Project project) {
        projects.remove(project);
        project.getTags().remove(this);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Documentation> getDocumentations() {
        return documentations;
    }

    public Tag documentations(Set<Documentation> documentations) {
        this.documentations = documentations;
        return this;
    }

    public Tag addDocumentation(Documentation documentation) {
        documentations.add(documentation);
        documentation.getTags().add(this);
        return this;
    }

    public Tag removeDocumentation(Documentation documentation) {
        documentations.remove(documentation);
        documentation.getTags().remove(this);
        return this;
    }

    public void setDocumentations(Set<Documentation> documentations) {
        this.documentations = documentations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        if (tag.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + id +
            ", title='" + title + "'" +
            '}';
    }
}
