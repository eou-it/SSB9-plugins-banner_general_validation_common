/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table
/**
 * <p> Email Types data from Person as well as from Organization</p>
 */
@Entity
@Table(name = "GVQ_EMAIL_TYPES")
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
     * Type of email either from Person or Organization
     * */
    @Column(name = 'ENTITY_TYPE')
    String entityType

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof EmailTypesView)) return false

        EmailTypesView that = (EmailTypesView) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (emailType != that.emailType) return false
        if (emailTypePrimary != that.emailTypePrimary) return false
        if (entityType != that.entityType) return false
        if (guid != that.guid) return false

        return true
    }

    int hashCode() {
        int result
        result = (emailTypePrimary != null ? emailTypePrimary.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (emailType != null ? emailType.hashCode() : 0)
        result = 31 * result + (entityType != null ? entityType.hashCode() : 0)
        return result
    }


    @Override
    public String toString() {
        return "EmailTypesView{" +
                "emailTypePrimary=" + emailTypePrimary +
                ", guid='" + guid + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", emailType='" + emailType + '\'' +
                ", entityType='" + entityType + '\'' +
                '}';
    }
}
