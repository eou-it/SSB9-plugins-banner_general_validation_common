/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Address Type Validation Table
 */

@Entity
@Table(name = "STVATYP")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "AddressType.fetchByCode",
                query = """FROM  AddressType a WHERE a.code = :code """),
        @NamedQuery(name = "AddressType.fetchAllWithGuid",
                query = """from AddressType a , GlobalUniqueIdentifier b where a.code = b.domainKey and b.ldmName = :ldmName"""),
        @NamedQuery(name = "AddressType.fetchAllWithGuidByCodeInList",
                query = """from AddressType a , GlobalUniqueIdentifier b where a.code = b.domainKey and b.ldmName = :ldmName and a.code in :codes """),
        @NamedQuery(name = "AddressType.fetchAllByCodeInList",
                query = """from AddressType a where a.code in :codes""")
])
class AddressType implements Serializable {

    /**
     * Surrogate ID for STVATYP
     */
    @Id
    @Column(name = "STVATYP_SURROGATE_ID")
    @SequenceGenerator(name = "STVATYP_SEQ_GEN", allocationSize = 1, sequenceName = "STVATYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVATYP_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVATYP
     */
    @Version
    @Column(name = "STVATYP_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * A code used throughout all BANNER systems to identify the type of address for which information is maintained (for example; Mailing, Permanent, or Billing).
     */
    @Column(name = "STVATYP_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * This field identifies the address type associated with the type code.
     */
    @Column(name = "STVATYP_DESC", length = 30)
    String description

    /**
     * System Required Indicator
     */
    @Column(name = "STVATYP_SYSTEM_REQ_IND", length = 1)
    String systemRequiredIndicator

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVATYP_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for STVATYP
     */
    @Column(name = "STVATYP_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for STVATYP
     */
    @Column(name = "STVATYP_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Foreign Key : FK1_STVATYP_INV_STVTELE_CODE
     */
    @ManyToOne
    @JoinColumns([
            @JoinColumn(name = "STVATYP_TELE_CODE", referencedColumnName = "STVTELE_CODE")
    ])
    TelephoneType telephoneType


    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: true, maxSize: 30)
        systemRequiredIndicator(nullable: true, maxSize: 1, inList: ["Y", "N"])
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        telephoneType(nullable: true)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

    /**
     * fetching AddressType details based on code
     * @param code
     * @return
     */
    public static AddressType fetchByCode(String code){
        AddressType addressType = AddressType.withSession{ session ->
            session.getNamedQuery('AddressType.fetchByCode').setString('code',code).uniqueResult()
        }
        return addressType

    }

}
