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

    // persons-holds API
    Map getDetail(){
        return [id:guid]
    }

    boolean equals(object) {
        if (this.is(object)) return true
        if (getClass() != object.class) return false
        if (!super.equals(object)) return false

        RestrictionType that = (RestrictionType) object

        if (category != that.category) return false
        if (guid != that.guid) return false
        if (holdType != that.holdType) return false
        if (metadata != that.metadata) return false

        return true
    }

    int hashCode() {
        int result = super.hashCode()
        result = 31 * result + holdType.hashCode()
        result = 31 * result + metadata.hashCode()
        result = 31 * result + guid.hashCode()
        result = 31 * result + category.hashCode()
        return result
    }


    @java.lang.Override
    public java.lang.String toString() {
        return "RestrictionType{" +
                "holdType=" + holdType +
                ", metadata=" + metadata +
                ", guid='" + guid + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
