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
@Table(name = "GVQ_STVTELE")
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
     * PROCESS CODE: The integration configuration setting process code.
     */
    @Column(name = "SQPR_CODE")
    String processCode
    
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
    @Column(name = "TRANSLATION_VALUE")
    String translationValue

    /**
     * Telephone Type code.
     */
    @Column(name = "CODE")
    String code

    /**
     * Telephone Type .
     */
    @Column(name = "TYPE")
    String phoneType


    @Override
    public String toString() {
        return "PhoneTypeView[" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", processCode='" + processCode + '\'' +
                ", description='" + description + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", translationValue='" + translationValue + '\'' +
                ", code='" + code + '\'' +
                ", phoneType='" + phoneType + '\'' +
                ']';
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        PhoneTypeView that = (PhoneTypeView) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (id != that.id) return false
        if (processCode != that.processCode) return false
        if (translationValue != that.translationValue) return false
        if (value != that.value) return false
        if (phoneType != that.phoneType) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (value != null ? value.hashCode() : 0)
        result = 31 * result + (processCode != null ? processCode.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (translationValue != null ? translationValue.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (phoneType != null ? phoneType.hashCode() : 0)
        return result
    }
}
