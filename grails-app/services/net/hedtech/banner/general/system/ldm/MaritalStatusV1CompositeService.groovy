/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.MaritalStatus
import net.hedtech.banner.general.system.ldm.v1.MaritalStatusDetail
import net.hedtech.banner.general.system.ldm.v1.Metadata

class MaritalStatusV1CompositeService extends AbstractMaritalStatusCompositeService {

    def getBannerMaritalStatusCodeToHedmMaritalStatusCategoryMap() {
        return getBannerMaritalStatusCodeToHedmMaritalStatusCategoryMap(GeneralValidationCommonConstants.MARITAL_STATUS_PARENT_CATEGORY, GeneralValidationCommonConstants.VERSION_V1)
    }


    MaritalStatusDetail createMaritalStatusDataModel(
            final String maritalStatusGuid,
            final MaritalStatus maritalStatus, final def bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap) {
        return new MaritalStatusDetail(maritalStatus, maritalStatusGuid, bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap.get(maritalStatus.code), new Metadata(maritalStatus.dataOrigin))
    }


    protected def getAllowedSortFields() {
        return [GeneralValidationCommonConstants.ABBREVIATION, GeneralValidationCommonConstants.TITLE]
    }


    protected def processListApiRequest(final Map requestParams) {
        String sortField = requestParams.sort?.trim()
        String domainSortField = getDataModelPropertyToMaritalStatusDomainFieldMap()[sortField]
        if (domainSortField) {
            sortField = domainSortField
        }
        String sortOrder = requestParams.order?.trim()

        int max = requestParams.max?.trim()?.toInteger()
        int offset = requestParams.offset?.trim()?.toInteger()

        return maritalStatusService.fetchAllWithGuid(sortField, sortOrder, max, offset)
    }


    protected void prepareDataMapForAll_ListExtension(Map dataMapForAll) {
        dataMapForAll.put("bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap", getBannerMaritalStatusCodeToHedmMaritalStatusCategoryMap())
    }


    protected void prepareDataMapForSingle_ListExtension(
            final Map dataMapForAll, Map dataMapForSingle) {
        dataMapForSingle << ["bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap": dataMapForAll.bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap]
    }


    protected def createMaritalStatusDataModel(final Map dataMapForSingle) {
        String maritalStatusGuid = dataMapForSingle.get("maritalStatusGuid")
        MaritalStatus maritalStatus = dataMapForSingle.get("maritalStatus")

        if (dataMapForSingle.get("sourceForDataMap") == "CREATE") {
            def dataMapForAll = prepareDataMapForAll_List()
            dataMapForSingle.putAll(prepareDataMapForSingle_List(maritalStatusGuid, maritalStatus, dataMapForAll))
        }

        return createMaritalStatusDataModel(maritalStatusGuid, maritalStatus, dataMapForSingle.bannerMaritalStatusCodeToHedmMaritalStatusCategoryMap)
    }


    protected def count(final Map requestParams) {
        return maritalStatusService.count(requestParams)
    }


    protected boolean isMaritalStatusEligibleForShow(final MaritalStatus maritalStatus) {
        return true
    }


    protected def extractDataFromRequestBody(final Map content) {
        def requestData = [:]

        String guidInPayload
        if (content.containsKey("guid") && content.get("guid") instanceof String) {
            guidInPayload = content?.guid?.trim()?.toLowerCase()
            if (guidInPayload) {
                requestData.put('msGuid', guidInPayload)
            }
        }

        // UPDATE operation - API SHOULD prefer the resource identifier on the URI, over the payload.
        String guidInURI = content?.id?.trim()?.toLowerCase()
        if (guidInURI && !guidInURI.equals(guidInPayload)) {
            content.put('guid', guidInURI)
            requestData.put('msGuid', guidInURI)
        }

        /** Required in DataModel - Required in Banner **/

        String desc
        if (content.containsKey("description") && content.get("description") instanceof String) {
            desc = content?.description?.trim()
        }
        if (!desc) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_DESCRIPTION_REQUIRED, null))
        }
        requestData.put('description', desc)

        /** Optional in DataModel - Required in Banner **/

        String code
        if (content.containsKey("code") && content.get("code") instanceof String) {
            code = content?.code?.trim()
        }
        if (!code) {
            throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, [GeneralValidationCommonConstants.ABBREVIATION.capitalize()]))
        }
        requestData.put('code', code)

        /** Required in DataModel - Optional in Banner **/

        /** Optional in DataModel - Optional in Banner **/

        if (content.containsKey("metadata") && content.get("metadata") instanceof Map) {
            def metadata = content.get("metadata")
            if (metadata.containsKey("dataOrigin") && metadata.get("dataOrigin") instanceof String) {
                String dataOrigin = content?.metadata?.dataOrigin?.trim()
                if (dataOrigin) {
                    requestData.put('dataOrigin', dataOrigin)
                }
            }
        }

        return requestData
    }


    protected void throwMaritalStatusAlreadyExistsException() {
        throw new ApplicationException(GeneralValidationCommonConstants.MARITAL_STATUS, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE, null))
    }


    private def getDataModelPropertyToMaritalStatusDomainFieldMap() {
        def map = [:]
        map.put(GeneralValidationCommonConstants.ABBREVIATION, 'code')
        map.put(GeneralValidationCommonConstants.TITLE, 'description')
        return map
    }

}
