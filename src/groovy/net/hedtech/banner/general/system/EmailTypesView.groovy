/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Version

/**
 * <p> Email Types data from Person as well as from Organization</p>
 */
@Entity
@Table(name = "GVQ_EMAIL_TYPES")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class EmailTypesView implements Serializable{

    @EmbeddedId
    EmailTypePrimary emailTypePrimary

    /**
     * GUID  for email types
     */
    @Column(name = 'GUID')
    String guid

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
     * Type of email either from Person or Organization
     * */
    @Column(name = 'ENTITY_TYPE')
    String entityType

}
