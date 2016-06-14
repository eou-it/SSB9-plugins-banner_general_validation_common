/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * <p> Decorator for EmailTypeCompositeSevice</p>
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class EmailTypeDetails {

    String code
    String description
    String id
    String emailType

    EmailTypeDetails(String code, String description, String guid, String emailType) {
        this.code = code
        this.description = description
        this.id = guid
        this.emailType = emailType
    }

   Map getDetail(){
        return [id:this.id]
    }
}
