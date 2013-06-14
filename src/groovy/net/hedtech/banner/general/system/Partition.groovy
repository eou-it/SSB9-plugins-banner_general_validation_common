/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Validation entries for Partition codes.  Partition Codes define campus zones, which are catagories or groupings of rooms.
 */
@Entity
@Table(name = "GTVPARS")
class Partition implements Serializable {

    /**
     * Surrogate ID
     */
    @Id
    @Column(name = "gtvpars_surrogate_id")
    @SequenceGenerator(name = "GTVPARS_SEQ_GEN", allocationSize = 1, sequenceName = "GTVPARS_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVPARS_SEQ_GEN")
    Long id

    /**
     * Partition code for campus zones for scheduling product.
     */
    @Column(name = "gtvpars_code", nullable = false, length = 7)
    String code

    /**
     * Description of partition code for scheduling product.
     */
    @Column(name = "gtvpars_desc", nullable = false, length = 30)
    String description

    /**
     * Numeric identifying value for partition code for scheduling product.
     */
    @Column(name = "gtvpars_scheduler_number", nullable = false, length = 22)
    BigDecimal schedulerNumber

    /**
     * This field identifies the user who last updated the record
     */
    @Column(name = "gtvpars_user_id")
    String lastModifiedBy

    /**
     * This field identifies the most recent date a record was created/updated
     */
    @Column(name = "gtvpars_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Campus code associated with partition for scheduling product.
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "gtvpars_camp_code", referencedColumnName = "STVCAMP_CODE")
    ])
    Campus campus

    /**
     * Optimistic Lock Token
     */
    @Version
    @Column(name = "gtvpars_version", nullable = false, length = 19)
    Long version

    /**
     * Application name that created this data
     */
    @Column(name = "gtvpars_data_origin", length = 30)
    String dataOrigin

    static constraints = {
        code(nullable: false, maxSize: 7)
        description(nullable: false, maxSize: 30)
        schedulerNumber(nullable: false)
        campus(nullable: true)
        dataOrigin(nullable: true)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true)
    }




    boolean equals(o) {
        if (this.is(o)) return true;

        if (!(o instanceof Partition)) return false;

        Partition partition = (Partition) o;

        if (campus != partition.campus) return false;
        if (code != partition.code) return false;
        if (dataOrigin != partition.dataOrigin) return false;
        if (description != partition.description) return false;
        if (id != partition.id) return false;
        if (lastModified != partition.lastModified) return false;
        if (lastModifiedBy != partition.lastModifiedBy) return false;
        if (schedulerNumber != partition.schedulerNumber) return false;
        if (version != partition.version) return false;

        return true;
    }


    int hashCode() {
        int result;

        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (schedulerNumber != null ? schedulerNumber.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (campus != null ? campus.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }


    public String toString() {
        """PartitionCode[id=$id,code=$code, description=$description, schedulerNumber=$schedulerNumber, lastModifiedBy=$lastModifiedBy, lastModified=$lastModified, campusCode=$campus, version=$version, dataOrigin=$dataOrigin]"""
    }

}
