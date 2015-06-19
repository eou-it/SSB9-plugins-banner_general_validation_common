/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.AcademicDisciplineView
import net.hedtech.banner.general.system.ldm.v4.AcademicDiscipline
import net.hedtech.banner.restfulapi.RestfulApiValidationException
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * <p> REST End point for Academic Discipline Service. If we'll pass type is major then , Academic Discipline major type of data will return.</p>
 * <p> If we'll pass type is minor then, Academic Discipline minor type of data will return else, It will return all  type of Academic Discipline data.</p>
 */
@Transactional
class AcademicDisciplineCompositeService {
    
    //This filed is used for only to create and update of Academic Discipline data
    def majorMinorConcentrationService
    
    private static final String ACADEMIC_DISCIPLINE_HEDM = 'academic-disciplines'
    private static final String DEFAULT_SORT_FIELD = 'title'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    private static final List allowedSortFields = [DEFAULT_SORT_FIELD]
    private final static HashMap ldmFieldToBannerDomainPropertyMap = [
            title: 'code'
    ]

    private String fetchBannerDomainPropertyForLdmField(String ldmField) {
        return ldmFieldToBannerDomainPropertyMap[ldmField]
    }


    /**
     * GET /api/academic-disciplines
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    List<AcademicDiscipline> list(Map params) {
        List<AcademicDiscipline> academicDisciplineDetailList = []
        List<AcademicDisciplineView> majorMinorConcentrationList
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ? params?.sort : DEFAULT_SORT_FIELD
        params?.order = params?.order ? params?.order : DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = fetchBannerDomainPropertyForLdmField(params.sort)
        majorMinorConcentrationList = getDataFromDB(false, params)
        getDisciplineFilterData(majorMinorConcentrationList, academicDisciplineDetailList)
        return academicDisciplineDetailList;
    }

/**
 *
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
    List<AcademicDiscipline> get(String guid) {
        List<AcademicDiscipline> academicDisciplineDetailList = []
        List<AcademicDisciplineView> majorMinorConcentrationList = AcademicDisciplineView.findAllByGuid(guid?.trim())
        if (majorMinorConcentrationList) {
            getDisciplineFilterData(majorMinorConcentrationList, academicDisciplineDetailList)
        } else {
            GlobalUniqueIdentifier globalUniqueIdentifier=GlobalUniqueIdentifier.findByGuid(guid?.trim())
            if(globalUniqueIdentifier && globalUniqueIdentifier?.ldmName!=ACADEMIC_DISCIPLINE_HEDM) {
                throw new RestfulApiValidationException("academicDiscipline.invalidGuid")
            }else {
                throw new ApplicationException("academicDiscipline", new NotFoundException())
            }
            }
        return academicDisciplineDetailList
    }


    private def getDataFromDB(Boolean count, Map params) {
        if (count) {
            params?.type ? AcademicDisciplineView.countByType(params.type) : AcademicDisciplineView.count()
        } else {
            params?.type ? AcademicDisciplineView.findAllByType(params.type, params) : AcademicDisciplineView.list(params)
        }
    }

    private void getDisciplineFilterData(List<AcademicDisciplineView> academicDisciplineList, List<AcademicDiscipline> academicDisciplineDetailList) {
        academicDisciplineList.each { academicDiscipline ->
            academicDisciplineDetailList <<  new AcademicDiscipline(academicDiscipline)
        }
    }
}
