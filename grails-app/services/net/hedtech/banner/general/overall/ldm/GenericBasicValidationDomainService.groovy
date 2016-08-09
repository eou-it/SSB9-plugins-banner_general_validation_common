/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import groovy.transform.ToString
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility

import javax.annotation.PostConstruct

/**
 * Generic service method for basic validation tables used by HEDM
 */
@ToString(includeFields = true)
class GenericBasicValidationDomainService {
    protected Class baseDomain
    protected Class guidDomain
    protected String guidIdField
    protected Class decorator
    protected List supportedSearchFields
    protected List supportedSortFields
    protected Map ethosToDomainFieldNameMap
    protected String defaultSortField
    protected Map joinFieldMap
    protected String joinFieldSeperator
    protected Map baseDomainFilter
    protected Map guidDomainFilter
    Integer DEFAULT_PAGE_SIZE = 500
    Integer MAX_PAGE_SIZE = 1000
    protected static
    final List<String> grailsDynamicPropeties = ['attached', 'domainClass', 'class', 'constraints', 'dirtyPropertyNames', 'errors', 'dirty', 'metadata']

    def get(guid) {
        log.debug("Begin get for id:${guid} with setup:${this.toString()}")
        validateSettings()
        def decoratedResponse = decorateObject(getDomainObject(guid))
        log.debug("End get for id:${guid}")
        return decoratedResponse
    }

    def decorateObject(def object) {
        if (object) {
            def decoratedResponse = decorator.newInstance();
            if (baseDomain == guidDomain) {
                Map properties = object.properties
                assignPropertyToObject(decoratedResponse, properties)
            } else {
                Map properties = object[0].properties
                properties.putAll(object[1].properties)
                assignPropertyToObject(decoratedResponse, properties)
            }
            return decoratedResponse;
        } else {
            throw new ApplicationException(baseDomain, new NotFoundException())
        }
    }

    void assignPropertyToObject(def object, def properties) {
        removeGrailsProperties(properties)
        properties.keySet().each {
            if (object.hasProperty(it)) {
                object."${it}" = properties.get(it)
            }
        }
    }

    def getDomainObject(String guid) {
        StringBuffer query = new StringBuffer()
        addFromClause(query)
        addWhereClause(guid, query)
        addBasicDomainFilter(query)
        addGuidDomainFilter(query)
        if (baseDomain == guidDomain) {
            return guidDomain.executeQuery(query.toString(), [guid: guid])[0]
        } else {
            addJoinFields(query)
            return guidDomain.executeQuery(query.toString(), [guid: guid])[0]
        }
    }

    protected StringBuffer addFromClause(StringBuffer query) {
        if (baseDomain == guidDomain) {
            query.append(" FROM ${guidDomain.getName()} ")
        } else {
            query.append(" FROM ${baseDomain.getName()} a, ${guidDomain.getName()} b ")
        }
        return query
    }

    protected StringBuffer addWhereClause(String guid, StringBuffer query) {
        if (baseDomain == guidDomain) {
            if (guid) {
                query.append(" WHERE ${guidIdField} = :guid ")
            } else {
                query.append(" WHERE 1 = 1 ")
            }
        } else {
            if (guid) {
                query.append(" WHERE b.${guidIdField} = :guid ")
            } else {
                query.append(" WHERE 1 = 1 ")
            }
        }
        return query
    }

    protected StringBuffer addBasicDomainFilter(StringBuffer query) {
        return addBasicDomainFilter(query, baseDomainFilter)
    }

    protected StringBuffer addBasicDomainFilter(StringBuffer query, Map baseDomainFilter) {
        return addFilter("a", query, baseDomainFilter)
    }

    protected StringBuffer addFilter(String tableIdentifier, StringBuffer query, Map domainFilter) {
        if (domainFilter) {
            if (tableIdentifier) {
                domainFilter.each {
                    query.append(" AND ${tableIdentifier}.${it.key} = '${it.value}' ")
                }
            } else {
                domainFilter.each {
                    query.append(" AND ${it.key} = '${it.value}' ")
                }
            }

        }
        return query
    }

    protected StringBuffer addGuidDomainFilter(StringBuffer query) {
        return addGuidDomainFilter(query, guidDomainFilter)
    }

    protected StringBuffer addGuidDomainFilter(StringBuffer query, Map guidDomainFilter) {
        String tableIdentifier
        if (baseDomain != guidDomain) {
            tableIdentifier = "b"
        }
        return addFilter(tableIdentifier, query, guidDomainFilter)
    }

    protected StringBuffer addJoinFields(StringBuffer query) {
        return addJoinFields(query, joinFieldMap)
    }

    protected StringBuffer addJoinFields(StringBuffer query, Map joinFieldMap) {
        if (joinFieldMap) {
            joinFieldMap.each {
                String baseJoinField
                String guidJoinField
                if (it.key instanceof List) {
                    baseJoinField = parseFileName(it.key, 'a')
                } else if (it.key instanceof String) {
                    baseJoinField = "a.${it.key}"
                } else {
                    throw new ApplicationException(baseDomain, new BusinessLogicValidationException("invalid.join.field.type", null))
                }
                if (it.value instanceof List) {
                    guidJoinField = parseFileName(it.value, 'b')
                } else if (it.value instanceof String) {
                    guidJoinField = "b.${it.value}"
                } else {
                    throw new ApplicationException(baseDomain, new BusinessLogicValidationException("invalid.join.field.type", null))
                }
                query.append(" AND ${baseJoinField} = ${guidJoinField} ")
            }
        }
        return query
    }

    String parseFileName(List fields, String tableIdentifier) {
        StringBuffer fieldName = new StringBuffer()
        fieldName.append('concat(')
        fields.each {
            fieldName.append(tableIdentifier)
            fieldName.append('.')
            fieldName.append(it)
            fieldName.append(',')
            fieldName.append("'${joinFieldSeperator}'")
            fieldName.append(',')
        }

        return fieldName.toString().substring(0, fieldName.toString().lastIndexOf(",'${joinFieldSeperator}',")) + ")"
    }

    def list(Map params) {
        log.info("Begin list with params:${params} with setup:${this.toString()}")
        List decoratedListResponse = decorateListResponse(fetchForListOrCount(params))
        log.info("End list with params:${params}")
        return decoratedListResponse
    }

    protected List decorateListResponse(def listResponse) {
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
                decoratedListResponse << decoratedResponse
            }
            return decoratedListResponse
        }
    }

    def count(params) {
        return fetchForListOrCount(params, true)
    }

    private def fetchForListOrCount(Map params, boolean count = false) {
        if (!params.offset) {
            params.offset = "0"
        }
        RestfulApiValidationUtility.correctMaxAndOffset(params, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE)
        RestfulApiValidationUtility.validateSortField(params.sort, supportedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        StringBuffer query = new StringBuffer()
        if (count) {
            query.append(" SELECT COUNT(*) ")
        }
        addFromClause(query)
        addWhereClause(null, query)
        addBasicDomainFilter(query)
        addGuidDomainFilter(query)
        addSearchFilter(query, params)
        if (baseDomain == guidDomain) {
            return guidDomain.executeQuery(query.toString(), [max: params.max, offset: params.offset])
        } else {
            addJoinFields(query)
            return baseDomain.executeQuery(query.toString(), [max: params.max, offset: params.offset])
        }
    }

    //TODO: currently search filter only works on baseDomain when baseDomain!=guidDomain and guidDomain when baseDomain==guidDomain
    //TODO: we need to enhance it
    private StringBuffer addSearchFilter(StringBuffer query, Map params) {
        if (baseDomain == guidDomain) {
            supportedSearchFields.each { field ->
                if (params.containsKey(field)) {
                    String fieldName = field;
                    if (ethosToDomainFieldNameMap && ethosToDomainFieldNameMap.containsKey(fieldName)) {
                        fieldName = ethosToDomainFieldNameMap.get(fieldName)
                    }
                    query.append(" AND ${fieldName} = '${params.get(fieldName)}' ")
                }
            }
        } else {
            supportedSearchFields.each { field ->
                if (params.containsKey(field)) {
                    String fieldName = field;
                    if (ethosToDomainFieldNameMap && ethosToDomainFieldNameMap.containsKey(fieldName)) {
                        fieldName = ethosToDomainFieldNameMap.get(fieldName)
                    }
                    query.append(" AND a.${fieldName} = '${params.get(fieldName)}' ")
                }
            }
        }
        return query
    }

    protected void validateSettings() {
        if (baseDomain == null) {
            throw new ApplicationException(baseDomain, new BusinessLogicValidationException("required.baseDomain", null))
        }

        if (guidDomain == null) {
            throw new ApplicationException(guidDomain, new BusinessLogicValidationException("required.guidDomain", null))
        }

        if (decorator == null) {
            throw new ApplicationException(decorator, new BusinessLogicValidationException("required.decorator", null))
        }

        if (!guidIdField) {
            throw new ApplicationException(guidDomain, new BusinessLogicValidationException("required.guidIdField", null))
        }
    }

    protected void removeGrailsProperties(Map properties) {
        grailsDynamicPropeties.each {
            properties.remove(it)
        }
    }

    public def fetchByFieldValueInList(String fieldName, List fieldValueList) {
        StringBuffer query = new StringBuffer()
        addFromClause(query)
        addWhereClause(null, query)
        addBasicDomainFilter(query)
        addGuidDomainFilter(query)
        if (baseDomain == guidDomain) {
            query.append(" AND ${fieldName} in (:fieldValueList) ")
        } else {
            query.append(" AND a.${fieldName} in (:fieldValueList) ")
        }
        return decorateListResponse(baseDomain.executeQuery(query.toString(), [fieldValueList: fieldValueList]))
    }

    @PostConstruct
    public void setDecorator() {
        if (!this.decorator) {
            this.decorator = GenericBasicValidationDecorator.class
        }
    }
}
