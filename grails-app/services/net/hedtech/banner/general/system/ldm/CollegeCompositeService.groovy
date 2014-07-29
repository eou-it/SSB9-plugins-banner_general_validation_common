package net.hedtech.banner.general.system.ldm

/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */


import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.system.College
import net.hedtech.banner.general.system.ldm.v1.CollegeDetail
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility

/**
 * LDM service class for the College resource, which exposes the
 * methods get() and list() as Restfull services
 */
class CollegeCompositeService {

    private static final String LDM_NAME ="colleges"
    def collegeService
    def globalUniqueIdentifierService

    /**
     * Responsible for returning the College Resource for a
     * given GUID, which is exposed as GET Restfull Webservice having the
     * End Point  /api/colleges/<<GUID>>
     * @param guid - String
     * @return CollegeDetail
     */
    CollegeDetail get(String guid) {
        def map = getCollegeByGuid(guid)

        return new CollegeDetail(map.college, map.globalUniqueIdentifier.guid);
    }


    def getCollegeByGuid(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.fetchByLdmNameAndGuid(LDM_NAME, guid)

        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: College.class.simpleName))
        }

        College college = collegeService.get(globalUniqueIdentifier.domainId)

        if (!college) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: College.class.simpleName))
        }

        return [college: college, globalUniqueIdentifier: globalUniqueIdentifier]
    }

    /**
     * Responsible for returning the list of College Resources
     * which is exposed as POST Restfull Webservice having the
     * End Point  /api/colleges
     * @param map - Map
     * @return List - contains the list of Colleges
     */
    List<CollegeDetail> list(Map map) {
        List collegeList = []
        RestfulApiValidationUtility.correctMaxAndOffset(map, GlobalUniqueIdentifierService.MAX, GlobalUniqueIdentifierService.OFFSET)
        List<College> colleges = collegeService.list(map) as List
        colleges.each { college ->
            collegeList << new CollegeDetail(college, globalUniqueIdentifierService.fetchByLdmNameAndDomainId(LDM_NAME, college.id))
        }

        return collegeList
    }

    /**
     * Utility method which returns a CollegeDetail Decorator object for a
     * given domainId, this api is a Utility method which can be  used
     * by other (parent) service.
     * @param domainId
     * @return
     */
    CollegeDetail fetchByCollegeId(Long domainId) {
        if(null == domainId) {
            return null
        }
        return new CollegeDetail(collegeService.get(domainId) as College, globalUniqueIdentifierService.fetchByLdmNameAndDomainId(LDM_NAME, domainId))
    }

    /**
     * Utility method which returns a CollegeDetail Decorator object for a
     * given collegeCode, this api is a Utility method which can be used
     * by other (parent) service.
     * @param collegeCode
     * @return
     */
    CollegeDetail fetchByCollegeCode(String collegeCode) {
        if(!collegeCode) {
            return null
        }
        College college = collegeService.fetchByCode(collegeCode)
        if(!college){
            return null
        }
        return new CollegeDetail(college, globalUniqueIdentifierService.fetchByLdmNameAndDomainId(LDM_NAME, college.id))
    }

    /**
     * Pagination support api, which returns the count
     * of the College resources in the STVSUBJ table
     * @return Long - the record count
     */
    Long count() {
        return collegeService.count()
    }



}
