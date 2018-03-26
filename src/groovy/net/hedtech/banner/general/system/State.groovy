/** *****************************************************************************
 Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
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
 * State Code Validation Table
 */

@Entity
@Table(name = "STVSTAT")
@NamedQueries(value = [
        @NamedQuery(name = "State.fetchByCode",
                query = """ FROM State a WHERE a.code = :code """)
])

@ToString(includeNames = true, ignoreNulls = false)
@EqualsAndHashCode(includeFields = true)
class State implements Serializable {

    /**
     * Surrogate ID for STVSTAT
     */
    @Id
    @Column(name = "STVSTAT_SURROGATE_ID")
    @SequenceGenerator(name = "STVSTAT_SEQ_GEN", allocationSize = 1, sequenceName = "STVSTAT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVSTAT_SEQ_GEN")
    Long id

    /**
     * This field identifies the state code referenced in the Gen.  Person, Admissions, Gen.  Student, Student Billing, and Academic History Modules.
     */
    @Column(name = "STVSTAT_CODE", nullable = false, length = 3)
    String code

    /**
     * This field specifies the state associated with the state code.
     */
    @Column(name = "STVSTAT_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the three character International Standards Organization (ISO) Code associated with the user defined nation code
     */
    @Column(name = "STVSTAT_SCOD_CODE_ISO", length = 3)
    String isoCode

    /**
     * This field identifies the most recent date a record was created or deleted.
     */
    @Column(name = "STVSTAT_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * This is the state equivalent code EDI will use for electronic transcripts for the BANNER2 state code matched on column STVSTAT_CODE.
     */
    @Column(name = "STVSTAT_EDI_EQUIV", length = 2)
    String ediEquiv

    /**
     * Statistics Canadian specific code.
     */
    @Column(name = "STVSTAT_STATSCAN_CDE", length = 5)
    String statscan

    /**
     * Free format code for Student IPEDS reporting.
     */
    @Column(name = "STVSTAT_IPEDS_CDE", length = 5)
    String ipeds

    /**
     * Version column which is used as a optimistic lock token for STVSTAT
     */
    @Version
    @Column(name = "STVSTAT_VERSION", length = 19)
    Long version

    /**
     * Last Modified By column for STVSTAT
     */
    @Column(name = "STVSTAT_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVSTAT
     */
    @Column(name = "STVSTAT_DATA_ORIGIN", length = 30)
    String dataOrigin

    static constraints = {
        code(nullable: false, maxSize: 3)
        description(nullable: false, maxSize: 30)
        isoCode(nullable: true, maxSize: 3)
        lastModified(nullable: true)
        ediEquiv(nullable: true, maxSize: 2)
        statscan(nullable: true, maxSize: 5)
        ipeds(nullable: true, maxSize: 5)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    public static fetchByCode(String code) {
        def state

        State.withSession { session ->
            state = session.getNamedQuery(
                    'State.fetchByCode')
                    .setString('code', code).list()[0]
        }
        return state
    }

}
