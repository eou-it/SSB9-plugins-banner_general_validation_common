/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table
import javax.persistence.Version

/**
 * <p>Read only view for phone types.</p>
 */
@Entity
@Table(name = "GVQ_PHONE_TYPES")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "PhoneType.fetchByGuid", query = """FROM PhoneType p WHERE p.id = :guid""")
])
class PhoneType implements Serializable {


    /**
     * GUID: Unique identifier across all database LDM objects.
     */
    @Id
    @Column(name = "GUID")
    String id

    /**
     * VERSION for GVQ_PHONE_TYPES
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
     * Telephone Type Description.
     */
    @Column(name = "DESCRIPTION")
    String description

    /**
     * Telephone Type data origin
     */
    @Column(name = "DATA_ORIGIN")
    String dataOrigin

    /**
     * TRANSLATION VALUE: A value that is the technical equivalent of the value in the VALUE field.
     */
    @Column(name = "PHONE_TYPE")
    String phoneType

    /**
     * Telephone Type code.
     */
    @Column(name = "CODE")
    String code


    /**
     * fetching PhoneType data based on guid
     * @param guid
     * @return PhoneType
     */
   public static PhoneType fetchByGuid(String guid){
        PhoneType phoneType = PhoneType.withSession{ session ->
            session.getNamedQuery('PhoneType.fetchByGuid').setString('guid',guid).uniqueResult()
        }
        return phoneType
    }


}
