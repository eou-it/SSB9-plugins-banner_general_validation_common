/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import net.hedtech.banner.general.system.Subject


/**
 * LDM decorator for Subject  resource
 * which is sent back to the consumer
 */
class SubjectDetail {

    @Delegate
    private final Subject subject
    String guid


    SubjectDetail(Subject subject, String guid) {
        this.subject = subject
        this.guid = guid
    }

}
