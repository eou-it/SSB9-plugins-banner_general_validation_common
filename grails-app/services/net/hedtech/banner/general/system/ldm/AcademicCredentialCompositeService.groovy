/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
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

    private static final List allowedSortFields = [GeneralValidationCommonConstants.DEFAULT_SORT_FIELD_ABBREVIATION,GeneralValidationCommonConstants.TYPE]
    private static final typeList = ['degree', 'honorary', 'diploma', 'certificate']

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
            academicCredentialsList << new AcademicCredentialDecorator(academicCredential.code,academicCredential.description,academicCredential.guid,academicCredential.type)
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
       return new AcademicCredentialDecorator(academicCredential.code,academicCredential.description,academicCredential.guid,academicCredential.type)
    }

    /**
     * POST /api/academic-credentials
     *
     * @param content Request body
     */
    AcademicCredentialDecorator create(Map content) {
        Degree degree = degreeService.fetchByCode(content.code)
        if (degree) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_EXISTS, null))
        }
        if (typeList.contains(content.type)) {
            content.degreeType = AcademicCredentialType.("${content.type}")?.value
        }
        degree = bindAcademicCredential(new Degree(), content)
        String degreeGuid = content.guid?.trim()?.toLowerCase()
        if (degreeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            degreeGuid = updateGuidValue(degree.id, degreeGuid, GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL_LDM_NAME)?.guid
        } else {
            degreeGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL_LDM_NAME, degree.id)?.guid
        }
        return new AcademicCredentialDecorator(degree.code, degree.description, degreeGuid, content.type)
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
         AcademicCredential academicCredential  = academicCredentialService.fetchByGuid(degreeGuid)
            if (!academicCredential) {
                if (!content.guid) {
                    content.guid = degreeGuid
                }
                //Per strategy when a GUID was provided, the create should happen.
                return create(content)
            }

        Degree degree = degreeService.get(academicCredential.id)
        if (!degree) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_CREDENTIAL, new NotFoundException())
        }

        // Should not allow to update instructional-methods.code as it is read-only
        if (degree.code != content.code?.trim()) {
            content.code = degree.code
        }
        content.degreeType = degree.degreeType

        degree = bindAcademicCredential(degree, content)
        return new AcademicCredentialDecorator(degree.code,degree.description,degreeGuid,academicCredential.type)
    }


    private def bindAcademicCredential(Degree degree, def content) {
        bindData(degree, content)
        degreeService.createOrUpdate(degree)
    }

    private void bindData(domainModel, content) {
        super.bindData(domainModel, content, [:])
        domainModel.degreeType = content.degreeType
        domainModel.displayWebIndicator = Boolean.FALSE
    }

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
}
