/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v4
import groovy.transform.InheritConstructors
import groovy.transform.ToString

/**
 * v4 Decorator for Subject
 */
@ToString(includeNames = true, includeFields = true)
@InheritConstructors
class SubjectDetail extends net.hedtech.banner.general.system.ldm.v1.SubjectDetail{

    def getDetail(){
        return ['id':getGuid()]
    }
}
