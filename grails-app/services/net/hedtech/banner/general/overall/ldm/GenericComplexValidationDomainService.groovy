/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import groovy.transform.ToString
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException

/**
 * Generic service method for complex validation tables used by HEDM
 */
@ToString(includeFields = true)
class GenericComplexValidationDomainService extends GenericBasicValidationDomainService {
    Map<String, Object> defaultFields
    //the below map would store field names which needs to be mapped from additionDataDomain into the decorator
    //basically if field1 from additional data needs to be set in decorator field2 then the map must contain [field1: field2]
    Map<String, String> additionDataFieldMap
    Class additionDataDomain
    //the following map contains join field mapping between basedomain and additional data domain
    //for ex if we need that basedomain.field1=additionalDataDomain.field2 then in the map we must have [field1: field2]
    Map<String, String> additionalDataJoinFieldMap
    Map additionDataDomainFilter
    boolean skipRecordsWithNoAdditionalData = false

    @Override
    def get(Object guid) {
        log.debug("Begin get for id:${guid} with setup:${this.toString()}")
        validateSettings()
        def domainObject = super.getDomainObject(guid)
        def decoratedResponse = super.decorateObject(domainObject)
        if (additionDataDomain != null) {
            StringBuffer query = new StringBuffer();
            addAdditionalDataFromClause(query)
            addWhereClause(null, query)
            //TODO: we are using id here, its not mandatory to have a Id field named 'id'
            query.append(" AND a.id = :id ")
            addJoinFields(query, additionalDataJoinFieldMap)
            addFilter("b", query, additionDataDomainFilter)
            def response
            if (baseDomain == guidDomain) {
                response = baseDomain.executeQuery(query.toString(), [id: domainObject.id])[0]
            } else {
                response = baseDomain.executeQuery(query.toString(), [id: domainObject[0].id])[0]
            }
            if (response) {
                Map properties = response[1].properties
                removeGrailsProperties(properties)
                properties.keySet().each {
                    if (additionDataFieldMap.containsKey(it) && decoratedResponse.hasProperty(additionDataFieldMap.get(it))) {
                        decoratedResponse."${additionDataFieldMap.get(it)}" = properties.get(it)
                    }
                }
            } else if (skipRecordsWithNoAdditionalData) {
                throw new ApplicationException(baseDomain, new NotFoundException())
            }
        }
        log.debug("End get for id:${guid}")
        return decoratedResponse
    }

    private StringBuffer addAdditionalDataFromClause(StringBuffer query) {
        query.append(" FROM ${baseDomain.getName()} a, ${additionDataDomain.getName()} b ")
    }

    @Override
    def list(Map params) {
        def domainObjects = super.fetchForListOrCount(params)
        Map domainIdToAdditionDataMap = [:]
        if (additionDataDomain != null) {
            List listResponse = fetchAdditionalData(domainObjects)
            if (listResponse) {
                listResponse.each {
                    domainIdToAdditionDataMap.put(it[0].id, it[1])
                }
            }
        }
        return decorateListResponse(domainObjects, domainIdToAdditionDataMap)
    }

    private List fetchAdditionalData(def domainObjects){
        StringBuffer query = new StringBuffer();
        addAdditionalDataFromClause(query)
        addWhereClause(null, query)
        //TODO: we are using id here, its not mandatory to have a Id field named 'id'
        query.append(" AND a.id in :id ")
        addJoinFields(query, additionalDataJoinFieldMap)
        addFilter("b", query, additionDataDomainFilter)
        List listResponse
        if (baseDomain == guidDomain) {
            listResponse = baseDomain.executeQuery(query.toString(), [id: domainObjects.id])
        } else {
            List ids = []
            domainObjects.each {
                ids << it[0].id
            }
            listResponse = baseDomain.executeQuery(query.toString(), [id: ids])
        }
        return listResponse
    }

    @Override
    def count(Object params) {
        if(skipRecordsWithNoAdditionalData && additionDataDomain){
            def domainObjects = super.fetchForListOrCount(params)
            List listResponse = fetchAdditionalData(domainObjects)
            return listResponse.size()
        }else{
            return super.count(params)
        }
    }

    protected List decorateListResponse(Object listResponse, Map additionalDataMap) {
        def decoratedResponse;
        if (baseDomain == guidDomain) {
            List decoratedListResponse = []
            listResponse.each { response ->
                Map properties = response.properties
                removeGrailsProperties(properties)
                decoratedResponse = decorator.newInstance()
                properties.keySet().each {
                    if (decoratedResponse.hasProperty(it)) {
                        decoratedResponse."${it}" = properties.get(it)
                    }
                }
                if (additionalDataMap.get(response.id)) {
                    Map properties1 = additionalDataMap.get(response.id).properties
                    removeGrailsProperties(properties1)
                    properties1.keySet().each {
                        if (additionDataFieldMap.containsKey(it) && decoratedResponse.hasProperty(additionDataFieldMap.get(it))) {
                            decoratedResponse."${additionDataFieldMap.get(it)}" = properties1.get(it)
                        }
                    }
                    decoratedListResponse << decoratedResponse
                } else if (!skipRecordsWithNoAdditionalData) {
                    decoratedListResponse << decoratedResponse
                }
                decoratedListResponse << decoratedResponse
            }
            return decoratedListResponse
        } else {
            List decoratedListResponse = []
            listResponse.each { response ->
                Map properties = response[0].properties
                properties.putAll(response[1].properties)
                removeGrailsProperties(properties)
                decoratedResponse = decorator.newInstance()
                properties.keySet().each {
                    if (decoratedResponse.hasProperty(it)) {
                        decoratedResponse."${it}" = properties.get(it)
                    }
                }
                if (additionalDataMap.get(response[0].id)) {
                    Map properties1 = additionalDataMap.get(response[0].id).properties
                    removeGrailsProperties(properties1)
                    properties1.keySet().each {
                        if (additionDataFieldMap.containsKey(it) && decoratedResponse.hasProperty(additionDataFieldMap.get(it))) {
                            decoratedResponse."${additionDataFieldMap.get(it)}" = properties1.get(it)
                        }
                    }
                    decoratedListResponse << decoratedResponse
                } else if (!skipRecordsWithNoAdditionalData) {
                    decoratedListResponse << decoratedResponse
                }
            }
            return decoratedListResponse
        }
    }

    @Override
    protected void validateSettings() {
        super.validateSettings()
        if (!additionalDataJoinFieldMap) {
            throw new ApplicationException(additionDataDomain, new BusinessLogicValidationException("addtional.data.join.fields.missing", null))
        }
        if (additionDataDomain && !additionDataFieldMap) {
            throw new ApplicationException(additionDataDomain, new BusinessLogicValidationException("addtional.data.fields.missing", null))
        }
    }
}
