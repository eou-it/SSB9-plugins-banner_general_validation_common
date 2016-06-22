/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.GenerationType
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.SequenceGenerator
import org.hibernate.annotations.Type

/**
 * Nation Validation Table
 */

@Entity
@Table(name = "STVNATN")
@NamedQueries(value = [
        @NamedQuery(name = "Nation.fetchByCode",
                query = """ FROM Nation a WHERE a.code = :code """)
])

@ToString(includeNames = true, ignoreNulls = false)
@EqualsAndHashCode(includeFields = true)
class Nation implements Serializable {

    /**
     * Surrogate ID for STVNATN
     */
    @Id
    @Column(name = "STVNATN_SURROGATE_ID")
    @SequenceGenerator(name = "STVNATN_SEQ_GEN", allocationSize = 1, sequenceName = "STVNATN_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVNATN_SEQ_GEN")
    Long id

    /**
     * This field identifies the nation code referenced in the Gen. Person, Admissions, Gen. Student, and Student Billing Modules.
     */
    @Column(name = "STVNATN_CODE", nullable = false, length = 5)
    String code

    /**
     * This field specifies the nation/country associated with the nation code.
     */
    @Column(name = "STVNATN_NATION", nullable = false, length = 30)
    String nation

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVNATN_CAPITAL", length = 20)
    String capital

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVNATN_AREA", length = 22)
    Integer area

    /**
     * This field is not currently in use.
     */
    @Column(name = "STVNATN_POPULATION", length = 22)
    Integer population

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVNATN_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * EDI/SPEEDE country code that equates to the nation code.  Refer to the SPEEDE Implementation Guide, Appendix B, data element 26 for valid values.
     */
    @Column(name = "STVNATN_EDI_EQUIV", length = 2)
    String ediEquiv

    /**
     * LMS (IA Plus) nation (country) code that equates to this Banner nation code. Translation to this LMS code occurs during the LMS transaction feed from Banner FA.
     */
    @Column(name = "STVNATN_LMS_EQUIV", length = 2)
    String lmsEquiv

    /**
     * None
     */
    @Column(name = "STVNATN_POSTAL_MASK", length = 15)
    String postalMask

    /**
     * None
     */
    @Column(name = "STVNATN_TELE_MASK", length = 15)
    String telephoneMask

    /**
     * Statistics Canadian specific code.
     */
    @Column(name = "STVNATN_STATSCAN_CDE", length = 5)
    String statscan

    /**
     * ISO CODE:  The three character International Standards Organization (ISO) Code associated with the user defined nation code.
     */
    @Column(name = "STVNATN_SCOD_CODE_ISO", length = 3)
    String scodIso

    /**
     * Social Security Administration country code that equates to the nation code.  Refer to the Magnetic Media Reporting and Electronic Filing (MMREF-1) Guide, Appendix C, for valid values.
     */
    @Column(name = "STVNATN_SSA_REPORTING_EQUIV", length = 2)
    String ssaReportingEquiv

    /**
     * SEVIS EQUIVALENCY: SEVIS code for nation
     */
    @Column(name = "STVNATN_SEVIS_EQUIV", length = 2)
    String sevisEquiv

    /**
     * Version column which is used as a optimistic lock token for STVNATN
     */
    @Version
    @Column(name = "STVNATN_VERSION", length = 19)
    Long version

    /**
     * Last Modified By column for STVNATN
     */
    @Column(name = "STVNATN_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVNATN
     */
    @Column(name = "STVNATN_DATA_ORIGIN", length = 30)
    String dataOrigin



    static constraints = {
        code(nullable: false, maxSize: 5)
        nation(nullable: false, maxSize: 30)
        capital(nullable: true, maxSize: 20)
        area(nullable: true)
        population(nullable: true)
        lastModified(nullable: true)
        ediEquiv(nullable: true, maxSize: 2)
        lmsEquiv(nullable: true, maxSize: 2)
        postalMask(nullable: true, maxSize: 15)
        telephoneMask(nullable: true, maxSize: 15)
        statscan(nullable: true, maxSize: 5)
        scodIso(nullable: true, maxSize: 3)
        ssaReportingEquiv(nullable: true, maxSize: 2)
        sevisEquiv(nullable: true, maxSize: 2)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


    public static Object fetchBySomeNation() {
        def returnObj = [list: Nation.list().sort { it.nation }]
        return returnObj
    }


    public static Object fetchBySomeNation(filter) {
        def returnObj = [list: Nation.findAllByCodeIlikeOrNationIlike("%" + filter + "%", "%" + filter + "%").sort { it.nation }]
        return returnObj
    }

    public static fetchByCode(String code) {
        def nation

        Nation.withSession { session ->
            nation = session.getNamedQuery(
                    'Nation.fetchByCode')
                    .setString('code', code).list()[0]
        }
        return nation
    }

}
