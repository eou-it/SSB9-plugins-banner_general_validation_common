/** *****************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
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

/**
 * County Code Validation Table
 */

@Entity
@Table(name = "STVCNTY")
@NamedQueries(value = [
        @NamedQuery(name = "County.fetchByCode",
                query = """ FROM County a WHERE a.code = :code """
        ),
        @NamedQuery(name = "County.fetchAllByCodeInList",
                query = """ FROM County a WHERE a.code IN :codes """
        ),
        @NamedQuery(name = "County.fetchAllByIsoCode",
                query = """ FROM County a WHERE a.isoCode = :isoCode """
        ),
        @NamedQuery(name = "County.fetchAllCodes",
                query = """ FROM County"""
        )
])

@ToString(includeNames = true, ignoreNulls = false)
@EqualsAndHashCode(includeFields = true)
class County implements Serializable {

    /**
     * Surrogate ID for STVCNTY
     */
    @Id
    @Column(name = "STVCNTY_SURROGATE_ID")
    @SequenceGenerator(name = "STVCNTY_SEQ_GEN", allocationSize = 1, sequenceName = "STVCNTY_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCNTY_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVCNTY
     */
    @Version
    @Column(name = "STVCNTY_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * This field identifies the county code referenced on the Quick Entry (SAAQUIK),  Identification (SPAIDEN) and Source/Base Institution Year (SOABGIY) Forms.
     */
    @Column(name = "STVCNTY_CODE", nullable = false, unique = true, length = 5)
    String code

    /**
     * This field specifies the county associated with the county code.
     */
    @Column(name = "STVCNTY_DESC", length = 30)
    String description

    /**
     * This field identifies the three character International Standards Organization (ISO) Code associated with the user defined county code
     */
    @Column(name = "STVCNTY_SCOD_CODE_ISO")
    String isoCode

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVCNTY_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVCNTY
     */
    @Column(name = "STVCNTY_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVCNTY
     */
    @Column(name = "STVCNTY_DATA_ORIGIN", length = 30)
    String dataOrigin



    static constraints = {
        code(nullable: false, maxSize: 5)
        description(nullable: true, maxSize: 30)
        isoCode(nullable: true, maxSize: 8)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']


    public static fetchByCode(String code) {
        def county

        County.withSession { session ->
            county = session.getNamedQuery(
                    'County.fetchByCode')
                    .setString('code', code).list()[0]
        }
        return county
    }

    public static fetchAllCodes() {
        def county

        County.withSession { session ->
            county = session.getNamedQuery(
                    'County.fetchAllCodes').list()
        }
        return county
    }

}
