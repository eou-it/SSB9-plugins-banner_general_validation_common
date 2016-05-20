/** *******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.HoldType


/**
 * CDM decorator for restriction-types resource
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class RestrictionType {

    @Delegate
    private final HoldType holdType
    Metadata metadata
    String guid
    // Version 6 Schema support -PERSON-HOLD-TYPES
    String category

    RestrictionType(HoldType holdType, String guid, Metadata metadata ) {
        this.holdType = holdType
        this.guid = guid
        this.metadata = metadata
          }

    //get the category for the PERSON-HOLD-TYPES
     String getCategory() {
        if (holdType.accountsReceivableHoldIndicator&&!holdType.registrationHoldIndicator&&!holdType.applicationHoldIndicator
                &&!holdType.complianceHoldIndicator&&!holdType.enrollmentVerificationHoldIndicator&&
                !holdType.gradeHoldIndicator&&!holdType.transcriptHoldIndicator&&!holdType.graduationHoldIndicator) {
            category = GeneralValidationCommonConstants.PERSON_HOLD_TYPES_FINANCE
        } else {
            category = GeneralValidationCommonConstants.PERSON_HOLD_TYPES_ACADEMIC
        }
        return category
    }
}
