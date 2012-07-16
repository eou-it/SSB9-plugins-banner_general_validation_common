/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
package net.hedtech.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * This table defines the Banner name type codes.
 */

@Entity
@Table(name = "GTVNTYP")
class NameType implements Serializable {

    /**
     * Surrogate ID for GTVNTYP
     */
    @Id
    @Column(name = "GTVNTYP_SURROGATE_ID")
    @SequenceGenerator(name = "GTVNTYP_SEQ_GEN", allocationSize = 1, sequenceName = "GTVNTYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVNTYP_SEQ_GEN")
    Long id

    /**
     * NAME TYPE CODE: The code associated with a type of name.
     */
    @Column(name = "GTVNTYP_CODE", nullable = false, length = 4)
    String code

    /**
     * DESCRIPTION: The description of the name type code.
     */
    @Column(name = "GTVNTYP_DESC", nullable = false, length = 30)
    String description

    /**
     * ACTIVITY DATE: The date that this record was created or last updated.
     */
    @Column(name = "GTVNTYP_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Version column which is used as a optimistic lock token for GTVNTYP
     */
    @Version
    @Column(name = "GTVNTYP_VERSION", length = 19)
    Long version

    /**
     * Last Modified By column for GTVNTYP
     */
    @Column(name = "GTVNTYP_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for GTVNTYP
     */
    @Column(name = "GTVNTYP_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """NameType[
					id=$id, 
					code=$code, 
					description=$description, 
					lastModified=$lastModified, 
					version=$version, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin, ]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true;
        if (!(o instanceof NameType)) return false;
        NameType that = (NameType) o;
        if (id != that.id) return false;
        if (code != that.code) return false;
        if (description != that.description) return false;
        if (lastModified != that.lastModified) return false;
        if (version != that.version) return false;
        if (lastModifiedBy != that.lastModifiedBy) return false;
        if (dataOrigin != that.dataOrigin) return false;
        return true;
    }


    int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        return result;
    }


    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)

        /**
         * Please put all the custom tests in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(nametype_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(nametype_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
