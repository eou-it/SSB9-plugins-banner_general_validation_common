/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.LdmService
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * RESTful APIs for data model academic-discipline
 */
@Transactional
class AcademicDisciplineDataModelFacadeService {


    private static
    final List<String> VERSIONS = [GeneralValidationCommonConstants.VERSION_V4, GeneralValidationCommonConstants.VERSION_V7]

    AcademicDisciplineV7CompositeService academicDisciplineV7CompositeService
    AcademicDisciplineV4CompositeService academicDisciplineV4CompositeService

    /**
     * GET /api/academic-disciplines
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def list(Map params) {
        AbstractAcademicDisciplineCompositeService abstractAcademicDisciplineCompositeService = getServiceUsingAcceptHeader()
        return abstractAcademicDisciplineCompositeService.listApi(params)
    }

    /**
     * GET /api/academic-disciplines
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
        AbstractAcademicDisciplineCompositeService abstractAcademicDisciplineCompositeService = getServiceUsingAcceptHeader()
        return abstractAcademicDisciplineCompositeService.count(params)
    }

    /**
     * GET /api/academic-disciplines/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    def get(String guid) {
        AbstractAcademicDisciplineCompositeService abstractAcademicDisciplineCompositeService = getServiceUsingAcceptHeader()
        return abstractAcademicDisciplineCompositeService.get(guid)
    }

    /**
     * POST /api/academic-disciplines
     *
     * @param content Request body
     */
    def create(Map content) {
        AbstractAcademicDisciplineCompositeService requestProcessingService = getServiceUsingContentTypeHeader()
        def dataMap = requestProcessingService.create(content)
        AbstractAcademicDisciplineCompositeService responseRenderingService = getServiceUsingAcceptHeader()
        return responseRenderingService.createAcademicDisciplineDataModel(dataMap)
    }

    /**
     * PUT /api/academic-disciplines/<guid>
     *
     * @param content Request body
     */
    def update(Map content) {
        AbstractAcademicDisciplineCompositeService requestProcessingService = getServiceUsingContentTypeHeader()
        def dataMapForAcademicDiscipline = requestProcessingService.update(content)
        AbstractAcademicDisciplineCompositeService responseRenderingService = getServiceUsingAcceptHeader()
        return responseRenderingService.createAcademicDisciplineDataModel(dataMapForAcademicDiscipline)
    }

    private AbstractAcademicDisciplineCompositeService getServiceUsingAcceptHeader() {
        return getServiceByVersion(LdmService.getAcceptVersion(VERSIONS))
    }


    private AbstractAcademicDisciplineCompositeService getServiceUsingContentTypeHeader() {
        return getServiceByVersion(LdmService.getContentTypeVersion(VERSIONS))
    }


    private AbstractAcademicDisciplineCompositeService getServiceByVersion(String version) {
        AbstractAcademicDisciplineCompositeService abstractAcademicDisciplineCompositeService
        switch (version) {
            case 'v4':
                abstractAcademicDisciplineCompositeService = academicDisciplineV4CompositeService
                break
            case 'v7':
                abstractAcademicDisciplineCompositeService = academicDisciplineV7CompositeService
                break
        }
        return abstractAcademicDisciplineCompositeService
    }


}
