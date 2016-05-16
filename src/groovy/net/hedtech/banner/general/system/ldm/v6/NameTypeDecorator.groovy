/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v6

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Decorator for "person-name-types" API
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class NameTypeDecorator {

    String id
    String title
    String code
    String category

    NameTypeDecorator(String id, String code, String title, String category) {
        this.id = id
        this.title = title
        this.code = code
        this.category = category
    }

    String getCategory() {
        if (!category || !NameTypeCategory.values().value*.capitalize().contains(category)) {
            return NameTypeCategory.PERSONAL.value
        } else if (category == NameTypeCategory.BIRTH.value.capitalize()) {
            return NameTypeCategory.BIRTH.value
        } else if (category == NameTypeCategory.LEGRAL.value.capitalize()) {
            return NameTypeCategory.LEGRAL.value
        }
    }
}
