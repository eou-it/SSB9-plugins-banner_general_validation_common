/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version

/**
 * <p> Location Type Read only View to retrieve the data from STVATYP , GORICCR and GORGUID Tables</p>
 */

@Entity
@Table(name = "GVQ_LOCATION_TYPES")
class LocationTypeReadOnly implements Serializable {

    /**
     * Guid for Location-types
     * */
    @Id
    @Column(name = "GUID")
    String id

    /**
     * Code for Location-types (STVATYP Table)
     * */
    @Column(name = "CODE")
    String code

    /**
     * Description about the Location-types code
     * */
    @Column(name = "DESCRIPTION")
    String description

    /**
     * DATA ORIGIN: Source system that created or updated the section
     */
    @Column(name = "DATA_ORIGIN")
    String dataOrigin

    /**
     * TRANSLATION VALUE: A value that is the technical equivalent of the value in the VALUE field (GORICCR Table)
     * */
    @Column(name = "LOCATION_TYPE")
    String locationType

    /**
     * VALUE: Value of configuration setting (GORICCR Table)
     * */
    @Column(name = "GORICCR_VALUE")
    String value


    /**
     * Optimistic lock token for STVATYP
     */
    @Version
    @Column(name = "VERSION")
    Long version

}
