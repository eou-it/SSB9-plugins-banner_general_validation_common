/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AcademicDisciplineView
import net.hedtech.banner.general.system.CIPCode
import net.hedtech.banner.general.system.MajorMinorConcentration
import net.hedtech.banner.general.system.MajorMinorConcentrationService
import net.hedtech.banner.general.system.ldm.v4.AcademicDisciplineType
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


abstract class AbstractAcademicDisciplineCompositeService extends LdmService {

    AcademicDisciplineSearchService academicDisciplineSearchService


    abstract protected def getAllowedSortFields()

    abstract protected Map processListApiRequest(final Map requestParams)

    abstract protected def createAcademicDisciplineDataModel(final def dataMapForAcademicDiscipline)

    abstract protected def extractDataFromRequestBody(Map content)

    def CIPCodeService
    MajorMinorConcentrationService majorMinorConcentrationService

    private static final String ACADEMIC_DISCIPLINE_HEDM = 'academic-disciplines'



    /**
     * GET /api/academic-disciplines
     *
     * @param requestParams
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected def listApi(Map requestParams) {
        setPagingParams(requestParams)

        setSortingParams(requestParams)

        Map requestProcessingResult = processListApiRequest(requestParams)

        injectTotalCountIntoParams(requestParams, requestProcessingResult)

        return createAcademicDisciplineDataModels(requestProcessingResult)
    }


    /**
     * GET /api/academic-disciplines
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @param requestParams
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected def count(Map requestParams) {
        return getInjectedPropertyFromParams(requestParams, "totalCount")
    }


    /**
     * GET /api/academic-disciplines/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected def get(String guid) {
        def academicDisciplines = academicDisciplineSearchService.fetchByGuid(guid?.trim())
        if (!academicDisciplines) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new NotFoundException())
        }
        Map requestProcessingResult = [:]
        requestProcessingResult.put('academicDisciplines', academicDisciplines)
        return createAcademicDisciplineDataModels(requestProcessingResult)[0]
    }


    /**
     * POST /api/academic-disciplines
     *
     * @param content Request body
     */
    def create(Map content) {
        Map requestData = extractDataFromRequestBody(content)

        Map dataMap = [:]

        String majorMinorConcentrationGuid
        if (requestData.containsKey("academicDisciplineGuid") && requestData.get("academicDisciplineGuid").length() > 0) {
            majorMinorConcentrationGuid = requestData.get('academicDisciplineGuid')

        }

        String academicDisciplineCode
        if (requestData.containsKey("academicDisciplineCode") && requestData.get("academicDisciplineCode").length() > 0) {
            academicDisciplineCode = requestData.get('academicDisciplineCode')
            dataMap.put("academicDisciplineCode",academicDisciplineCode)
        }

        String type
        if (requestData.containsKey("type") && requestData.get("type").length() > 0) {
            type = requestData.get('type')
            dataMap.put("type",type)
        }

        String description
        if (requestData.containsKey("description") && requestData.get("description").length() > 0) {
            description = requestData.get('description')
            dataMap.put("description",description)
        }

        CIPCode cipcCode
        if (requestData.containsKey("cipcCode") && requestData.get("cipcCode").length() > 0) {
            String code = requestData.get('cipcCode')
            cipcCode = CIPCodeService.fetchByCode(code)?.get(0)
            dataMap.put("cipcCode",cipcCode)
        }

        if (globalUniqueIdentifierService.fetchByLdmNameAndGuid(ACADEMIC_DISCIPLINE_HEDM, majorMinorConcentrationGuid)) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE, null))
        }

        MajorMinorConcentration majorMinorConcentration = majorMinorConcentrationService.fetchByCode(academicDisciplineCode)
        if (!majorMinorConcentration) {
            // if code in the request does not exists - create the record
            majorMinorConcentration = createUpdateMajorMinorConcentration(new MajorMinorConcentration(), dataMap)
            majorMinorConcentrationGuid = updateGuidIfExist(majorMinorConcentration, dataMap, majorMinorConcentrationGuid)
        }
        // if code in the request exists - then check with the type
        else if (AcademicDisciplineView.findByCodeAndType(academicDisciplineCode, type)) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE, null))
        } else {
            majorMinorConcentration = createUpdateMajorMinorConcentration(majorMinorConcentration, dataMap)
            majorMinorConcentrationGuid = updateGuidIfExist(majorMinorConcentration, dataMap, majorMinorConcentrationGuid)
        }


        Map dataMapForAcademicDiscipline = [:]

        dataMapForAcademicDiscipline << ["code": majorMinorConcentration.code]
        dataMapForAcademicDiscipline << ["description": majorMinorConcentration.description]
        dataMapForAcademicDiscipline << ["guid": majorMinorConcentrationGuid]
        dataMapForAcademicDiscipline << ["type": type]
        if (cipcCode) {
            dataMapForAcademicDiscipline << ["cipcCode": cipcCode.code]
        }
        return dataMapForAcademicDiscipline



    }


    /**
     * PUT /api/courses/<guid>
     *
     * @param content Request body
     */
    def update(Map content) {
        Map requestData = extractDataFromRequestBody(content)
        Map dataMap = [:]

        String academicDisciplineGuid
        if (requestData.containsKey("academicDisciplineGuid") && requestData.get("academicDisciplineGuid").length() > 0) {
            academicDisciplineGuid = requestData.get('academicDisciplineGuid')
        }else{
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new NotFoundException())
        }

        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(ACADEMIC_DISCIPLINE_HEDM, academicDisciplineGuid)
        if (!globalUniqueIdentifier) {
            //Per strategy when a ID was provided, the create should happen.
            return create(content)
        }
        String code = globalUniqueIdentifier?.domainKey.split('\\^')[0]
        String type = globalUniqueIdentifier?.domainKey.split('\\^')[1]
        dataMap.put("academicDisciplineCode",code)
        dataMap.put("type",type)
        MajorMinorConcentration majorMinorConcentration = majorMinorConcentrationService.fetchByCode(code?.trim())
        if (!majorMinorConcentration) {
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new NotFoundException())
        }

        String academicDisciplineCodeInRequest
        if (requestData.containsKey("academicDisciplineCode") && requestData.get("academicDisciplineCode").length() > 0) {
            academicDisciplineCodeInRequest = requestData.get('academicDisciplineCode')
        }

        String typeInRequest
        if (requestData.containsKey("type") && requestData.get("type").length() > 0) {
            typeInRequest = requestData.get('type')
        }

        String descriptionInRequest
        if (requestData.containsKey("description") && requestData.get("description").length() > 0) {
            dataMap.put("description",requestData.get('description'))
        }else if (requestData.containsKey("description") && requestData.get("description").length() == 0){
            dataMap.put("description",null)
        }

        CIPCode cipcCode
        if (requestData.containsKey("cipcCode") && requestData.get("cipcCode").length() > 0) {
            String codeInRequest = requestData.get('cipcCode')
            cipcCode = CIPCodeService.fetchByCode(codeInRequest)?.get(0)
            dataMap.put("cipcCode",cipcCode)
        } else if (requestData.containsKey("cipcCode") && requestData.get("cipcCode").length() == 0) {
            dataMap.put("cipcCode",null)
        }

        if (code != academicDisciplineCodeInRequest) {
            dataMap.put("academicDisciplineCode",code)
        }
        if (type != typeInRequest) {
            dataMap.put("type",type)
        }

        majorMinorConcentration = createUpdateMajorMinorConcentration(majorMinorConcentration, dataMap)

        Map dataMapForAcademicDiscipline = [:]
        dataMapForAcademicDiscipline << ["code": majorMinorConcentration.code]
        dataMapForAcademicDiscipline << ["description": majorMinorConcentration.description]
        dataMapForAcademicDiscipline << ["guid": academicDisciplineGuid]
        dataMapForAcademicDiscipline << ["type": type]
        if (cipcCode) {
            dataMapForAcademicDiscipline << ["cipcCode": cipcCode?.code]
        }
        return dataMapForAcademicDiscipline

    }


    /**
     * Checking the id - if id is being sent in the request payload - update the id else return the generated id
     */
    private String updateGuidIfExist(MajorMinorConcentration majorMinorConcentration, Map dataMap, String majorMinorConcentrationGuid = null) {
        // if id being sent in the request - update the guid else return the generated guid
        if (majorMinorConcentrationGuid) {
            majorMinorConcentrationGuid = updateGuidValueByDomainKey(majorMinorConcentration.code + "^" + dataMap.get("type"), majorMinorConcentrationGuid, ACADEMIC_DISCIPLINE_HEDM)?.guid
        } else {
            majorMinorConcentrationGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey(ACADEMIC_DISCIPLINE_HEDM, majorMinorConcentration.code + "^" + dataMap.get("type"))?.guid
        }
        majorMinorConcentrationGuid
    }

    /**
     * Invoking the LDM service to bind map properties onto grails domains.
     * Invoking the ServiceBase to creates or updates a model instance provided within the supplied domainModelOrMap.
     */
    def createUpdateMajorMinorConcentration(MajorMinorConcentration majorMinorConcentration, Map dataMap) {
        majorMinorConcentration.code = dataMap.get("academicDisciplineCode")
        if(dataMap.containsKey("description")){
            majorMinorConcentration.description = dataMap.get("description")
        }
        if(dataMap.containsKey("cipcCode")){
            majorMinorConcentration.cipcCode = dataMap.get("cipcCode")
        }
        switch (dataMap?.get("type")) {
            case AcademicDisciplineType.MAJOR.value:
                majorMinorConcentration.validMajorIndicator = true
                break
            case AcademicDisciplineType.MINOR.value:
                majorMinorConcentration.validMinorIndicator = true
                break
            case AcademicDisciplineType.CONCENTRATION.value:
                majorMinorConcentration.validConcentratnIndicator = true
                break
        }
       return majorMinorConcentrationService.createOrUpdate(majorMinorConcentration)
    }



    protected void setPagingParams(Map requestParams) {
        RestfulApiValidationUtility.correctMaxAndOffset(requestParams, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        if (!requestParams.containsKey("offset")) {
            requestParams.put("offset", "0")
        }
    }

    protected void setSortingParams(Map requestParams) {
        def allowedSortFields = getAllowedSortFields()

        if (requestParams.containsKey("sort")) {
            RestfulApiValidationUtility.validateSortField(requestParams.sort, allowedSortFields)
            requestParams.sort = LdmService.fetchBannerDomainPropertyForLdmField(requestParams.sort) ?: requestParams.sort
        }

        if (requestParams.containsKey("order")) {
            RestfulApiValidationUtility.validateSortOrder(requestParams.order)
        } else {
            requestParams.put('order', "asc")
        }
    }


    private void injectTotalCountIntoParams(Map requestParams, Map requestProcessingResult) {
        if (requestProcessingResult.containsKey("totalCount")) {
            log.debug("X-Total-Count = ${requestProcessingResult.totalCount}")
            injectPropertyIntoParams(requestParams, "totalCount", requestProcessingResult.totalCount)
        }
    }


    private void injectPropertyIntoParams(Map params, String propName, def propVal) {
        def injectedProps = [:]
        if (params.containsKey("academicDiscipline-injected") && params.get("academicDiscipline-injected") instanceof Map) {
            injectedProps = params.get("academicDiscipline-injected")
        } else {
            params.put("academicDiscipline-injected", injectedProps)
        }
        injectedProps.putAt(propName, propVal)
    }


    private def getInjectedPropertyFromParams(Map params, String propName) {
        def propVal
        def injectedProps = params.get("academicDiscipline-injected")
        if (injectedProps instanceof Map && injectedProps.containsKey(propName)) {
            propVal = injectedProps.get(propName)
        }
        return propVal
    }

    protected def createAcademicDisciplineDataModels(final Map requestProcessingResult) {
        def decorators = []

        List<AcademicDisciplineView> academicDisciplines = requestProcessingResult.get("academicDisciplines")
        if (academicDisciplines) {
            academicDisciplines?.each { academicDiscipline ->
                def dataMapForAcademicDiscipline = prepareDataMapForAcademicDiscipline(academicDiscipline)
                decorators << createAcademicDisciplineDataModel(dataMapForAcademicDiscipline)
            }
        }

        return decorators
    }


    private def prepareDataMapForAcademicDiscipline(AcademicDisciplineView academicDiscipline) {
        Map dataMapForAcademicDiscipline = [:]

        // code
        if (academicDiscipline.code) {
            dataMapForAcademicDiscipline << ["code": academicDiscipline.code]
        }
        // description
        if (academicDiscipline.description) {
            dataMapForAcademicDiscipline << ["description": academicDiscipline.description]
        }
        // code
        if (academicDiscipline.id) {
            dataMapForAcademicDiscipline << ["guid": academicDiscipline.id]
        }
        // code
        if (academicDiscipline.type) {
            dataMapForAcademicDiscipline << ["type": academicDiscipline.type]
        }
        // code
        if (academicDiscipline.cipcCode) {
            dataMapForAcademicDiscipline << ["cipcCode": academicDiscipline.cipcCode]
        }

        return dataMapForAcademicDiscipline
    }




}
