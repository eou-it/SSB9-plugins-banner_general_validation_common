/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table
import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator
import org.hibernate.annotations.Type
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version


/**
 * Directory Options Validation Table
 */

@Entity
@Table(name = "GTVDIRO")
@NamedQueries(value = [
        @NamedQuery(name = "DirectoryOption.fetchByCode",
                query = """FROM DirectoryOption a
    WHERE a.code = :code
""")
])
class DirectoryOption implements Serializable {

    /**
     * Surrogate ID for GTVDIRO
     */
    @Id
    @Column(name="GTVDIRO_SURROGATE_ID")
    @SequenceGenerator(name ="GTVDIRO_SEQ_GEN", allocationSize =1, sequenceName  ="GTVDIRO_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator ="GTVDIRO_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVDIRO
     */
    @Version
    @Column(name = "GTVDIRO_VERSION")
    Long version

    /**
     * DIRECTORY CODE: Code for Directory Item.
     */
    @Column(name = "GTVDIRO_CODE")
    String code

    /**
     * DIRECTORY DESCRIPTION: Description for Directory Item.
     */
    @Column(name = "GTVDIRO_DESC")
    String description

    /**
     * SYSTEM REQUIRED INDICATOR: System Required Indicator.
     */
    @Type(type = "yes_no")
    @Column(name = "GTVDIRO_SYSTEM_REQ_IND")
    Boolean systemRequiredIndicator

    /**
     * ACTIVITY DATE: Date of last activity on this record.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVDIRO_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for GTVDIRO
     */
    @Column(name = "GTVDIRO_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for GTVDIRO
     */
    @Column(name = "GTVDIRO_DATA_ORIGIN")
    String dataOrigin


    public String toString() {
        """DirectoryOption[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					systemRequiredIndicator=$systemRequiredIndicator, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof DirectoryOption)) return false
        DirectoryOption that = (DirectoryOption) o
        if(id != that.id) return false
        if(version != that.version) return false
        if(code != that.code) return false
        if(description != that.description) return false
        if(systemRequiredIndicator != that.systemRequiredIndicator) return false
        if(lastModified != that.lastModified) return false
        if(lastModifiedBy != that.lastModifiedBy) return false
        if(dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable:false, maxSize:8)
        description(nullable:false, maxSize:30)
        systemRequiredIndicator(nullable:false)
        lastModified(nullable:true)
        lastModifiedBy(nullable:true, maxSize:30)
        dataOrigin(nullable:true, maxSize:30)
    }


    //Read Only fields that should be protected against update
    public static readonlyProperties = [ 'code' ]

}
