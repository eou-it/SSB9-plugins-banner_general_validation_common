/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.system.EmailTypeReadOnly

/**
 * <p> Decorator for EmailTypeCompositeSevice</p>
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class EmailTypeDetails {

    @Delegate
    EmailTypeReadOnly emailTypesView

    EmailTypeDetails(EmailTypeReadOnly emailTypesView) {
        this.emailTypesView = emailTypesView
    }
}
