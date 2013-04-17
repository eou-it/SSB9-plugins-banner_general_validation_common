/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Subject Validation Table
 */
@Entity
@Table(name = "STVSUBJ")
class Subject implements Serializable {

    /**
     * Surrogate ID for STVSUBJ
     */
    @Id
    @Column(name = "STVSUBJ_SURROGATE_ID")
    @SequenceGenerator(name = "STVSUBJ_SEQ_GEN", allocationSize = 1, sequenceName = "STVSUBJ_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSUBJ_SEQ_GEN")
    Long id

    /**
     * This field identifies the subject code referenced in the Catalog, Registration and Acad.  Hist.  Modules.
     */
    @Column(name = "STVSUBJ_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * This field specifies the subject associated with the subject code.
     */
    @Column(name = "STVSUBJ_DESC", length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVSUBJ_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * The Voice Response message number assigned to the recorded message that describes the subject code.
     */
    @Column(name = "STVSUBJ_VR_MSG_NO", precision = 6)
    Integer vrMsgNo

    /**
     * Web registration indicator
     */
    @Type(type = "yes_no")
    @Column(name = "STVSUBJ_DISP_WEB_IND", nullable = false)
    Boolean dispWebInd

    /**
     * Column for storing last modified by for STVSUBJ
     */
    @Column(name = "STVSUBJ_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Optimistic Lock Token for STVSUBJ
     */
    @Version
    @Column(name = "STVSUBJ_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Column for storing data origin for STVSUBJ
     */
    @Column(name = "STVSUBJ_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        "Subject[id=$id, code=$code, description=$description, lastModified=$lastModified, vrMsgNo=$vrMsgNo, dispWebInd=$dispWebInd, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
    }

    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: true, maxSize: 30)
        vrMsgNo(nullable: true)
        dispWebInd(nullable: false)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


    boolean equals(o) {
        if (this.is(o)) return true

        if (!(o instanceof Subject)) return false

        Subject subject = (Subject) o

        if (code != subject.code) return false
        if (dataOrigin != subject.dataOrigin) return false
        if (description != subject.description) return false
        if (dispWebInd != subject.dispWebInd) return false
        if (id != subject.id) return false
        if (lastModified != subject.lastModified) return false
        if (lastModifiedBy != subject.lastModifiedBy) return false
        if (version != subject.version) return false
        if (vrMsgNo != subject.vrMsgNo) return false

        return true
    }


    int hashCode() {
        int result

        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (vrMsgNo != null ? vrMsgNo.hashCode() : 0)
        result = 31 * result + (dispWebInd != null ? dispWebInd.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
