/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import org.springframework.transaction.annotation.Transactional
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.Degree
import net.hedtech.banner.general.system.ldm.v4.AcademicCredentialType
import net.hedtech.banner.general.system.ldm.v4.AcademicCredentials
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility

/**
* <p> REST End point for Academic Credential Service. If we'll pass type is degree then , Academic Credential degree type of data will return.</p>
* <p> If we'll pass type is honorary then, Academic Credential honorary type of data will return.</p>
 * <p>If we'll pass type is diploma then, Academic Credential diploma type of data will return.</p>
 * <p>If we'll pass type is certificate then, Academic Credential certificate type of data will return.</p>
 * <p> else, It will return all  type of Academic Credential data.</p>
*/
@Transactional
class AcademicCredentialsCompositeService {

    def degreeService
    
    private static final String DEFAULT_SORT_FIELD = 'abbreviation'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    private static final String PROCESS_CODE = "HEDM"
    private static final String SETTING_CREDENTIAL_TYPE = "CREDENTIALS.TYPE"
    private static final List allowedSortFields = [DEFAULT_SORT_FIELD]
    private static final String LDM_NAME ='academic-credentials'
    private final static HashMap searchFielddMap = [
            degree: '1',
            honorary:'2',
            diploma:'3',
            certificate:'4'
    ]
    /**
     * GET /api/academic-credentials
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    List<AcademicCredentials> list(Map params) {
        List<AcademicCredentials> academicCredentialsList=[]
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ? params?.sort : DEFAULT_SORT_FIELD
        params?.order = params?.order ? params?.order : DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
        List<Degree> degreeList=getDataFromDB(false, params)
        degreeList.each { degree ->
            academicCredentialsList << new AcademicCredentials(degree, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, degree.id)?.guid,new Metadata(degree.dataOrigin),populateTypeValue(degree.degreeType))
        }
        return academicCredentialsList
    }

    /**
     * @return Count
     */
    @Transactional(readOnly = true)
    Long count(Map params) {
        getDataFromDB(true, params)
    }

    /**
     * GET /api/academic-disciplines/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    AcademicCredentials get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByGuid(guid?.trim())
        if(!globalUniqueIdentifier){
            throw new ApplicationException("academicCredential", new NotFoundException())
        }else if (globalUniqueIdentifier && globalUniqueIdentifier.ldmName!=LDM_NAME) {
            throw new RestfulApiValidationException("academicCredential.invalidGuid")
        }
            Degree degree = degreeService.get(globalUniqueIdentifier.domainId)
            if (!degree) {
                throw new ApplicationException("academicCredential", new NotFoundException())
            }
        new AcademicCredentials(degree, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, degree.id)?.guid,new Metadata(degree.dataOrigin),populateTypeValue(degree.degreeType))
    }

    private def getDataFromDB(Boolean count, Map params) {
        if (count) {
          return  params?.type ? params.type==AcademicCredentialType.DEGREE.name().toLowerCase()? Degree.countByDegreeTypeOrDegreeTypeIsNull(searchFielddMap[params.type]?:params.type):Degree.countByDegreeType(searchFielddMap[params.type]?:params.type) : degreeService.count()
        } else {
           return  params?.type ? params.type==AcademicCredentialType.DEGREE.name().toLowerCase()? Degree.findAllByDegreeTypeOrDegreeTypeIsNull(searchFielddMap[params.type]?:params.type,params):Degree.findAllByDegreeType(searchFielddMap[params.type]?:params.type,params) : degreeService.list(params)
        }
    }

    private def populateTypeValue(String type){
        String dType
        switch (type?:IntegrationConfiguration.findByProcessCodeAndSettingName(PROCESS_CODE, SETTING_CREDENTIAL_TYPE)?.value){
            case AcademicCredentialType.DEGREE.value : dType=AcademicCredentialType.DEGREE.name().toLowerCase()
                break
            case AcademicCredentialType.DIPLOMA.value : dType= AcademicCredentialType.DIPLOMA.name().toLowerCase()
                break
            case AcademicCredentialType.HONORARY.value: dType=AcademicCredentialType.HONORARY.name().toLowerCase()
                break
            case AcademicCredentialType.CERTIFICATE.value: dType=AcademicCredentialType.CERTIFICATE.name().toLowerCase()
                break
        }
        dType
    }
}
