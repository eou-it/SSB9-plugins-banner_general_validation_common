/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/

package net.hedtech.banner.general.common

/**
 * Util class for General validation module where in  will be
 * This class will holds all common variables used across the banner-general-validation-common plugin.
 */

class GeneralValidationCommonConstants {

    private GeneralValidationCommonConstants() {} // prevents instantiation

    /** Common constants */
    final static String TYPE = 'type'
    final static String CODE = 'code'
    final static String ERROR_MSG_EXISTS_MESSAGE = 'exists.message'
    final static String ERROR_MSG_CODE_REQUIRED = 'code.required.message'
    final static String ERROR_MSG_CODE_EXISTS = 'code.exists.message'
    final static String DEFAULT_ORDER_TYPE = 'ASC'
    final static String DEFAULT_SORT_FIELD_ABBREVIATION = 'abbreviation'
    final static String DEFAULT_SORT_FIELD_CODE = 'code'

    /** Common constants for AcademicDiscipline*/
    final static String ACADEMIC_DISCIPLINE = 'academicDiscipline'

    final static String ERROR_MSG_OPERATION_NOT_SUPPORTED = 'operation not supported'

    /** Common constants for Location Type*/
    final static String LOCATION_TYPE = 'locationType'
    final static String ORGANIZATION = 'organization'
    final static String PERSON = 'person'
    final static String ERROR_MSG_INVALID_GUID = 'locationType.invalidGuid'

    /** Common constants for EmailType*/
    final static String PERSONAL = 'personal'
    final static String BUSINESS = 'business'
    final static String SCHOOL = 'school'
    final static String OTHERS = 'other'
    final static String PARENT = 'parent'
    final static String FAMILY = 'family'
    final static String SALES = 'sales'
    final static String SUPPORT = 'support'
    final static String GENERAL = 'general'
    final static String BILLING = 'billing'
    final static String LEGAL = 'legal'
    final static String HR = 'hr'
    final static String MEDIA = 'media'
    final static String MATCHINGGIFTS = 'matchingGifts'
    final static String EMAIL_TYPE = 'emailType'
}
