/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.query

class DynamicFinderV2 {
    //We would assign alias to the domains in the format t1,t2,t3,t4....tn as they appear in the list
    //please provide other information in same format
    //i.e. for example if you would like to use some field from field domain class in the list use t1.property
    //for third use t3.property and so on
    List<Class> domains;
    //in the joinConditions use the table identifier as mentioned above
    List<QueryCondition> joinConditions

    public static class QueryCondition {
        String field1;
        String field2;
        String operator;
        String andOrForNextConditoin;
    }

    private String buildQuery() {
        StringBuffer query = new StringBuffer()
        if (domains) {
            addFromClause(query)
            addWhereClause(query)
        }
        return query.toString()
    }

    private StringBuffer addFromClause(StringBuffer query) {
        query.append(" FROM ")
        for (int i = 0; i < domains.size(); ++i) {
            query.append(" ${domains.get(i).getSimpleName()} t${i + 1} ")
            if (i < domains.size() - 1) {
                query.append(",")
            }
        }
        return query
    }

    private StringBuffer addWhereClause(StringBuffer query) {
        if (joinConditions) {
            query.append(" WHERE ")
            addJoinConditions(query)
        }
    }

    private StringBuffer addJoinConditions(StringBuffer query) {
        for (int i = 0; i < joinConditions.size(); ++i) {
            query.append(" ${joinConditions.get(i).field1} ${joinConditions.get(i).operator} ${joinConditions.get(i).field2} ")
            if (i < joinConditions.size() - 1) {
                query.append(" ${joinConditions.get(i).andOrForNextConditoin} ")
            }
        }
    }
}
