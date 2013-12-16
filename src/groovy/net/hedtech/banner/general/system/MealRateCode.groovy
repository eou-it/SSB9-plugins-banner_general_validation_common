/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type

import javax.persistence.*

/**
 * Describes a meal rate code.
 *
 */
@Entity
@Table(name = "STVMRCD")
class MealRateCode implements Serializable {

    /**
     * The unique identifier for this domain object.
     */
    @Id
    @SequenceGenerator(name = "STVMRCD_SEQ_GEN", allocationSize = 1, sequenceName = "STVMRCD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVMRCD_SEQ_GEN")
    @Column(name = "STVMRCD_SURROGATE_ID")
    Long id

    /**
     * Date that this domain object was last modified.
     */
    @Column(name = "STVMRCD_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Id of person to last modify this domain object.
     */
    @Column(name = "STVMRCD_USER_ID")
    String lastModifiedBy

    /**
     * Source system that created or updated this domain object.
     */
    @Column(name = "STVMRCD_DATA_ORIGIN")
    String dataOrigin

    /**
     * Version column which is used as a optimistic lock token for STVBLCK
     */
    @Version
    @Column(name = "STVMRCD_VERSION")
    Long version

    /**
     * MealRateCode code.
     */
    @Column(name = "STVMRCD_CODE")
    String code

    /**
     * MealRateCode description.
     */
    @Column(name = "STVMRCD_DESC")
    String description

    /**
     * MealRateCode monthly basis indicator.
     */
    @Type(type = "yes_no")
    @Column(name = "STVMRCD_MONTHLY_IND")
    Boolean monthlyIndicator

    /**
     * MealRateCode daily basis indicator.
     */
    @Type(type = "yes_no")
    @Column(name = "STVMRCD_DAILY_IND")
    Boolean dailyIndicator

    /**
     * MealRateCode termly basis indicator.
     */
    @Type(type = "yes_no")
    @Column(name = "STVMRCD_TERMLY_IND")
    Boolean termIndicator


    public String toString() {
        """MealRateCode[
            id=$id,
            version=$version,
            code=$code,
            description=$description,
            monthlyIndicator=$monthlyIndicator,
            dailyIndicator=$dailyIndicator,
            termIndicator=$termIndicator,
            lastModified=$lastModified,
            lastModifiedBy=$lastModifiedBy,
            dataOrigin=$dataOrigin]"""
    }


    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: false, maxSize: 30)
        monthlyIndicator(nullable: false)
        dailyIndicator(nullable: false)
        termIndicator(nullable: false)
        lastModified(nullable: false)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof MealRateCode)) return false;

        MealRateCode that = (MealRateCode) o;

        if (code != that.code) return false;
        if (dataOrigin != that.dataOrigin) return false;
        if (description != that.description) return false;
        if (id != that.id) return false;
        if (lastModified != that.lastModified) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (version != that.version) return false;
        if (monthlyIndicator != that.monthlyIndicator) return false;
        if (dailyIndicator != that.dailyIndicator) return false;
        if (termIndicator != that.termIndicator) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (monthlyIndicator != null ? monthlyIndicator.hashCode() : 0);
        result = 31 * result + (dailyIndicator != null ? dailyIndicator.hashCode() : 0);
        result = 31 * result + (termIndicator != null ? termIndicator.hashCode() : 0);
        return result;
    }
}
