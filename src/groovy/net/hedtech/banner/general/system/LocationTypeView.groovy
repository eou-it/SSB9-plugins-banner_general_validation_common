/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * <p> Location Type Read only View to retrieve the data from STVATYP , GORICCR and GORGUID Tables</p>
 */

@Entity
@Table(name = "GVQ_LOCATION_TYPES")
class LocationTypeView implements Serializable {

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
     * TYPE: Filed to identify the type of the location
     */
    @Column(name = "ENTITY_TYPE")
    String entityType

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof LocationTypeView)) return false

        LocationTypeView that = (LocationTypeView) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (entityType != that.entityType) return false
        if (id != that.id) return false
        if (locationType != that.locationType) return false
        if (value != that.value) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (locationType != null ? locationType.hashCode() : 0)
        result = 31 * result + (value != null ? value.hashCode() : 0)
        result = 31 * result + (entityType != null ? entityType.hashCode() : 0)
        return result
    }


    @Override
    public String toString() {
        return "LocationTypeView{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", locationType='" + locationType + '\'' +
                ", value='" + value + '\'' +
                ", entityType='" + entityType + '\'' +
                '}';
    }
}
