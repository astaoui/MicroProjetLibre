package ensa.ql.projetlibre.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Documentation.
 */
@Entity
@Table(name = "documentation")
public class Documentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @ManyToMany
    @JoinTable(name = "documentation_tag",
               joinColumns = @JoinColumn(name="documentations_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="ID"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    private Project project;

    @ManyToOne
    private Deliverable deliverable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Documentation title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Documentation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFile() {
        return file;
    }

    public Documentation file(byte[] file) {
        this.file = file;
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public Documentation fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Documentation tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Documentation addTag(Tag tag) {
        tags.add(tag);
        tag.getDocumentations().add(this);
        return this;
    }

    public Documentation removeTag(Tag tag) {
        tags.remove(tag);
        tag.getDocumentations().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Project getProject() {
        return project;
    }

    public Documentation project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Deliverable getDeliverable() {
        return deliverable;
    }

    public Documentation deliverable(Deliverable deliverable) {
        this.deliverable = deliverable;
        return this;
    }

    public void setDeliverable(Deliverable deliverable) {
        this.deliverable = deliverable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Documentation documentation = (Documentation) o;
        if (documentation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, documentation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Documentation{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", file='" + file + "'" +
            ", fileContentType='" + fileContentType + "'" +
            '}';
    }
}
