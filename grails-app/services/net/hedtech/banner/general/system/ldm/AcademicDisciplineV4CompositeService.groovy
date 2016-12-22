/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.AcademicDisciplineView
import net.hedtech.banner.general.system.ldm.v4.AcademicDiscipline


class AcademicDisciplineV4CompositeService extends AbstractAcademicDisciplineCompositeService {

    protected def getAllowedSortFields() {
        return ["type", "abbreviation"]
    }


    protected Map processListApiRequest(final Map requestParams) {
        String sortField = requestParams.sort?.trim()
        String sortOrder = requestParams.order?.trim()
        int max = requestParams.max?.trim()?.toInteger() ?: 0
        int offset = requestParams.offset?.trim()?.toInteger() ?: 0

        Map mapForSearch = [:]
        if (requestParams.containsKey("type")) {
            mapForSearch << [type: requestParams.get("type")]
        }
        List<AcademicDisciplineView> academicDisciplines = academicDisciplineSearchService.fetchAllByCriteria(mapForSearch, sortField, sortOrder, max, offset)
        int totalCount = academicDisciplineSearchService.countByCriteria(mapForSearch)
        return [academicDisciplines: academicDisciplines, totalCount: totalCount]
    }



    protected def createAcademicDisciplineDataModel(def dataMapForAcademicDiscipline) {
        AcademicDiscipline decorator = new AcademicDiscipline()

        decorator.code = dataMapForAcademicDiscipline["code"]
        decorator.description = dataMapForAcademicDiscipline["description"]
        decorator.guid = dataMapForAcademicDiscipline["guid"]
        decorator.type = dataMapForAcademicDiscipline["type"]

        return decorator
    }


    protected Map extractDataFromRequestBody(Map content) {
        def requestData = [:]

        /* Required in DataModel - Required in Banner */

        String academicDisciplineInPayload
        if (content.containsKey("guid") && content.get("guid") instanceof String) {
            academicDisciplineInPayload = content?.guid?.trim()?.toLowerCase()
            requestData.put('academicDisciplineGuid', academicDisciplineInPayload)
        }

        // UPDATE operation - API SHOULD prefer the resource identifier on the URI, over the payload.
        String academicDisciplineInURI = content?.id?.trim()?.toLowerCase()
        if (academicDisciplineInURI && !academicDisciplineInURI.equals(academicDisciplineInPayload)) {
            content.put('guid', academicDisciplineInURI)
            requestData.put('academicDisciplineGuid', academicDisciplineInURI)
        }

        if (content.containsKey("code") && content.get("code") instanceof String) {
            requestData.put('academicDisciplineCode', content.get("code")?.trim())
        }else{
            throw new ApplicationException(GeneralValidationCommonConstants.ACADEMIC_DISCIPLINE, new BusinessLogicValidationException(GeneralValidationCommonConstants.ERROR_MSG_CODE_REQUIRED, null))
        }

        if (content.containsKey("type") && content.get("type") instanceof String) {
            requestData.put('type', content.get("type"))
        }

        if (content.containsKey("description") && content.get("description") instanceof String) {
            requestData.put('description', content.get("description")?.trim())
        }

        return requestData
    }



}
