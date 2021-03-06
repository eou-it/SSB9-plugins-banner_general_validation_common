/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Menu Code Table
 */

@Entity
@Table(name = "GTVMENU")
class Menu implements Serializable {

    /**
     * Surrogate ID for GTVMENU
     */
    @Id
    @Column(name = "GTVMENU_SURROGATE_ID")
    @SequenceGenerator(name = "GTVMENU_SEQ_GEN", allocationSize = 1, sequenceName = "GTVMENU_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVMENU_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVMENU
     */
    @Version
    @Column(name = "GTVMENU_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Menu Code.
     */
    @Column(name = "GTVMENU_CODE", nullable = false, unique = true, length = 10)
    String code

    /**
     * Menu Code Description.
     */
    @Column(name = "GTVMENU_DESC", nullable = false, length = 30)
    String description

    /**
     * Date menu code was last created or modified.
     */
    @Column(name = "GTVMENU_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVMENU
     */
    @Column(name = "GTVMENU_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVMENU
     */
    @Column(name = "GTVMENU_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """Menu[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Menu)) return false
        Menu that = (Menu) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 10)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
