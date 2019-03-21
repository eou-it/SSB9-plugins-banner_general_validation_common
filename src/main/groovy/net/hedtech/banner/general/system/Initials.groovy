/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Recruiting Initials Code Validation Table
 */

@Entity
@Table(name = "STVINIT")
class Initials implements Serializable {

    /**
     * Surrogate ID for STVINIT
     */
    @Id
    @Column(name = "STVINIT_SURROGATE_ID")
    @SequenceGenerator(name = "STVINIT_SEQ_GEN", allocationSize = 1, sequenceName = "STVINIT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVINIT_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVINIT
     */
    @Version
    @Column(name = "STVINIT_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * This field identifies the recruiting initials code referenced on the Recruiting Material Control Form (SRAMATL) and the Prospect Information Form    (SRARECR).
     */
    @Column(name = "STVINIT_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * This field specifies the recruiter associated with the recruiting initials code.  This form is used to generate signature line for recruiting materials.
     */
    @Column(name = "STVINIT_DESC", nullable = false, length = 60)
    String description

    /**
     * This field identifies the first line of a title associated with the recruiter.
     */
    @Column(name = "STVINIT_TITLE1", nullable = false, length = 35)
    String title1

    /**
     * This field identifies the second line of a title associated with the recruiter.
     */
    @Column(name = "STVINIT_TITLE2", length = 35)
    String title2

    /**
     * Email address for person whose initials are defined for letter.
     */
    @Column(name = "STVINIT_EMAIL_ADDRESS", length = 128)
    String emailAddress

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVINIT_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVINIT
     */
    @Column(name = "STVINIT_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVINIT
     */
    @Column(name = "STVINIT_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """Initials[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					title1=$title1,
					title2=$title2,
					emailAddress=$emailAddress,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Initials)) return false
        Initials that = (Initials) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (title1 != that.title1) return false
        if (title2 != that.title2) return false
        if (emailAddress != that.emailAddress) return false
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
        result = 31 * result + (title1 != null ? title1.hashCode() : 0)
        result = 31 * result + (title2 != null ? title2.hashCode() : 0)
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: false, maxSize: 60)
        title1(nullable: false, maxSize: 35)
        title2(nullable: true, maxSize: 35)
        emailAddress(nullable: true, maxSize: 128)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
