/*********************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.utility

import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes

class InformationTextPersona implements Serializable {

    /**
     * CODE : The persona code
     */
    String code

    /**
     * DESCRIPTION: The description for the persona.
     */
    String description

    /**
     * ACTIVITY DATE: The date that information in this record was entered or last updated.
     */
    Date lastModified

    /**
     * USER ID: The user ID of the person who inserted or last updated this record.
     */
    String lastModifiedBy


    public String toString() {
        """InformationTextPersona[
            code=$code,
            description=$description,
            lastModified=$lastModified,
            lastModifiedBy=$lastModifiedBy
        """
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof InformationTextPersona)) return false
        InformationTextPersona that = (InformationTextPersona) o
        if (code != that.code) return false
        if (description != that.description) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        return true
    }

    int hashCode() {
        int result
        result = (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        return result
    }
}
