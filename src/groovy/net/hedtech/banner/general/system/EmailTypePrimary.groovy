/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * <p> Composite primary key for EmialTypeView</p>
 */

@Embeddable
class EmailTypePrimary implements Serializable{

    /**
     * Setting value from GORICCR
     * */
    @Column(name = 'GORICCR_VALUE')
    String settingValue

    /**
     * Process code from GORICCR
     * */
    @Column(name = 'SQPR_CODE')
    String processCode

    @Override
    public String toString() {
        return "EmailTypePrimary{" +
                "settingValue='" + settingValue + '\'' +
                ", processCode='" + processCode + '\'' +
                '}';
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        EmailTypePrimary that = (EmailTypePrimary) o

        if (processCode != that.processCode) return false
        if (settingValue != that.settingValue) return false

        return true
    }

    int hashCode() {
        int result
        result = (settingValue != null ? settingValue.hashCode() : 0)
        result = 31 * result + (processCode != null ? processCode.hashCode() : 0)
        return result
    }
}
