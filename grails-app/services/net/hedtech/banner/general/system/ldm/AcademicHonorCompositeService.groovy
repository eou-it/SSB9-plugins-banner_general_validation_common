/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AcademicHonorView
import net.hedtech.banner.general.system.ldm.v6.AcademicHonor
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Academic Honor Service. If we'll pass type is award then , Departmental-honor records will return .</p>
 * <p> If we'll pass type is distinction then, Institutional  Honor records will return else, Both of 2 Honor will return</p>
 */
class AcademicHonorCompositeService extends LdmService {

    private static final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V6]


    def academicHonorService
    public final Map ldmToDomainFieldMap = [
            award      : GeneralValidationCommonConstants.LDM_NAME_DEPARTMENTAL,
            distinction: GeneralValidationCommonConstants.LDM_NAME_INSTITUTIONAL
    ]

    /**
     * GET /api/academic-honors
     * @return List
     */
    @Transactional(readOnly = true)
    List<AcademicHonor> list(Map params) {
        String acceptVersion = getAcceptVersion(VERSIONS)

        List<AcademicHonor> academicHonors = []
        List<AcademicHonorView> academicHonorList
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params.offset = params.offset ?: 0
        if ((params.containsKey(GeneralValidationCommonConstants.ACADEMIC_HONOR_SEARCH_FIELD))) {
            def typeValue = ldmToDomainFieldMap[params.get(GeneralValidationCommonConstants.ACADEMIC_HONOR_SEARCH_FIELD)]?.trim()
            academicHonorList = academicHonorService.fetchByType(typeValue, params)
        } else {
            academicHonorList = academicHonorService.fetchAll(params)
        }

        academicHonorList.each { result ->
            academicHonors << new AcademicHonor(result)
        }
        return academicHonors
    }

    /**
     *
     * @return Long value as total count
     */
    Long count(params) {
        if (!params.type) {
            return academicHonorService.countRecord()
        } else {
            def typeValue = ldmToDomainFieldMap[params?.get(GeneralValidationCommonConstants.ACADEMIC_HONOR_SEARCH_FIELD)]?.trim()
            return academicHonorService.countRecordWithFilter(typeValue)
        }

    }

    /**
     * GET /api/academic-honors/{guid}* @param guid
     * @return
     */
    @Transactional(readOnly = true)
    AcademicHonor get(String guid) {
        String acceptVersion = getAcceptVersion(VERSIONS)

        AcademicHonorView academicHonorView = academicHonorService.fetchByGuid(guid?.toLowerCase()?.trim())
        if (!academicHonorView) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_HONOR, new NotFoundException())
        }
        new AcademicHonor(academicHonorView)
    }

}
