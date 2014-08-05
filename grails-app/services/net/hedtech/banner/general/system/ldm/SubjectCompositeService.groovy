package net.hedtech.banner.general.system.ldm
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.query.DynamicFinder

/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */


import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifierService
import net.hedtech.banner.general.system.Subject
import net.hedtech.banner.general.system.ldm.v1.SubjectDetail
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility

/**
 * LDM service class for the Subject resource, which exposes the
 * methods get() and list() as Restfull services
 */
class SubjectCompositeService {

    private static final String LDM_NAME = "subjects"
    def subjectService
    def globalUniqueIdentifierService

    /**
     * Responsible for returning the Subject Resource for a given
     * GUID, which is exposed as GET Restfull Webservice having the
     * End Point  /api/subjects/<<GUID>>
     * @param guid - String
     * @return SubjectDetail
     */
    SubjectDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Subject.class.simpleName))
        }

        Subject subject = Subject.get(globalUniqueIdentifier.domainId)
        if (!subject) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Subject.class.simpleName))
        }

        return new SubjectDetail(subject, globalUniqueIdentifier.guid);
    }

    /**
     * Responsible for returning the list of Subject Resources
     * which is exposed as POST Restfull Webservice having the
     * End Point  /api/subjects
     * @param map - Map
     * @return List - contains the list of Subjects
     */
    List<SubjectDetail> list(Map map) {
        List subjectList = []
        RestfulApiValidationUtility.correctMaxAndOffset(map, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)
        def filters = QueryBuilder.createFilters(map)
        def allowedSearchFields = ['code', 'description']
        def allowedOperators = [Operators.EQUALS, Operators.EQUALS_IGNORE_CASE, Operators.CONTAINS, Operators.STARTS_WITH]
        RestfulApiValidationUtility.validateCriteria(filters, allowedSearchFields, allowedOperators)
        RestfulApiValidationUtility.validateSortField(map.sort,allowedSearchFields)
        def filterMap = QueryBuilder.getFilterData(map)
        def query = """from Subject a"""
        DynamicFinder dynamicFinder = new DynamicFinder(Subject.class, query, "a")
        List<Subject> subjects = dynamicFinder.find([params: filterMap.params, criteria: filterMap.criteria], filterMap.pagingAndSortParams)
        subjects.each { subject ->
            subjectList << new SubjectDetail(subject, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, subject.id).guid)
        }

        return subjectList
    }

    /**
     * Utility method which returns a SubjectDetail Decorator object for a
     * given domainId, this api is a Utility method which can be  used
     * by other (parent) service.
     * @param domainId
     * @return
     */
    SubjectDetail fetchBySubjectId(Long domainId) {
        if (null == domainId) {
            return null
        }
        Subject subject = subjectService.get(domainId)
        if (!subject) {
            return null
        }
        return new SubjectDetail(subjectService.get(domainId) as Subject, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, domainId).guid)
    }

    /**
     * Utility method which returns a SubjectDetail Decorator object for a
     * given subjectCode, this api is a Utility method which can be used
     * by other (parent) service.
     * @param subjectCode
     * @return
     */
    SubjectDetail fetchBySubjectCode(String subjectCode) {
        if (!subjectCode) {
            return null
        }
        Subject subject = subjectService.fetchByCode(subjectCode)
        if (!subject) {
            return null
        }
        return new SubjectDetail(subject, GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, subject.id).guid)
    }

    /**
     * Pagination support api, which returns the count
     * of the Subject resources in the STVSUBJ table
     * @return Long - the record count
     */
    Long count() {
        return subjectService.count()
    }


}
