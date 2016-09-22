package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

class RelationshipService extends ServiceBase {

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
            throw new ApplicationException(County, "@@r1:invalidRelationship@@")
        }

        relationship
    }
}
