package cz.ensembleversus.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import cz.ensembleversus.webapp.domain.enumeration.VoiceType;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "second_name", nullable = false)
    private String secondName;

    @Column(name = "active")
    private Boolean active;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "voice", nullable = false)
    private VoiceType voice;

    @Column(name = "photo")
    private String photo;

    @Column(name = "member_from")
    private LocalDate memberFrom;

    @Column(name = "member_to")
    private LocalDate memberTo;

    @OneToOne
    @JoinColumn(unique = true)
    private TranslationKey description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public VoiceType getVoice() {
        return voice;
    }

    public void setVoice(VoiceType voice) {
        this.voice = voice;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LocalDate getMemberFrom() {
        return memberFrom;
    }

    public void setMemberFrom(LocalDate memberFrom) {
        this.memberFrom = memberFrom;
    }

    public LocalDate getMemberTo() {
        return memberTo;
    }

    public void setMemberTo(LocalDate memberTo) {
        this.memberTo = memberTo;
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
        Member member = (Member) o;
        if(member.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", secondName='" + secondName + "'" +
            ", active='" + active + "'" +
            ", voice='" + voice + "'" +
            ", photo='" + photo + "'" +
            ", memberFrom='" + memberFrom + "'" +
            ", memberTo='" + memberTo + "'" +
            '}';
    }
}
