/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * <p>Read only view for phone types.</p>
 */
@Entity
@Table(name = "GVQ_PHONE_TYPES")
class PhoneTypeView implements Serializable {


    /**
     * GUID: Unique identifier across all database LDM objects.
     */
    @Id
    @Column(name = "GUID")
    String id

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
     * Telephone Type .
     */
    @Column(name = "ENTITY_TYPE")
    String entityType

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof PhoneTypeView)) return false

        PhoneTypeView that = (PhoneTypeView) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (entityType != that.entityType) return false
        if (id != that.id) return false
        if (phoneType != that.phoneType) return false
        if (value != that.value) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (value != null ? value.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (phoneType != null ? phoneType.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (entityType != null ? entityType.hashCode() : 0)
        return result
    }


    @Override
    public String toString() {
        return "PhoneTypeView{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", phoneType='" + phoneType + '\'' +
                ", code='" + code + '\'' +
                ", entityType='" + entityType + '\'' +
                '}';
    }
}
