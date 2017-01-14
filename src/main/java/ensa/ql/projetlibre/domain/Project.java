package ensa.ql.projetlibre.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ensa.ql.projetlibre.domain.enumeration.ProjectTypes;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "project_type", nullable = false)
    private ProjectTypes projectType;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "date_begining", nullable = false)
    private LocalDate dateBegining;

    @NotNull
    @Column(name = "date_ending", nullable = false)
    private LocalDate dateEnding;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<Documentation> documentations = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<Deliverable> deliverables = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "project_tag",
               joinColumns = @JoinColumn(name="projects_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="ID"))
    private Set<Tag> tags = new HashSet<>();

    @OneToOne(mappedBy = "project")
    @JsonIgnore
    private Calender calender;

    @ManyToMany
    @JoinTable(name = "project_user",
               joinColumns = @JoinColumn(name="projects_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="users_id", referencedColumnName="ID"))
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectTypes getProjectType() {
        return projectType;
    }

    public Project projectType(ProjectTypes projectType) {
        this.projectType = projectType;
        return this;
    }

    public void setProjectType(ProjectTypes projectType) {
        this.projectType = projectType;
    }

    public String getDescription() {
        return description;
    }

    public Project description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateBegining() {
        return dateBegining;
    }

    public Project dateBegining(LocalDate dateBegining) {
        this.dateBegining = dateBegining;
        return this;
    }

    public void setDateBegining(LocalDate dateBegining) {
        this.dateBegining = dateBegining;
    }

    public LocalDate getDateEnding() {
        return dateEnding;
    }

    public Project dateEnding(LocalDate dateEnding) {
        this.dateEnding = dateEnding;
        return this;
    }

    public void setDateEnding(LocalDate dateEnding) {
        this.dateEnding = dateEnding;
    }

    public Set<Documentation> getDocumentations() {
        return documentations;
    }

    public Project documentations(Set<Documentation> documentations) {
        this.documentations = documentations;
        return this;
    }

    public Project addDocumentation(Documentation documentation) {
        documentations.add(documentation);
        documentation.setProject(this);
        return this;
    }

    public Project removeDocumentation(Documentation documentation) {
        documentations.remove(documentation);
        documentation.setProject(null);
        return this;
    }

    public void setDocumentations(Set<Documentation> documentations) {
        this.documentations = documentations;
    }

    public Set<Deliverable> getDeliverables() {
        return deliverables;
    }

    public Project deliverables(Set<Deliverable> deliverables) {
        this.deliverables = deliverables;
        return this;
    }

    public Project addDeliverable(Deliverable deliverable) {
        deliverables.add(deliverable);
        deliverable.setProject(this);
        return this;
    }

    public Project removeDeliverable(Deliverable deliverable) {
        deliverables.remove(deliverable);
        deliverable.setProject(null);
        return this;
    }

    public void setDeliverables(Set<Deliverable> deliverables) {
        this.deliverables = deliverables;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Project tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Project addTag(Tag tag) {
        tags.add(tag);
        tag.getProjects().add(this);
        return this;
    }

    public Project removeTag(Tag tag) {
        tags.remove(tag);
        tag.getProjects().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Calender getCalender() {
        return calender;
    }

    public Project calender(Calender calender) {
        this.calender = calender;
        return this;
    }

    public void setCalender(Calender calender) {
        this.calender = calender;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Project users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Project addUser(User user) {
        users.add(user);
        return this;
    }

    public Project removeUser(User user) {
        users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if (project.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", projectType='" + projectType + "'" +
            ", description='" + description + "'" +
            ", dateBegining='" + dateBegining + "'" +
            ", dateEnding='" + dateEnding + "'" +
            '}';
    }
}
