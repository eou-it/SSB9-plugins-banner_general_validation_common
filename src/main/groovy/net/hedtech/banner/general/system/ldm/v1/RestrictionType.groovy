/** *******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v1

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.HoldType
import java.util.Map

import org.grails.datastore.gorm.GormValidateable
import grails.validation.Validateable
import org.grails.datastore.mapping.dirty.checking.DirtyCheckable


/**
 * CDM decorator for restriction-types resource
 */

class RestrictionType implements GormValidateable, DirtyCheckable, Validateable {

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

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        RestrictionType that = (RestrictionType) o

        if (org_grails_datastore_gorm_GormValidateable__skipValidate != that.org_grails_datastore_gorm_GormValidateable__skipValidate) return false
        if (this.getCategory() != that.getCategory()) return false
        if (guid != that.guid) return false
        if (holdType != that.holdType) return false
        if (metadata != that.metadata) return false
        if (org_grails_datastore_gorm_GormValidateable__errors != that.org_grails_datastore_gorm_GormValidateable__errors) return false
        if (org_grails_datastore_mapping_dirty_checking_DirtyCheckable__$changedProperties != that.org_grails_datastore_mapping_dirty_checking_DirtyCheckable__$changedProperties) return false

        return true
    }

    int hashCode() {
        int result
        result = (holdType != null ? holdType.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        result = 31 * result + (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (category != null ? category.hashCode() : 0)
        result = 31 * result + (org_grails_datastore_mapping_dirty_checking_DirtyCheckable__$changedProperties != null ? org_grails_datastore_mapping_dirty_checking_DirtyCheckable__$changedProperties.hashCode() : 0)
        result = 31 * result + (org_grails_datastore_gorm_GormValidateable__skipValidate ? 1 : 0)
        result = 31 * result + (org_grails_datastore_gorm_GormValidateable__errors != null ? org_grails_datastore_gorm_GormValidateable__errors.hashCode() : 0)
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
