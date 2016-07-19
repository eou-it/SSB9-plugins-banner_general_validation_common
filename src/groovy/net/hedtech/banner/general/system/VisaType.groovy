/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

/**
 * Import Related Entities if they are external to this package
 */

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Visa Type Code Validation Table
 */

@Entity
@Table(name = "STVVTYP")
@NamedQueries(value = [
        @NamedQuery(name = "VisaType.fetchAllWithGuid", query = """ FROM VisaType n,GlobalUniqueIdentifier g where g.ldmName = :ldmName AND g.domainKey = n.code """),
        @NamedQuery(name = "VisaType.fetchAllWithGuidByCodeInList", query = """ FROM VisaType n,GlobalUniqueIdentifier g where g.ldmName = :ldmName AND g.domainKey = n.code and n.code in :codes """)
])
class VisaType implements Serializable {

    /**
     * Surrogate ID for STVVTYP
     */
    @Id
    @Column(name = "STVVTYP_SURROGATE_ID")
    @SequenceGenerator(name = "STVVTYP_SEQ_GEN", allocationSize = 1, sequenceName = "STVVTYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVVTYP_SEQ_GEN")
    Long id

    /**
     * This field identifies the visa type code referenced on the International Information Form (GOAINTL).
     */
    @Column(name = "STVVTYP_CODE", nullable = false, length = 2)
    String code

    /**
     * This field specifies the visa type (e.g. student visa, resident alien, exchange scholar) associated with the visa type code.
     */
    @Column(name = "STVVTYP_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVVTYP_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Non-resident indicator.  Y indicates student is non-resident
     */
    @Column(name = "STVVTYP_NON_RES_IND", length = 1)
    String nonResIndicator

    /**
     * The Voice Response message number assigned to the recorded message that describes the visa type code.
     */
    @Column(name = "STVVTYP_VR_MSG_NO", length = 22)
    Integer voiceResponseMsgNumber

    /**
     * Statistics Canadian Code,indicates current visa status.
     */
    @Column(name = "STVVTYP_STATSCAN_CDE2", length = 22)
    Integer statscanCde2

    /**
     * SEVIS EQUIVALENCY: SEVIS code for visa type
     */
    @Column(name = "STVVTYP_SEVIS_EQUIV", length = 2)
    String sevisEquiv

    /**
     * Version column which is used as a optimistic lock token for STVVTYP
     */
    @Version
    @Column(name = "STVVTYP_VERSION", nullable = false, length = 19)
    Long version

    /**
     * Last Modified By column for STVVTYP
     */
    @Column(name = "STVVTYP_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVVTYP
     */
    @Column(name = "STVVTYP_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Foreign Key : FK1_STVVTYP_INV_STVADMR_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVVTYP_ADMR_CODE", referencedColumnName = "STVADMR_CODE")
    ])
    AdmissionRequest admrCode


    public String toString() {
        "VisaType[id=$id, code=$code, description=$description, lastModified=$lastModified, nonResIndicator=$nonResIndicator, voiceResponseMsgNumber=$voiceResponseMsgNumber,  statscanCde2=$statscanCde2, sevisEquiv=$sevisEquiv, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
    }

    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        nonResIndicator(nullable: true, maxSize: 1)
        voiceResponseMsgNumber(nullable: true)
        statscanCde2(nullable: true)
        sevisEquiv(nullable: true, maxSize: 2)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        admrCode(nullable: true)
    }


    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof VisaType)) return false;

        VisaType visaType = (VisaType) o;

        if (admrCode != visaType.admrCode) return false;
        if (code != visaType.code) return false;
        if (dataOrigin != visaType.dataOrigin) return false;
        if (description != visaType.description) return false;
        if (id != visaType.id) return false;
        if (lastModified != visaType.lastModified) return false;
        if (lastModifiedBy != visaType.lastModifiedBy) return false;
        if (nonResIndicator != visaType.nonResIndicator) return false;
        if (sevisEquiv != visaType.sevisEquiv) return false;
        if (statscanCde2 != visaType.statscanCde2) return false;
        if (version != visaType.version) return false;
        if (voiceResponseMsgNumber != visaType.voiceResponseMsgNumber) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (nonResIndicator != null ? nonResIndicator.hashCode() : 0);
        result = 31 * result + (voiceResponseMsgNumber != null ? voiceResponseMsgNumber.hashCode() : 0);
        result = 31 * result + (statscanCde2 != null ? statscanCde2.hashCode() : 0);
        result = 31 * result + (sevisEquiv != null ? sevisEquiv.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (admrCode != null ? admrCode.hashCode() : 0);
        return result;
    }

}
