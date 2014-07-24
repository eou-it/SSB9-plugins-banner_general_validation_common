package net.hedtech.banner.general.system.ldm

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


class SubjectCompositeService {

    private static final String LDM_NAME ="subjects"
    def subjectService
    def globalUniqueIdentifierService

    /**
     * Responsible for returning the Subject Resource for a
     * given GUID, which is exposed as GET Restfull Webservice having the
     * End Point  /api/subjects/<<GUID>>
     * @param guid - String
     * @return SubjectDetail
     */
    SubjectDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = globalUniqueIdentifierService.findByGuid(guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Subject.class.simpleName))
        }
        Subject subject = subjectService.get(globalUniqueIdentifier.domainId)
        if (!subject) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new NotFoundException(id: Subject.class.simpleName))
        }
        return new SubjectDetail(subject, globalUniqueIdentifier.guid );
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
        RestfulApiValidationUtility.correctMaxAndOffset(map, GlobalUniqueIdentifierService.MAX, GlobalUniqueIdentifierService.OFFSET)
        List<Subject> subjects = subjectService.list(map) as List
        subjects.each { subject ->
            subjectList << new SubjectDetail(subject, globalUniqueIdentifierService.fetchByLdmNameAndDomainId(LDM_NAME, subject.id))
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
        if(null == domainId) {
            return null
        }
        return new SubjectDetail(subjectService.get(domainId) as Subject, globalUniqueIdentifierService.fetchByLdmNameAndDomainId('subjects', domainId)?.guid)
    }

    /**
     * Utility method which returns a SubjectDetail Decorator object for a
     * given subjectCode, this api is a Utility method which can be used
     * by other (parent) service.
     * @param subjectCode
     * @return
     */
    SubjectDetail fetchBySubjectCode(String subjectCode) {
        if(!subjectCode) {
            return null
        }
        Subject subject = subjectService.findByCode(subjectCode)
        if(!gradingMode1){
            return null
        }
        return new SubjectDetail(subject, globalUniqueIdentifierService.fetchByLdmNameAndDomainId('gradeSchemes', subject.id)?.guid)
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
