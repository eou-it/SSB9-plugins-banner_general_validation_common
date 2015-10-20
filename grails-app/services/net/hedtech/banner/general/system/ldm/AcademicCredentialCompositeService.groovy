/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AcademicCredentialView
import net.hedtech.banner.general.system.Degree
import net.hedtech.banner.general.system.ldm.v4.AcademicCredential
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

    private static final String DEFAULT_SORT_FIELD = 'abbreviation'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    private static final List allowedSortFields = [DEFAULT_SORT_FIELD,'type']
    private static final String LDM_NAME = 'academic-credentials'
    private static final typeList = ['degree', 'honorary', 'diploma', 'certificate']

    /**
     * GET /api/academic-credentials
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    List<AcademicCredential> list(Map params) {
        List<AcademicCredential> academicCredentialsList=[]
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ?: DEFAULT_SORT_FIELD
        params?.order = params?.order ?: DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)?:params.sort
        List<AcademicCredentialView> academicCredentialList=fetchAcademicCredentialByCriteria(params)
        academicCredentialList.each { academicCredential ->
            academicCredentialsList << new AcademicCredential(academicCredential?.code,academicCredential?.description,academicCredential?.guid,academicCredential?.type)
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
    AcademicCredential get(String guid) {
        AcademicCredentialView academicCredential = AcademicCredentialView.findByGuid(guid?.trim())
        if(!academicCredential){
            throw new ApplicationException("academicCredential", new NotFoundException())
        }
       return new AcademicCredential(academicCredential?.code,academicCredential?.description,academicCredential?.guid,academicCredential?.type)
    }

    /**
     * POST /api/academic-credentials
     *
     * @param content Request body
     */
    AcademicCredential create(Map content) {
        Degree degree = Degree.findByCode(content?.code)
        if (degree) {
            throw new ApplicationException('academicCredential', new BusinessLogicValidationException('code.exists.message', null))
        }
        if (typeList.contains(content?.type)) {
            content.put("degreeType", AcademicCredentialType.("${content?.type}").value)
        }
        degree = bindAcademicCredential(new Degree(), content)
        String degreeGuid = content?.guid?.trim()?.toLowerCase()
        if (degreeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            degreeGuid = updateGuidValue(degree.id, degreeGuid, LDM_NAME)?.guid
        } else {
            degreeGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, degree?.id)?.guid
        }
        return new AcademicCredential(degree?.code, degree?.description, degreeGuid, content?.type)
    }

    /**
     * PUT /api/academic-credentials/<guid>
     *
     * @param content Request body
     * @return
     */
    def update(Map content) {
        String degreeGuid = content?.id?.trim()?.toLowerCase()

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, degreeGuid)
        if (degreeGuid) {
            if (!globalUniqueIdentifier) {
                if (!content.get('guid')) {
                    content.put('guid', degreeGuid)
                }
                //Per strategy when a GUID was provided, the create should happen.
                return create(content)
            }
        } else {
            throw new ApplicationException("academicCredential", new NotFoundException())
        }

        Degree degree = Degree.findById(globalUniqueIdentifier?.domainId)
        if (!degree) {
            throw new ApplicationException("academicCredential", new NotFoundException())
        }

        // Should not allow to update instructional-methods.code as it is read-only
        if (degree?.code != content?.code?.trim()) {
            content.put("code", degree?.code)
        }
        if (!typeList.contains(content?.type) || degree?.degreeType != AcademicCredentialType.("${content?.type}").value) {
            content.put("degreeType", degree?.degreeType)
        }
        degree = bindAcademicCredential(degree, content)

        return new AcademicCredential(degree?.code,degree?.description,degreeGuid,content?.type)
    }


    private def bindAcademicCredential(Degree degree, def content) {
        bindData(degree, content)
        degreeService.createOrUpdate(degree)
    }

    private void bindData(domainModel, content) {
        super.bindData(domainModel, content, [:])
        domainModel.degreeType = content?.degreeType
        domainModel.displayWebIndicator = Boolean.FALSE
    }

    private def fetchAcademicCredentialByCriteria(Map content, boolean count = false) {

        def result
        def filterMap = QueryBuilder.getFilterData(content)
        def params = filterMap.params
        def criteria = filterMap.criteria
        def pagingAndSortParams = filterMap.pagingAndSortParams

        if (content.containsKey("type")) {
            params.put("type", content.get("type").trim())
            criteria.add([key: "type", binding: "type", operator: Operators.EQUALS])
        }
        if (count) {
            result = AcademicCredentialView.countAll([params: params, criteria: criteria])
        } else {
            result = AcademicCredentialView.fetchSearch([params: params, criteria: criteria], pagingAndSortParams)
        }

        return result
    }
}
