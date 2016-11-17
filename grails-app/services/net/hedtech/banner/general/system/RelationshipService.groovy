/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting the Relationship model.
 * */
class RelationshipService extends ServiceBase {
    boolean transactional = true

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

}
