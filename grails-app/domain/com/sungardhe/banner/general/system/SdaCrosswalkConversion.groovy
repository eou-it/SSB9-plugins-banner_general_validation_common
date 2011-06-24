/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 0.1.1
 Generated: Thu Jan 13 15:25:11 EST 2011 
 */
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator

/**
 * SDA crosswalk/conversion table
 */
/*PROTECTED REGION ID(sdacrosswalkconversion_namedqueries) ENABLED START*/
/**
 * Where clause on this entity present in forms:
 * Form Name: GUAPPRF
 *  where GTVSDAX_INTERNAL_CODE_GROUP = 'CREATEPIN'

 * Form Name: GUAQPRG
 *  GTVSDAX_INTERNAL_CODE = 'AQ4PIPES'

 * Order by clause on this entity present in forms:
 * Form Name: GTVSDAX
 *  gtvsdax_internal_code,
 gtvsdax_internal_code_group,
 gtvsdax_internal_code_seqno,
 gtvsdax_external_code

 WHERE G.GTVSDAX_INTERNAL_CODE = 'PREREQCHK'
 AND G.GTVSDAX_INTERNAL_CODE_GROUP = 'DEGREEWORKS'
 AND G.GTVSDAX_EXTERNAL_CODE <> '999999'
 AND G.GTVSDAX_EXTERNAL_CODE <= :KEY_BLOCK.SSASECT_TERM_CODE
 AND EXISTS (SELECT 1 FROM STVTERM WHERE STVTERM_CODE=G.GTVSDAX_EXTERNAL_CODE);

 * Form Name: GUAQPRG
 *  GTVSDAX_INTERNAL_CODE_GROUP

 */

@NamedQueries(value = [
@NamedQuery(name = "SdaCrosswalkConversion.fetchAllByInternalAndExternalAndInternalGroup",
query = """FROM SdaCrosswalkConversion a
           WHERE  a.internal = :internal
           and a.external = :external
           and a.internalGroup = :internalGroup
           order by a.internalSequenceNumber """),
@NamedQuery(name = "SdaCrosswalkConversion.fetchAllByInternalAndLessExternalAndInternalGroup",
query = """FROM SdaCrosswalkConversion a
           WHERE  a.internal = :internal
           and a.external < :external
           and a.internalGroup = :internalGroup
           order by a.internalSequenceNumber """)    ,
@NamedQuery(name = "SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup",
query = """FROM SdaCrosswalkConversion a
           WHERE  a.internal = :internal
           and a.internalGroup = :internalGroup
           order by a.internalSequenceNumber """)
])




/*PROTECTED REGION END*/
@Entity
@Table(name = "GTVSDAX")
class SdaCrosswalkConversion implements Serializable {

    /**
     * Surrogate ID for GTVSDAX
     */
    @Id
    @Column(name = "GTVSDAX_SURROGATE_ID")
    @SequenceGenerator(name = "GTVSDAX_SEQ_GEN", allocationSize = 1, sequenceName = "GTVSDAX_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVSDAX_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVSDAX
     */
    @Version
    @Column(name = "GTVSDAX_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Banner/concept code for to be retrieved
     */
    @Column(name = "GTVSDAX_EXTERNAL_CODE", nullable = false, length = 15)
    String external

    /**
     * The pre-determined SDA retrieval code
     */
    @Column(name = "GTVSDAX_INTERNAL_CODE", nullable = false, length = 10)
    String internal

    /**
     * The date column used for various reporting needs
     */
    @Column(name = "GTVSDAX_REPORTING_DATE")
    Date reportingDate

    /**
     * The hard-coded translation of the user defined external code, eg. Freshman, PUB for Public School, etc
     */
    @Column(name = "GTVSDAX_TRANSLATION_CODE", length = 15)
    String translation

    /**
     * The sequence number for internal codes requiring multiple values
     */
    @Column(name = "GTVSDAX_INTERNAL_CODE_SEQNO", precision = 2)
    Integer internalSequenceNumber

    /**
     * The group typing for translated or crosswalked values to specifically identify which grouping each series of codes belongs too
     */
    @Column(name = "GTVSDAX_INTERNAL_CODE_GROUP", nullable = false, length = 20)
    String internalGroup

    /**
     * The description of the SDA code/concept
     */
    @Column(name = "GTVSDAX_DESC", nullable = false, length = 30)
    String description

    /**
     * A Y in this field indicates that this row is required for production processing
     */
    @Column(name = "GTVSDAX_SYSREQ_IND", length = 1)
    String systemRequestIndicator

    /**
     * The last activity date for this row of data
     */
    @Column(name = "GTVSDAX_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for GTVSDAX
     */
    @Column(name = "GTVSDAX_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVSDAX
     */
    @Column(name = "GTVSDAX_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """SdaCrosswalkConversion[
					id=$id, 
					version=$version, 
					external=$external, 
					internal=$internal, 
					reportingDate=$reportingDate, 
					translation=$translation, 
					internalSequenceNumber=$internalSequenceNumber, 
					internalGroup=$internalGroup, 
					description=$description, 
					systemRequestIndicator=$systemRequestIndicator, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof SdaCrosswalkConversion)) return false
        SdaCrosswalkConversion that = (SdaCrosswalkConversion) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (external != that.external) return false
        if (internal != that.internal) return false
        if (reportingDate != that.reportingDate) return false
        if (translation != that.translation) return false
        if (internalSequenceNumber != that.internalSequenceNumber) return false
        if (internalGroup != that.internalGroup) return false
        if (description != that.description) return false
        if (systemRequestIndicator != that.systemRequestIndicator) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (external != null ? external.hashCode() : 0)
        result = 31 * result + (internal != null ? internal.hashCode() : 0)
        result = 31 * result + (reportingDate != null ? reportingDate.hashCode() : 0)
        result = 31 * result + (translation != null ? translation.hashCode() : 0)
        result = 31 * result + (internalSequenceNumber != null ? internalSequenceNumber.hashCode() : 0)
        result = 31 * result + (internalGroup != null ? internalGroup.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (systemRequestIndicator != null ? systemRequestIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        external(nullable: false, maxSize: 15)
        internal(nullable: false, maxSize: 10)
        reportingDate(nullable: true)
        translation(nullable: true, maxSize: 15)
        internalSequenceNumber(nullable: true, min: -99, max: 99)
        internalGroup(nullable: false, maxSize: 20)
        description(nullable: false, maxSize: 30)
        systemRequestIndicator(nullable: true, maxSize: 1, inList: ["Y", "N"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(sdacrosswalkconversion_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }
    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(sdacrosswalkconversion_custom_methods) ENABLED START*/


    public static List fetchAllByInternalAndExternalAndInternalGroup(String internal, String external, String internalGroup) {
        def sdax
        SdaCrosswalkConversion.withSession {session ->
            sdax = session.getNamedQuery('SdaCrosswalkConversion.fetchAllByInternalAndExternalAndInternalGroup').setString('internal', internal).setString('internalGroup', internalGroup).setString('external', external).list()
        }
        return sdax

    }


    public static List fetchAllByInternalAndLessExternalAndInternalGroup(String internal, String external, String internalGroup) {
        def sdax
        SdaCrosswalkConversion.withSession {session ->
            sdax = session.getNamedQuery('SdaCrosswalkConversion.fetchAllByInternalAndLessExternalAndInternalGroup').setString('internal', internal).setString('internalGroup', internalGroup).setString('external', external).list()
        }
        return sdax

    }

        public static List fetchAllByInternalAndInternalGroup(String internal,  String internalGroup) {
        def sdax
        SdaCrosswalkConversion.withSession {session ->
            sdax = session.getNamedQuery('SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup')
                    .setString('internal', internal).setString('internalGroup', internalGroup).list()
        }
        return sdax

    }

    /*PROTECTED REGION END*/
}
