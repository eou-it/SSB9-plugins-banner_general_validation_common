/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AcademicCredential
import net.hedtech.banner.general.system.Degree
import net.hedtech.banner.general.system.ldm.v4.AcademicCredentialDecorator
import net.hedtech.banner.general.system.ldm.v4.AcademicCredentialType
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional
/**
* <p> REST End point for Academic Credential Service. If we'll pass type is degree then , Academic Credential degree type of data will return.</p>
* <p> If we'll pass type is honorary then, Academic Credential honorary type of data will return.</p>
 * <p>If we'll pass type is diploma then, Academic Credential diploma type of data will return.</p>
 * <p>If we'll pass type is certificate then, Academic Credential certificate type of data will return.</p>
 * <p> else, It will return all  type of Academic Credential data.</p>
*/
@Transactional
class AcademicCredentialCompositeService extends LdmService {

    //Injection of transactional service
    def degreeService
    def academicCredentialService
    def supplementalDataService


    private static final List allowedSortFields = [GeneralValidationCommonConstants.DEFAULT_SORT_FIELD_ABBREVIATION,GeneralValidationCommonConstants.TYPE]

    /**
     * GET /api/academic-credentials
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    List<AcademicCredentialDecorator> list(Map params) {
        List<AcademicCredentialDecorator> academicCredentialsList=[]
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params.sort = params.sort ?: GeneralValidationCommonConstants.DEFAULT_SORT_FIELD_ABBREVIATION
        params.order = params.order ?: GeneralValidationCommonConstants.DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)?:params.sort
        fetchAcademicCredentialByCriteria(params).each { academicCredential ->
            academicCredentialsList << new AcademicCredentialDecorator(academicCredential.code,academicCredential.description,academicCredential.guid,academicCredential.type,academicCredential.suplementaryDesc)
        }
        return academicCredentialsList
    }

    /**
     * @return Count
     */
    @Transactional(readOnly = true)
    Long count(Map params) {
        fetchAcademicCredentialByCriteria(params,true)
    }

    /**
     * GET /api/academic-credentials/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    AcademicCredentialDecorator get(String guid) {
        AcademicCredential academicCredential = academicCredentialService.fetchByGuid(guid?.trim())
        if(!academicCredential){
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new NotFoundException())
        }
       return new AcademicCredentialDecorator(academicCredential.code,academicCredential.description,academicCredential.guid,academicCredential.type,academicCredential.suplementaryDesc)
    }

    /**
     * POST /api/academic-credentials
     *
     * @param content Request body
     */
    AcademicCredentialDecorator create(Map content) {
        validateRequest(content)
        Degree degree = degreeService.fetchByCode(content.code)
        if (degree) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS, null))
        }
        degree = bindAcademicCredential(new Degree(), content)
        String degreeGuid = content.guid?.trim()?.toLowerCase()
        if (degreeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            degreeGuid = updateGuidValue(degree.id, degreeGuid, GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL_LDM_NAME)?.guid
        } else {
            degreeGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL_LDM_NAME, degree.id)?.guid
        }
        if (supplementalDataService.hasSdeData(degree)) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS, null))
        }else {
            updateSupplementalFieldByModel(degree, content)
        }
        return new AcademicCredentialDecorator(degree.code, degree.description, degreeGuid, content.type, content.supplementalDesc)
    }


    /**
     * PUT /api/academic-credentials/<guid>
     *
     * @param content Request body
     * @return
     */
    def update(Map content) {
        String degreeGuid = content.id?.trim()?.toLowerCase()
        if(!degreeGuid){
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier  = GlobalUniqueIdentifier.fetchByGuid(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL_LDM_NAME,degreeGuid)
            if (!globalUniqueIdentifier) {
                if (!content.guid) {
                    content.guid = degreeGuid
                }
                //Per strategy when a GUID was provided, the create should happen.
                return create(content)
            }

        Degree degree = degreeService.get(globalUniqueIdentifier.domainId)
        if (!degree) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new NotFoundException())
        }

        // Should not allow to update Degree code as it is read-only
        if (degree.code != content.code?.trim()) {
            content.code = degree.code
        }

        validateRequest(content)
        degree = bindAcademicCredential(degree, content)
        updateSupplementalFieldByModel(degree, content)
        return new AcademicCredentialDecorator(degree.code,degree.description,degreeGuid,content.type,content.supplementalDesc)
    }

    /**
     * bind the Degree values
     * @param degree
     * @param content
     * @return
     */
    private def bindAcademicCredential(Degree degree, def content) {
        super.bindData(degree, content, [:])
        degree.displayWebIndicator = Boolean.FALSE
        degreeService.createOrUpdate(degree)
    }

    /**
     * Validate Request payload
     * @param content
     */
    private validateRequest(content) {
        if (!content.code) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        if (!content.description) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_DESCRIPTION_REQUIRED, null))
        }
        if (!AcademicCredentialType.values().value.contains(content.type)) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_TYPE_NOT_EXISTS, null))
        }
    }

    /**
     * fetch Academic Credential by Criteria
     * @param content
     * @param count
     * @return
     */
    private def fetchAcademicCredentialByCriteria(Map content, boolean count = false) {

        def result
        def filterMap = QueryBuilder.getFilterData(content)
        def params = filterMap.params
        def criteria = filterMap.criteria
        def pagingAndSortParams = filterMap.pagingAndSortParams

        if (content.containsKey(GeneralValidationCommonConstants.TYPE)) {
            params.put(GeneralValidationCommonConstants.TYPE, content.get(GeneralValidationCommonConstants.TYPE)?.trim())
            criteria.add([key: GeneralValidationCommonConstants.TYPE, binding: GeneralValidationCommonConstants.TYPE, operator: Operators.EQUALS])
        }
        if (count) {
            result = academicCredentialService.countAll([params: params, criteria: criteria])
        } else {
            result = academicCredentialService.fetchSearch([params: params, criteria: criteria], pagingAndSortParams)
        }

        return result
    }

    /**
     * Checks if SDE is enabled for that UI component.
     * Creates the Supplemental Field to AcademicCredentialDecorator
     * @param Degree model (degree object by criteria) to load SDE.
     * @param Map content
     */

    private void updateSupplementalFieldByModel(model, content) {

        def sdeModel = supplementalDataService.loadSupplementalDataForModel(model)

        if (sdeModel.containsKey(GeneralValidationCommonConstants.HEDM_CREDENTIAL_CATEGORY)) {
            sdeModel.HEDM_CREDENTIAL_CATEGORY."1".value = content.type
        }

        if (sdeModel.containsKey(GeneralValidationCommonConstants.HEDM_CREDENTIAL_DESCRIPTION)) {
            sdeModel.HEDM_CREDENTIAL_DESCRIPTION."1".value = content.supplementalDesc
        }

        supplementalDataService.persistSupplementalDataFor(model, sdeModel)

    }

}
