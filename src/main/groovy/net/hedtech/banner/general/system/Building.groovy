/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
 package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Building Code Validation Table
 */

@Entity
@Table(name = "STVBLDG")
class Building implements Serializable {

    /**
     * Surrogate ID for STVBLDG
     */
    @Id
    @Column(name = "STVBLDG_SURROGATE_ID")
    @SequenceGenerator(name = "STVBLDG_SEQ_GEN", allocationSize = 1, sequenceName = "STVBLDG_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVBLDG_SEQ_GEN")
    Long id

    /**
     * This field identifies the building code referenced in the Class Schedule and Registration Modules.
     */
    @Column(name = "STVBLDG_CODE", nullable = false, length = 6)
    String code

    /**
     * This field specifies the building associated with the building code.
     */
    @Column(name = "STVBLDG_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated .
     */
    @Column(name = "STVBLDG_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * The Voice Response message number assigned to the recorded message that describes the building code.
     */
    @Column(name = "STVBLDG_VR_MSG_NO", length = 22)
    BigDecimal voiceResponseMsgNumber

    /**
     * Version column which is used as a optimistic lock token for STVBLDG
     */
    @Version
    @Column(name = "STVBLDG_VERSION", nullable = false, length = 19)
    Long version

    /**
     * Last Modified By column for STVBLDG
     */
    @Column(name = "STVBLDG_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVBLDG
     */
    @Column(name = "STVBLDG_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        "Building[id=$id, code=$code, description=$description, lastModified=$lastModified, voiceResponseMsgNumber=$voiceResponseMsgNumber, version=$version, lastModifiedBy=$lastModifiedBy, dataOrigin=$dataOrigin]"
    }


    static constraints = {
        code(nullable: false, maxSize: 6)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        voiceResponseMsgNumber(nullable: true, scale: 0)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }



    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof Building)) return false;

        Building building = (Building) o;

        if (code != building.code) return false;
        if (dataOrigin != building.dataOrigin) return false;
        if (description != building.description) return false;
        if (id != building.id) return false;
        if (lastModified != building.lastModified) return false;
        if (lastModifiedBy != building.lastModifiedBy) return false;
        if (version != building.version) return false;
        if (voiceResponseMsgNumber != building.voiceResponseMsgNumber) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (voiceResponseMsgNumber != null ? voiceResponseMsgNumber.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }

}
