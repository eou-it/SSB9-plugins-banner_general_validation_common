/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.LdmService
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * RESTful APIs for data model marital-statuses
 */
@Transactional
class MaritalStatusDataModelFacadeService {

    private static
    final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V1, GeneralValidationCommonConstants.VERSION_V4]

    MaritalStatusV1CompositeService maritalStatusV1CompositeService
    MaritalStatusV4CompositeService maritalStatusV4CompositeService

    /**
     * GET /api/marital-statuses
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def list(Map params) {
        AbstractMaritalStatusCompositeService abstractMaritalStatusCompositeService = getServiceUsingAcceptHeader()
        return abstractMaritalStatusCompositeService.list(params)
    }

    /**
     * GET /api/marital-statuses
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def count(Map params) {
        AbstractMaritalStatusCompositeService abstractMaritalStatusCompositeService = getServiceUsingAcceptHeader()
        return abstractMaritalStatusCompositeService.count(params)
    }

    /**
     * GET /api/marital-statuses/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def get(String guid) {
        AbstractMaritalStatusCompositeService abstractMaritalStatusCompositeService = getServiceUsingAcceptHeader()
        return abstractMaritalStatusCompositeService.get(guid)
    }

    /**
     * POST /api/marital-statuses
     *
     * @param content Request body
     */
    def create(Map content) {
        AbstractMaritalStatusCompositeService requestProcessingService = getServiceUsingContentTypeHeader()
        def dataMap = requestProcessingService.create(content)
        AbstractMaritalStatusCompositeService responseRenderingService = getServiceUsingAcceptHeader()
        return responseRenderingService.createMaritalStatusDataModel(dataMap)
    }

    /**
     * PUT /api/marital-statuses/<guid>
     *
     * @param content Request body
     */
    def update(Map content) {
        AbstractMaritalStatusCompositeService requestProcessingService = getServiceUsingContentTypeHeader()
        def dataMapForCourse = requestProcessingService.update(content)
        AbstractMaritalStatusCompositeService responseRenderingService = getServiceUsingAcceptHeader()
        return responseRenderingService.createMaritalStatusDataModel(dataMapForCourse)
    }


    private AbstractMaritalStatusCompositeService getServiceUsingAcceptHeader() {
        return getServiceByVersion(LdmService.getAcceptVersion(VERSIONS))
    }


    private AbstractMaritalStatusCompositeService getServiceUsingContentTypeHeader() {
        return getServiceByVersion(LdmService.getContentTypeVersion(VERSIONS))
    }


    private AbstractMaritalStatusCompositeService getServiceByVersion(String version) {
        AbstractMaritalStatusCompositeService abstractMaritalStatusCompositeService
        switch (version) {
            case 'v1':
                abstractMaritalStatusCompositeService = maritalStatusV1CompositeService
                break
            case 'v4':
                abstractMaritalStatusCompositeService = maritalStatusV4CompositeService
                break
        }
        return abstractMaritalStatusCompositeService
    }

}
