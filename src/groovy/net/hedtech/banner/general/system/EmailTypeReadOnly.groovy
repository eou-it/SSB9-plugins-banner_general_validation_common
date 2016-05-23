/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*

/**
 * <p> Email Types data from Person as well as from Organization</p>
 */
@Entity
@Table(name = "GVQ_EMAIL_TYPES")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "EmailTypeReadOnly.fetchByGuid", query = """FROM EmailTypeReadOnly p WHERE p.id = :guid""")
])
class EmailTypeReadOnly implements Serializable{

    /**
     * GUID: Unique identifier across all database LDM objects.
     */
    @Id
    @Column(name = "GUID")
    String id

    /**
     * Code for Email Type
     * */
    @Column(name = 'CODE')
    String code

    /**
     * Email Type Description
     * */
    @Column(name = 'DESCRIPTION')
    String description

    /**
     * Data origin for Email Type
     * */
    @Column(name = 'DATA_ORIGIN')
    String dataOrigin

    /**
     * Translation value from GORICCR
     * */
    @Column(name = 'EMAIL_TYPE')
    String emailType

    /**
     * VERSION for GVQ_EMAIL_TYPES
     */
    @Version
    @Column(name = "VERSION")
    Long version

    /**
     * VALUE: Value of configuration setting.
     */
    @Column(name = "GORICCR_VALUE")
    String value

    /**
     * fetching AddressType data based on guid
     * @param guid
     * @return PhoneType
     */
    public static EmailTypeReadOnly fetchByGuid(String guid){
        return  EmailTypeReadOnly.withSession{ session ->
            session.getNamedQuery('EmailTypeReadOnly.fetchByGuid').setString('guid',guid).uniqueResult()
        }
    }
}
