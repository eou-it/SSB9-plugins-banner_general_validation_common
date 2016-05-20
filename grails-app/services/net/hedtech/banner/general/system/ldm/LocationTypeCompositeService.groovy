/*********************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AddressType
import net.hedtech.banner.general.system.LocationTypeReadOnly
import net.hedtech.banner.general.system.ldm.v4.LocationType
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Location Type Service.</p>
 * <p> It will return location types of person and organization.</p>
 * <p> If location type code was configure for both person and organization, then priority is given to person.</p>
 */
@Transactional
class LocationTypeCompositeService extends LdmService{


    def addressTypeService
    def locationTypeReadOnlyService

    /**
     * GET /api/address-types
     * @param params
     * @return List
     */
    @Transactional(readOnly = true)
    def  list(Map params) {
        List<LocationType> locationTypeArrayList = []
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        locationTypeReadOnlyService.fetchAll(params).each {locationTypeView ->
            locationTypeArrayList <<  new LocationType(locationTypeView)
        }
      return  locationTypeArrayList
    }

    /**
     * @return Long value as total count
     */
    @Transactional(readOnly = true)
    Long count() {
      return  locationTypeReadOnlyService.fetchCountAll()
    }

    /**
     * GET /api/address-types/{guid}
     * @param guid
     * @return
     */
    @Transactional(readOnly = true)
    LocationType get(String guid) {
        LocationTypeReadOnly locationTypeViewRecord = locationTypeReadOnlyService.fetchByGuid(guid)
        if (!locationTypeViewRecord) {
            throw new ApplicationException(GeneralValidationCommonConstants.LOCATION_TYPE, new NotFoundException())
        }
        return new LocationType(locationTypeViewRecord)
    }

    /**
     * POST /api/address-types
     *
     * @param content Request body
     */
    def create(Map content) {
       if (!content?.code) {
            throw new ApplicationException(GeneralValidationCommonConstants.LOCATION_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        AddressType addressType = addressTypeService.fetchByCode(content?.code?.trim())
        if (addressType) {
            throw new ApplicationException(GeneralValidationCommonConstants.LOCATION_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_EXISTS_MESSAGE, null))
        }
        addressType = bindaddressType(new AddressType(), content)
        String addressTypeGuid = content?.id?.trim()?.toLowerCase()
        if (addressTypeGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(addressType.id, addressTypeGuid, GeneralValidationCommonConstants.ADDRESS_TYPE_LDM_NAME)
        } else {
            addressTypeGuid = (globalUniqueIdentifierService.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.ADDRESS_TYPE_LDM_NAME, addressType?.id)).guid
        }
        log.debug("GUID: ${addressTypeGuid}")
        return getDecorator(addressType,addressTypeGuid,content)
    }

    /**
     * PUT /api/address-types/<id>
     *
     * @param content Request body
     * @return
     */
    def update(Map content) {
        String addressTypeGuid = content?.id?.trim()?.toLowerCase()
        if (!addressTypeGuid) {
            throw new ApplicationException(GeneralValidationCommonConstants.LOCATION_TYPE, new NotFoundException())
        }
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(GeneralValidationCommonConstants.ADDRESS_TYPE_LDM_NAME, addressTypeGuid)
        if (!globalUniqueIdentifier) {
            //Per strategy when a ID was provided, the create should happen.
            return create(content)
        }
        if (!content?.code) {
            throw new ApplicationException(GeneralValidationCommonConstants.LOCATION_TYPE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }
        AddressType addressType = addressTypeService.get(globalUniqueIdentifier?.domainId)
        if (!addressType) {
            throw new ApplicationException(GeneralValidationCommonConstants.LOCATION_TYPE, new NotFoundException())
        }
        // Should not allow to update locationType code as it is read-only
        if (addressType.code != content?.code?.trim()) {
            content.put(GeneralValidationCommonConstants.CODE, addressType.code)
        }
        addressType = bindaddressType(addressType, content)
        return getDecorator(addressType,addressTypeGuid,content)
    }

    /**
     * Invoking the LDM service to bind map properties onto grails domains.
     * Invoking the ServiceBase to creates or updates a model instance provided within the supplied domainModelOrMap.
     */
    def bindaddressType(AddressType addressType, Map content) {
        bindData(addressType, content, [:])
        addressTypeService.createOrUpdate(addressType)
    }

    /**
     * Populating the decorator class with the response as per schema.
     */
    private def getDecorator(AddressType addressType, String addressTypeGuid = null,Map request) {
        LocationTypeReadOnly locationTypeRecord = setLocationTypesRecord(addressType,addressTypeGuid,request)
        return  new LocationType(locationTypeRecord)
    }


   private  LocationTypeReadOnly setLocationTypesRecord(AddressType addressType, String addressTypeGuid, Map request){
        LocationTypeReadOnly locationTypeView = new LocationTypeReadOnly()
        locationTypeView.setId(addressTypeGuid)
        locationTypeView.setCode(addressType?.code)
        locationTypeView.setDescription(addressType?.description)
        locationTypeView.setLocationType(request.addressType)
        return locationTypeView
    }
}
