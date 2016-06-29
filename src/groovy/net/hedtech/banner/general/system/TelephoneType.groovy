/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*

/**
 * Telephone Type Validation Table.
 */

@Entity
@Table(name = "STVTELE")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "TelephoneType.fetchByCode",query = """FROM TelephoneType a WHERE a.code = :code"""),
        @NamedQuery(name = "TelephoneType.fetchAllWithGuid",
                query = """from TelephoneType a , GlobalUniqueIdentifier b where a.code = b.domainKey and b.ldmName = :ldmName"""),
        @NamedQuery(name = "TelephoneType.fetchAllWithGuidInList",
                query = """from TelephoneType a , GlobalUniqueIdentifier b where a.code = b.domainKey and b.ldmName = :ldmName and a.code in :codes"""),
        @NamedQuery(name = "TelephoneType.fetchAllByCodeInList",
                query = """from TelephoneType a where a.code in :codes""")
])
class TelephoneType implements Serializable {

    /**
     * Surrogate ID for STVTELE
     */
    @Id
    @Column(name = "STVTELE_SURROGATE_ID")
    @SequenceGenerator(name = "STVTELE_SEQ_GEN", allocationSize = 1, sequenceName = "STVTELE_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVTELE_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVTELE
     */
    @Version
    @Column(name = "STVTELE_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Telephone Type code.
     */
    @Column(name = "STVTELE_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * Telephone Type Description.
     */
    @Column(name = "STVTELE_DESC", nullable = false, length = 30)
    String description

    /**
     * Date of last change for telephone type record.
     */
    @Column(name = "STVTELE_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVTELE
     */
    @Column(name = "STVTELE_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVTELE
     */
    @Column(name = "STVTELE_DATA_ORIGIN", length = 30)
    String dataOrigin


    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

}
