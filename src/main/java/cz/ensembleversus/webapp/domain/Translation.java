package cz.ensembleversus.webapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import cz.ensembleversus.webapp.domain.enumeration.LocaleType;

/**
 * A Translation.
 */
@Entity
@Table(name = "translation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Translation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "locale", nullable = false)
    private LocaleType locale;

    @NotNull
    @Column(name = "translation", nullable = false)
    private String translation;

    @ManyToOne
    private TranslationKey key;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocaleType getLocale() {
        return locale;
    }

    public void setLocale(LocaleType locale) {
        this.locale = locale;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public TranslationKey getKey() {
        return key;
    }

    public void setKey(TranslationKey translationKey) {
        this.key = translationKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Translation translation = (Translation) o;
        if(translation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, translation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Translation{" +
            "id=" + id +
            ", locale='" + locale + "'" +
            ", translation='" + translation + "'" +
            '}';
    }
}
