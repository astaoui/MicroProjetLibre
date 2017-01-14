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
 * A Calender.
 */
@Entity
@Table(name = "calender")
public class Calender implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "date_begining")
    private LocalDate dateBegining;

    @Column(name = "date_ending")
    private LocalDate dateEnding;

    @OneToOne
    @JoinColumn(unique = true)
    private Project project;

    @OneToMany(mappedBy = "calender")
    @JsonIgnore
    private Set<Meeting> meetings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Calender title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDateBegining() {
        return dateBegining;
    }

    public Calender dateBegining(LocalDate dateBegining) {
        this.dateBegining = dateBegining;
        return this;
    }

    public void setDateBegining(LocalDate dateBegining) {
        this.dateBegining = dateBegining;
    }

    public LocalDate getDateEnding() {
        return dateEnding;
    }

    public Calender dateEnding(LocalDate dateEnding) {
        this.dateEnding = dateEnding;
        return this;
    }

    public void setDateEnding(LocalDate dateEnding) {
        this.dateEnding = dateEnding;
    }

    public Project getProject() {
        return project;
    }

    public Calender project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Meeting> getMeetings() {
        return meetings;
    }

    public Calender meetings(Set<Meeting> meetings) {
        this.meetings = meetings;
        return this;
    }

    public Calender addMeeting(Meeting meeting) {
        meetings.add(meeting);
        meeting.setCalender(this);
        return this;
    }

    public Calender removeMeeting(Meeting meeting) {
        meetings.remove(meeting);
        meeting.setCalender(null);
        return this;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Calender calender = (Calender) o;
        if (calender.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, calender.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Calender{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", dateBegining='" + dateBegining + "'" +
            ", dateEnding='" + dateEnding + "'" +
            '}';
    }
}
