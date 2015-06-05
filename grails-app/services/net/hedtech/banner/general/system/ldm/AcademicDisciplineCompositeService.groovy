package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.AcademicDiscipline
import net.hedtech.banner.general.system.ldm.v4.AcademicDisciplineDetailDecorator
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Transactional

/**
 * Created by invthannee on 6/2/2015.
 */
@Transactional
class AcademicDisciplineCompositeService {

    def majorMinorConcentrationService

    private static final String DEFAULT_SORT_FIELD = 'abbreviation'
    private static final String DEFAULT_ORDER_TYPE = 'ASC'
    static final List allowedSortFields = [DEFAULT_SORT_FIELD]

    /**
     * GET /api/academic-disciplines
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    List<AcademicDisciplineDetailDecorator> list(Map params) {
        List<AcademicDisciplineDetailDecorator> academicDisciplineDetailList = []
        List<AcademicDiscipline> majorMinorConcentrationList
        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        params?.sort = params?.sort ? params?.sort : DEFAULT_SORT_FIELD
        params?.order = params?.order ? params?.order : DEFAULT_ORDER_TYPE
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = LdmService.fetchBannerDomainPropertyForLdmField(params.sort)
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
     * GET /api/academic-disciplines/{guid}* @param guid
     * @return
     */
    @Transactional(readOnly = true)
    List<AcademicDisciplineDetailDecorator> get(String guid) {
        List<AcademicDisciplineDetailDecorator> academicDisciplineDetailList = []
        List<AcademicDiscipline> majorMinorConcentrationList = AcademicDiscipline.findAllByGuid(guid?.trim())
        if (majorMinorConcentrationList) {
            getDisciplineFilterData(majorMinorConcentrationList, academicDisciplineDetailList)
        } else {
            throw new ApplicationException("academicDiscipline", new NotFoundException())
        }
        return academicDisciplineDetailList
    }


    private def getDataFromDB(Boolean count, Map params) {
        if (count) {
            params?.type ? AcademicDiscipline.countByType(params.type) : AcademicDiscipline.count()
        } else {
            params?.type ? AcademicDiscipline.findAllByType(params.type, params) : AcademicDiscipline.list(params)
        }
    }

    private void getDisciplineFilterData(List<AcademicDiscipline> academicDisciplineList, List<AcademicDisciplineDetailDecorator> academicDisciplineDetailList) {
        academicDisciplineList.each { academicDiscipline ->
            academicDisciplineDetailList << popluateData(academicDiscipline)
        }
    }

    private AcademicDisciplineDetailDecorator popluateData(academicDiscipline) {
        new AcademicDisciplineDetailDecorator(academicDiscipline)
    }


}
