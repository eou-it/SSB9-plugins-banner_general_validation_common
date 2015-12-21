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

    /** Common constants for AcademicDiscipline*/
    final static String ACADEMIC_DISCIPLINE = 'academicDiscipline'

    final static String ERROR_MSG_OPERATION_NOT_SUPPORTED = 'operation not supported'

    /** Common constants for Location Type*/
    final static String LOCATION_TYPE = 'locationType'
    final static String ORGANIZATION = 'organization'
    final static String PERSON = 'person'
    final static String ERROR_MSG_INVALID_GUID = 'locationType.invalidGuid'
}
