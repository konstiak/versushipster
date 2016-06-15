package cz.ensembleversus.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import cz.ensembleversus.webapp.domain.enumeration.EventType;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_time", nullable = false)
    private ZonedDateTime dateTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EventType type;

    @Column(name = "public_event")
    private Boolean publicEvent;

    @Column(name = "publish_program")
    private Boolean publishProgram;

    @Column(name = "poster")
    private String poster;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_program",
               joinColumns = @JoinColumn(name="events_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="programs_id", referencedColumnName="ID"))
    private Set<Composition> programs = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private TranslationKey title;

    @OneToOne
    @JoinColumn(unique = true)
    private TranslationKey place;

    @OneToOne
    @JoinColumn(unique = true)
    private TranslationKey description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Boolean isPublicEvent() {
        return publicEvent;
    }

    public void setPublicEvent(Boolean publicEvent) {
        this.publicEvent = publicEvent;
    }

    public Boolean isPublishProgram() {
        return publishProgram;
    }

    public void setPublishProgram(Boolean publishProgram) {
        this.publishProgram = publishProgram;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Set<Composition> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<Composition> compositions) {
        this.programs = compositions;
    }

    public TranslationKey getTitle() {
        return title;
    }

    public void setTitle(TranslationKey translationKey) {
        this.title = translationKey;
    }

    public TranslationKey getPlace() {
        return place;
    }

    public void setPlace(TranslationKey translationKey) {
        this.place = translationKey;
    }

    public TranslationKey getDescription() {
        return description;
    }

    public void setDescription(TranslationKey translationKey) {
        this.description = translationKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if(event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", dateTime='" + dateTime + "'" +
            ", type='" + type + "'" +
            ", publicEvent='" + publicEvent + "'" +
            ", publishProgram='" + publishProgram + "'" +
            ", poster='" + poster + "'" +
            '}';
    }
}
