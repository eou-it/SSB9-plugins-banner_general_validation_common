/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system
/**
 * service layer for email types
 * */
class EmailTypeReadOnlyService {

    /**
     * fetching email type data based on guid
     * @param guid
     * @return
     */
    EmailTypeReadOnly fetchByGuid(String guid){
        return EmailTypeReadOnly.fetchByGuid(guid)
    }

    /**
     * fetching email type data
     * @param Map params
     * @return
     */
    List<EmailTypeReadOnly> fetchAll(Map params){
        return EmailTypeReadOnly.createCriteria().list(max: params.max, offset: params.offset){}
    }

    /**
     * fetching total count of email-types
     * @return
     */
    Long fetchCountAll() {
        return EmailTypeReadOnly.createCriteria().get {
            projections  {
                count()
            }
        }
    }
}
