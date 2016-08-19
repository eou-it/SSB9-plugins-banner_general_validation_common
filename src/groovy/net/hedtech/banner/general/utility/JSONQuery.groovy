/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.utility

import groovy.json.JsonSlurper
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.query.operators.Operators

/**
 * Wrapper for a JSON Query string like { "status": "A", "$or" : [{"qty" : {"$gt" : 10}}, {"qty" : {"$lt" : 20}}]}*
 */
class JSONQuery {

    public static final String LOGICAL_OR = "\$or"
    private static
    final COMPARISON_OPERATORS = ["\$ne": Operators.NOT_EQUALS, "\$gt": Operators.GREATER_THAN, "\$gte": Operators.GREATER_THAN_EQUALS, "\$lt": Operators.LESS_THAN, "\$lte": Operators.LESS_THAN_EQUALS]

    private String jsonQuery
    private def jsonQueryObjectModel


    JSONQuery(String jsonQuery) {
        this.jsonQuery = jsonQuery
        // Preserve parsed data structure
        JsonSlurper slurper = new JsonSlurper()
        try {
            this.jsonQueryObjectModel = slurper.parseText(jsonQuery)
        } catch (Exception ex) {
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("invalid", null))
        }
    }


    def getObjectModel() {
        return jsonQueryObjectModel
    }


    def getWhereClauseConditions() {
        def conditions = []
        for (def entry : this.jsonQueryObjectModel) {
            conditions << traverseForWhereClauseConditions(entry)
        }
        return conditions
    }


    def traverseForWhereClauseConditions(def entry) {
        if (entry instanceof Map) {
            for (def temp : entry) {
                return traverseForWhereClauseConditions(temp)
            }
        } else if (entry instanceof Map.Entry && entry.value instanceof List) {
            if (!LOGICAL_OR.equals(entry.key)) {
                throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("logical.operator.expected", [LOGICAL_OR]))
            }
            def conditions = []
            for (def temp : entry.value) {
                conditions << traverseForWhereClauseConditions(temp)
            }
            return [operator: LOGICAL_OR, value: conditions]
        } else if (entry instanceof Map.Entry) {
            String operator
            String value
            if (entry.value instanceof Map) {
                // Example - { "count" : { "$gt" : 10 } }
                operator = COMPARISON_OPERATORS.get(entry.value.find().key)
                if (!operator) {
                    throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("comparison.operator.invalid", null))
                }
                value = entry.value.find().value
            } else {
                // Example - { "count" : 10 }
                operator = Operators.EQUALS
                value = entry.value
            }
            return [property: entry.key, operator: operator, value: value]
        }
    }

}
