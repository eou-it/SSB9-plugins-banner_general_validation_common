/** *******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.system.Ethnicity
import net.hedtech.banner.general.system.ldm.v6.ReportingDecorator


/**
 * CDM decorator for ethnicities resource (/base/domain/ethnicities/ethnicities.json-schema)
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)

class EthnicityDetail {

    @Delegate
    private final Ethnicity ethnicity
    String guid
    String parentCategory
    Metadata metadata

    EthnicityDetail(Ethnicity ethnicity, String guid, String parentCategory, Metadata metadata) {
        this.ethnicity = ethnicity
        this.guid = guid
        this.parentCategory = parentCategory
        this.metadata = metadata
    }
    }
