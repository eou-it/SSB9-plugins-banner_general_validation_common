/** *******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.overall.ldm.LdmService
import net.hedtech.banner.general.system.Subject
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v1.SubjectDetail
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * RESTful APIs for HeDM subjects
 */
@Transactional
class SubjectCompositeService extends LdmService {

    private static final String LDM_NAME = "subjects"

    def subjectService

    /**
     * GET /api/subjects
     *
     * @param params Request parameters
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<SubjectDetail> list(Map params) {
        List subjectList = []

        RestfulApiValidationUtility.correctMaxAndOffset(params, RestfulApiValidationUtility.MAX_DEFAULT, RestfulApiValidationUtility.MAX_UPPER_LIMIT)

        List allowedSortFields = ['abbreviation', 'title']
        RestfulApiValidationUtility.validateSortField(params.sort, allowedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        params.sort = fetchBannerDomainPropertyForLdmField(params.sort)

        List<Subject> subjects = subjectService.list(params) as List
        subjects.each { subject ->
            subjectList << getDecorator(subject)
        }
        return subjectList
    }

    /**
     * GET /api/subjects
     *
     * The count method must return the total number of instances of the resource.
     * It is used in conjunction with the list method when returning a list of resources.
     * RestfulApiController will make call to "count" only if the "list" execution happens without any exception.
     *
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Long count() {
        return subjectService.count()
    }

    /**
     * GET /api/subjects/<guid>
     *
     * @param guid
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    SubjectDetail get(String guid) {
        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, guid)
        if (!globalUniqueIdentifier) {
            throw new ApplicationException("subject", new NotFoundException())
        }

        Subject subject = Subject.get(globalUniqueIdentifier.domainId)
        if (!subject) {
            throw new ApplicationException("subject", new NotFoundException())
        }

        return getDecorator(subject, globalUniqueIdentifier.guid)
    }

    /**
     * POST /api/subjects
     *
     * @param content Request body
     */
    def create(content) {
        validateRequest(content)

        Subject subject = Subject.findByCode(content?.code?.trim())
        if (subject) {
            throw new ApplicationException("subject", new BusinessLogicValidationException("exists.message", null))
        }

        subject = bindSubject(new Subject(), content)

        String subjectGuid = content?.guid?.trim()?.toLowerCase()
        if (subjectGuid) {
            // Overwrite the GUID created by DB insert trigger, with the one provided in the request body
            updateGuidValue(subject.id, subjectGuid, LDM_NAME)
        } else {
            GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, subject?.id)
            subjectGuid = globalUniqueIdentifier.guid
        }
        log.debug("GUID: ${subjectGuid}")

        return getDecorator(subject, subjectGuid)
    }

    /**
     * PUT /api/marital-statuses/<guid>
     *
     * @param content Request body
     */
    def update(content) {
        String subjectGuid = content?.id?.trim()?.toLowerCase()

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndGuid(LDM_NAME, subjectGuid)
        if (subjectGuid) {
            if (!globalUniqueIdentifier) {
                if (!content.get('guid')) {
                    content.put('guid', subjectGuid)
                }
                //Per strategy when a GUID was provided, the create should happen.
                return create(content)
            }
        } else {
            throw new ApplicationException("subject", new NotFoundException())
        }

        Subject subject = Subject.findById(globalUniqueIdentifier?.domainId)
        if (!subject) {
            throw new ApplicationException("subject", new NotFoundException())
        }

        // Should not allow to update subject.code as it is read-only
        if (subject.code != content?.code?.trim()) {
            content.put("code", subject.code)
        }
        subject = bindSubject(subject, content)

        return getDecorator(subject, subjectGuid)
    }


    SubjectDetail fetchBySubjectId(Long domainId) {
        SubjectDetail subjectDetail
        if (domainId) {
            Subject subject = subjectService.get(domainId)
            if (subject) {
                subjectDetail = getDecorator(subject)
            }
        }
        return subjectDetail
    }


    SubjectDetail fetchBySubjectCode(String subjectCode) {
        SubjectDetail subjectDetail
        if (subjectCode) {
            Subject subject = subjectService.fetchByCode(subjectCode)
            if (subject) {
                subjectDetail = getDecorator(subject)
            }
        }
        return subjectDetail
    }


    private void validateRequest(content) {
        if (!content?.code) {
            throw new ApplicationException('subject', new BusinessLogicValidationException('code.required.message', null))
        }
        if (!content?.description) {
            throw new ApplicationException('subject', new BusinessLogicValidationException('description.required.message', null))
        }
    }


    def bindSubject(Subject subject, Map content) {
        setDataOrigin(subject, content)
        bindData(subject, content, [:])
        subject.dispWebInd = false
        subjectService.createOrUpdate(subject)
    }


    private SubjectDetail getDecorator(Subject subject, String subjectGuid = null) {
        SubjectDetail decorator
        if (subject) {
            if (!subjectGuid) {
                subjectGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainId(LDM_NAME, subject.id)?.guid
            }
            decorator = new SubjectDetail(subject, subjectGuid, new Metadata(subject.dataOrigin))
        }
        return decorator
    }

}
