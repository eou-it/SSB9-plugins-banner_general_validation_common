/*******************************************************************************
Copyright 2015 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.general.crossproduct

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*
 
/**
 * Bank Routing Number Table entity.
 */
@Entity
@Table(name = "GXVDIRD")
@ToString(includeNames = true, ignoreNulls = false)
@EqualsAndHashCode(includeFields = true)
class BankRoutingInfo implements Serializable {

    /**
     * Surrogate ID for GXVDIRD
     */
    @Id
    @Column(name = "GXVDIRD_SURROGATE_ID")
    @SequenceGenerator(name = "GXVDIRD_SEQ_GEN", allocationSize = 1, sequenceName = "GXVDIRD_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GXVDIRD_SEQ_GEN")
    Long id

    /**
     * VERSION: Optimistic lock token.
     */
    @Version
    @Column(name = "GXVDIRD_VERSION")
    Long version

    /**
     * BANK ROUTING NUMBER:  Routing number for bank.
     * Id annotation is here to allow joins on this field with GXRDIRD.
     */
    @Column(name = "GXVDIRD_CODE_BANK_ROUT_NUM")
    String bankRoutingNum

    /**
     * BANK NAME:  Description contains name of bank.
     */
    @Column(name = "GXVDIRD_DESC")
    String bankName

    /**
     * ACTIVITY DATE: The date when the information for this record on the table was entered or last updated.
     */
    @Column(name = "GXVDIRD_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * USER ID: The user id of the person who updates this record.
     */
    @Column(name = "GXVDIRD_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the data.
     */
    @Column(name = "GXVDIRD_DATA_ORIGIN")
    String dataOrigin

    static constraints = {
        bankRoutingNum(nullable: false, maxSize: 11)
        bankName(nullable: false, maxSize: 60)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }
}