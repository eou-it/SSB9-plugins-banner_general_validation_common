/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.query

class DynamicFinderV2 {
    List<QueryMetadata> queryMetadataList

    DynamicFinderV2() {
        queryMetadataList = []
    }

    public void addQueryMetaData(QueryMetadata queryMetadata) {
        queryMetadataList.add(queryMetadata)
    }

    public static class QueryMetadata {
        //We would assign alias to the domains in the format t1,t2,t3,t4....tn as they appear in the list
        //please provide other information in same format
        //i.e. for example if you would like to use some field from field domain class in the list use t1.property
        //for third use t3.property and so on
        List<Class> domains;
        //in the joinConditions use the table identifier as mentioned above
        List<QueryCondition> joinConditions

        List<QueryCondition> filterConditions
        String extraWhereClause

        String selectQuery

        QueryMetadata() {
            domains = []
            joinConditions = []
            filterConditions = []
            extraWhereClause = null
        }

        QueryMetadata(List<Class> domains, List<QueryCondition> joinConditions, List<QueryCondition> filterConditions, String extraWhereClause) {
            DynamicFinderV2()
            if (domains) {
                this.domains = domains
            }
            if (joinConditions) {
                this.joinConditions = joinConditions
            }
            if (filterConditions) {
                this.filterConditions
            }
            this.extraWhereClause = extraWhereClause
        }

        public void addDomain(Class domain) {
            domains.add(domain)
        }

        public void addJoinCondition(QueryCondition condition) {
            joinConditions.add(condition)
        }

        public void addFilterCondition(QueryCondition condition) {
            filterConditions.add(condition)
        }

        //this is a convenience method provided to convert the
        //request param object into filter conditions
        //it will always try to look for the field in the listed domains
        //i.e for example
        //we have a param "field1"="valueOfField1"
        //in our domains list there are two classes class1 and class2
        //both have property named field1 then the filter will be applied to class1.field1
        //if only class1 has property field1 then the filter will be applied to class1.field1
        //if only class2 has property field1 then the filter will be applied to class2.field1
        //basically the first class with the property name will apply the filter
        //if none of the class have the property then we will simply apply field1=valueOfField1
        public void addToFilterConditions(Map params) {
            params.each {
                boolean condtionSet = false
                for (int i = 0; i < domains.size(); ++i) {
                    Class domain = domains.get(i)
                    if (domain.hasProperty(it.key)) {
                        QueryCondition condition = new QueryCondition()
                        condition.field1 = "t${i + 1}.${it.key}"
                        condition.field2 = it.value
                        condition.operator = '='
                        condition.andOr = 'AND'
                        filterConditions.add(condition)
                        condtionSet = true
                        break;
                    }
                }
                if (!condtionSet) {
                    QueryCondition condition = new QueryCondition()
                    condition.field1 = it.key
                    condition.field2 = it.value
                    condition.operator = '='
                    condition.andOr = 'AND'
                    filterConditions.add(condition)
                }
            }
        }

        public static class QueryCondition {
            String field1;
            String field2;
            String operator = "=";
            String andOr = "AND";
        }

        private String buildQuery() {
            StringBuffer query = new StringBuffer(selectQuery ?: " SELECT *  ")
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
            query.append(" WHERE 1=1 ")
            if (joinConditions) {
                addJoinConditions(query)
            }
            if (filterConditions) {
                addFilterConditions(query)
            }
            if (extraWhereClause) {
                query.append(" AND ")
                query.append(extraWhereClause)
            }
        }

        private StringBuffer addJoinConditions(StringBuffer query) {
            for (int i = 0; i < joinConditions.size(); ++i) {
                query.append(" ${joinConditions.get(i).andOr} ")
                query.append(" ${joinConditions.get(i).field1} ${joinConditions.get(i).operator} ${joinConditions.get(i).field2} ")
            }
        }

        private StringBuffer addFilterConditions(StringBuffer query) {
            for (int i = 0; i < filterConditions.size(); ++i) {
                query.append(" ${filterConditions.get(i).andOr} ")
                query.append(" ${filterConditions.get(i).field1} ${filterConditions.get(i).operator} '${filterConditions.get(i).field2}' ")
            }
        }
    }

    public String buildQuery() {
        StringBuffer query = new StringBuffer();
        for (int i = 0; i < queryMetadataList.size(); ++i) {
            query.append(queryMetadataList.get(i).buildQuery())
            if (i < queryMetadataList.size() - 1) {
                query.append(" UNION ")
            }
        }
        return query.toString()
    }

    public List find(String max, String offset) {
        return queryMetadataList[0].domains[0].executeQuery(buildQuery(), [max: max, offset: offset])
    }

    public int count(String max, String offset) {
        return queryMetadataList[0].domains[0].executeQuery(buildQuery(), [max: max, offset: offset]).size()
    }
}
