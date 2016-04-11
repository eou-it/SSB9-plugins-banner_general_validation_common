/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version


/**
 * GeographicRegion Validation table.
 */


@Entity
@Table(name="STVGEOR")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class GeographicRegion implements Serializable{

    /**
     * Surrogate ID for STVGEOR
      */
    @Id
    @Column(name="STVGEOR_SURROGATE_ID")
    @SequenceGenerator(name = "STVGEOR_SEQ_GEN", allocationSize = 1, sequenceName = "STVGEOR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVGEOR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVGEOR
     */
    @Version
    @Column(name = "STVGEOR_VERSION")
    Long version

    /**
     * The geographic region code
     */
    @Column(name = "STVGEOR_CODE")
    String code

    /**
     * The geographic region description
     */
    @Column(name = "STVGEOR_DESC")
    String description

    /**
     * The date the record was created or last updated
     */
    @Column(name = "STVGEOR_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * The user who created or last updated the record
     */
    @Column(name = "STVGEOR_USER_ID")
    String lastModifiedBy

    /**
     * Data Origin column
     */
    @Column(name = "STVGEOR_DATA_ORIGIN")
    String dataOrigin


    /**
     * Constraints
     */
    static constraints = {
        code(nullable: false, maxSize: 10)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


    /**
     * Protect the field against the updates
     */
    public static readonlyProperties = ['code']
}
