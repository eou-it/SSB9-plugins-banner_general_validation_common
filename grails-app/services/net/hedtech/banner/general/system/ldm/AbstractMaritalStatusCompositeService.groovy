/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.IntegrationConfiguration
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.MaritalStatusService
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility

abstract class AbstractMaritalStatusCompositeService extends LdmService {

    MaritalStatusService maritalStatusService


    abstract protected def getAllowedSortFields()


    abstract protected def processListApiRequest(final Map requestParams)


    abstract protected void prepareDataMapForAll_ListExtension(Map dataMapForAll)


    abstract protected void prepareDataMapForSingle_ListExtension(final Map dataMapForAll, Map dataMapForSingle)


    abstract protected def createMaritalStatusDataModel(final Map dataMapForSingle)


    abstract protected def count(final Map requestParams)


    abstract protected boolean isMaritalStatusEligibleForShow(final MaritalStatus maritalStatus)


    abstract protected def extractDataFromRequestBody(final Map content)


    abstract protected void throwMaritalStatusAlreadyExistsException()


    protected def list(Map requestParams) {
        setPagingParams(requestParams)

        setSortingParams(requestParams)

        def rows = processListApiRequest(requestParams)

        return createMaritalStatusDataModels(rows)
    }


    protected def get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        MaritalStatus maritalStatus = maritalStatusService.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        if (!isMaritalStatusEligibleForShow(maritalStatus)) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        return createMaritalStatusDataModels([[maritalStatus: maritalStatus, globalUniqueIdentifier: globalUniqueIdentifier]])[0]
    }


    protected def create(Map content) {
        Map requestData = extractDataFromRequestBody(content)

        String msGuid = requestData.get('msGuid')

        MaritalStatus maritalStatus = maritalStatusService.fetchByCode(requestData.get('code'))
        if (maritalStatus) {
            throwMaritalStatusAlreadyExistsException()
        }
        maritalStatus = new MaritalStatus()

        bindData(maritalStatus, requestData, [:])
        maritalStatus.financeConversion = "1"
        maritalStatusService.createOrUpdate(maritalStatus)

        if (msGuid && msGuid != GeneralValidationCommonConstants.NIL_GUID) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(maritalStatus.id, msGuid, GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME)
        } else {
            msGuid = globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, maritalStatus.id)?.guid
        }
        log.debug("GUID: ${msGuid}")

        return prepareDataMapForSingle_Create(msGuid, maritalStatus)
    }


    protected def update(Map content) {
        Map requestData = extractDataFromRequestBody(content)

        String msGuid = requestData.get('msGuid')

        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.MARITAL_STATUS_LDM_NAME, msGuid)
        if (!globalUniqueIdentifier) {
            //Per strategy when a GUID was provided, the create should happen
            return create(content)
        }

        MaritalStatus maritalStatus = maritalStatusService.get(globalUniqueIdentifier.domainId)
        if (!maritalStatus) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new NotFoundException())
        }

        // Should not allow to update maritalStatus.code as it is read-only
        if (maritalStatus.code != requestData.get('code')) {
            requestData.put('code', maritalStatus.code)
        }

        bindData(maritalStatus, requestData, [:])
        maritalStatusService.createOrUpdate(maritalStatus)

        return prepareDataMapForSingle_Create(msGuid, maritalStatus)
    }


    private def createMaritalStatusDataModels(final def rows) {
        def decorators = []

        if (rows) {
            def dataMapForAll = prepareDataMapForAll_List()

            rows?.each {
                MaritalStatus maritalStatus = it.maritalStatus
                GlobalUniqueIdentifier globalUniqueIdentifier = it.globalUniqueIdentifier
                def dataMapForSingle = prepareDataMapForSingle_List(globalUniqueIdentifier.guid, maritalStatus, dataMapForAll)
                decorators << createMaritalStatusDataModel(dataMapForSingle)
            }
        }

        return decorators
    }


    private def prepareDataMapForAll_List() {
        def dataMapForAll = [:]

        // Call extension
        prepareDataMapForAll_ListExtension(dataMapForAll)

        return dataMapForAll
    }


    private
    def initDataMapForSingle(String sourceForDataMap, String maritalStatusGuid, MaritalStatus maritalStatus) {
        Map dataMapForSingle = [:]
        dataMapForSingle.put("sourceForDataMap", sourceForDataMap)
        dataMapForSingle.put("maritalStatusGuid", maritalStatusGuid)
        dataMapForSingle.put("maritalStatus", maritalStatus)
        return dataMapForSingle
    }


    private def prepareDataMapForSingle_List(String maritalStatusGuid, MaritalStatus maritalStatus,
                                             final Map dataMapForAll) {
        Map dataMapForSingle = initDataMapForSingle("LIST", maritalStatusGuid, maritalStatus)

        // Call extension
        prepareDataMapForSingle_ListExtension(dataMapForAll, dataMapForSingle)

        return dataMapForSingle
    }


    private def prepareDataMapForSingle_Create(String maritalStatusGuid, MaritalStatus maritalStatus) {
        Map dataMapForSingle = initDataMapForSingle("CREATE", maritalStatusGuid, maritalStatus)

        return dataMapForSingle
    }


    protected def getBannerMaritalStatusCodeToHedmMaritalStatusCategoryMap(String settingName, String version) {
        Map<String, String> bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap = [:]
        List<IntegrationConfiguration> intConfs = findAllByProcessCodeAndSettingName(GeneralValidationCommonConstants.PROCESS_CODE, settingName)
        log.debug "List of IntegrationConfiguration objects"
        log.debug intConfs
        if (intConfs) {
            List<MaritalStatus> entities = maritalStatusService.fetchAllByCodeInList(intConfs.value)
            intConfs.each {
                MaritalStatusCategory maritalStatusCategory = MaritalStatusCategory.getByDataModelValue(it.translationValue, version)
                if (entities.code.contains(it.value) && maritalStatusCategory) {
                    bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap.put(it.value, maritalStatusCategory.versionToEnumMap[version])
                }
            }
        }
        if (bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap.isEmpty()) {
            throw new ApplicationException(this.class, new BusinessLogicValidationException("goriccr.not.found.message", [settingName]))
        }
        return bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap
    }


    protected void setPagingParams(Map requestParams) {
        RestfulApiValidationUtility.correctMaxAndOffset(requestParams, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
    }


    protected void setSortingParams(Map requestParams) {
        def allowedSortFields = getAllowedSortFields()

        if (requestParams.containsKey("sort")) {
            RestfulApiValidationUtility.validateSortField(requestParams.sort, allowedSortFields)
        }

        if (requestParams.containsKey("order")) {
            RestfulApiValidationUtility.validateSortOrder(requestParams.order)
        } else {
            requestParams.put('order', "asc")
        }
    }

}
