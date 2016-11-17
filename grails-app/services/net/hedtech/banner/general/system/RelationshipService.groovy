/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting the Relationship model.
 * */
class RelationshipService extends ServiceBase {

    List<Relationship> fetchAllByCodeInList(Collection<String> relationshipCodes) {
        List entities = []
        if (relationshipCodes) {
            entities = Relationship.withSession { session ->
                session.getNamedQuery('Relationship.fetchAllByCodeInList')
                        .setParameterList('codes', relationshipCodes)
                        .list()
            }
        }
        return entities
    }

    def fetchRelationshipList(int max = 10, int offset = 0, String searchString = '') {
        def criteria = Relationship.createCriteria()
        def relationshipList = criteria.list(max: max, offset: offset, sort: 'description', order: 'asc') {
            and {
                ilike("description", "%${searchString}%")
            }
        }

        relationshipList
    }

    def fetchRelationship(def code) {
        def relationship = Relationship.fetchByCode(code)

        if(!relationship){
            throw new ApplicationException(Relationship, "@@r1:invalidRelationship@@")
        }

        relationship
    }
}
