/*******************************************************************************
 Copyright 2014-2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import groovy.transform.ToString
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.query.DynamicFinder
import net.hedtech.banner.query.QueryBuilder
import net.hedtech.banner.query.operators.Operators
import net.hedtech.banner.restfulapi.RestfulApiValidationUtility

import javax.annotation.PostConstruct

/**
 * Generic service method for basic validation tables used by HEDM
 */
@ToString(includeFields = true)
class GenericBasicValidationDomainService {
    Class baseDomain
    String baseDomainKeyField
    Class guidDomain
    String guidIdField
    String guidDomainKeyField
    String ldmNameField
    String ldmNameValue
    Class decorator
    List supportedSearchFields
    List supportedSortFields
    Map ethosToDomainFieldNameMap
    String defaultSortField
    Integer DEFAULT_PAGE_SIZE = 500
    Integer MAX_PAGE_SIZE = 1000
    private static
    final List<String> grailsDynamicPropeties = ['attached', 'domainClass', 'class', 'constraints', 'dirtyPropertyNames', 'errors', 'dirty', 'metadata']

    def get(guid) {
        log.info("Begin get for id:${guid} with setup:${this.toString()}")
        def decoratedResponse;
        validateSettings()
        if (baseDomain == guidDomain) {
            //base table has guid field in it we will not join
            String query = """FROM ${guidDomain.getName()}
                                WHERE ${ldmNameField} = '${ldmNameValue}'
                                AND ${guidIdField} = (:guid)"""
            def response = guidDomain.executeQuery(query, [guid: guid])[0]
            Map properties = response.properties
            removeGrailsProperties(properties)
            decoratedResponse = decorator.newInstance()
            properties.keySet().each {
                if (decoratedResponse.hasProperty(it)) {
                    decoratedResponse."${it}" = properties.get(it)
                }
            }
        } else {
            //we will have to perform join
            String query = """  FROM ${baseDomain.getName()} a, ${guidDomain.getName()} b
                                WHERE a.${baseDomainKeyField} = b.${guidDomainKeyField}
                                AND b.${ldmNameField} = '${ldmNameValue}'
                                AND b.${guidIdField} = (:guid)"""
            def response = guidDomain.executeQuery(query, [guid: guid])[0]
            Map properties = response[0].properties
            properties.putAll(response[1].properties)
            removeGrailsProperties(properties)
            decoratedResponse = decorator.newInstance()
            properties.keySet().each {
                if (decoratedResponse.hasProperty(it)) {
                    decoratedResponse."${it}" = properties.get(it)
                }
            }
        }
        log.info("End get for id:${guid}")
        return decoratedResponse
    }

    def list(Map params) {
        log.info("Begin list with params:${params} with setup:${this.toString()}")
        List decoratedListResponse = decorateListResponse(fetchForListOrCount(params))
        log.info("End list with params:${params}")
        return decoratedListResponse
    }

    private List decorateListResponse(def listResponse) {
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
        RestfulApiValidationUtility.correctMaxAndOffset(params, DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE)
        RestfulApiValidationUtility.validateSortField(params.sort, supportedSortFields)
        RestfulApiValidationUtility.validateSortOrder(params.order)
        Map pagingAndSortParams = params.clone()
        pagingAndSortParams.sort = ethosToDomainFieldNameMap && ethosToDomainFieldNameMap.get(pagingAndSortParams.sort) ?
                ethosToDomainFieldNameMap.get(pagingAndSortParams.sort) : pagingAndSortParams.sort
        pagingAndSortParams.sort = pagingAndSortParams.sort ?: defaultSortField
        pagingAndSortParams.order = pagingAndSortParams.order ?: 'ASC'
        String query
        DynamicFinder df
        if (baseDomain == guidDomain) {
            //base table has guid field in it we will not join
            query = """FROM ${guidDomain.getName()} a"""
            df = new DynamicFinder(guidDomain, query, "a")
        } else {
            //we will have to perform join
            query = """  FROM ${baseDomain.getName()} a, ${guidDomain.getName()} b
                                WHERE a.${baseDomainKeyField} = b.${guidDomainKeyField}
                                AND b.${ldmNameField} = '${ldmNameValue}'"""
            df = new DynamicFinder(baseDomain, query, "a")
        }
        Map queryParams = [:]
        List criteria = []
        supportedSearchFields.each { field ->
            if (params.containsKey(field)) {
                queryParams.put(field, params.get(field))
                criteria.add([key: field, binding: ethosToDomainFieldNameMap?.get(field) ?: field, operator: Operators.EQUALS])
            }
        }
        if (count) {
            return df.count([params: queryParams, criteria: criteria])
        } else {
            return df.find([params: queryParams, criteria: criteria], QueryBuilder.getFilterData(pagingAndSortParams).pagingAndSortParams)
        }
    }

    protected void validateSettings() {
        if (baseDomain == null) {
            throw new ApplicationException(baseDomain, "required.baseDomain")
        }

        if (guidDomain == null) {
            throw new ApplicationException(guidDomain, "required.guidDomain")
        }

        if (decorator == null) {
            throw new ApplicationException(decorator, "required.decorator")
        }

        if (!guidIdField) {
            throw new ApplicationException(guidDomain, "required.guidIdField")
        }
        if (!ldmNameField) {
            throw new ApplicationException(guidDomain, "required.ldmNameField")
        }
        if (!ldmNameValue) {
            throw new ApplicationException(guidDomain, "required.ldmNameValue")
        }
        if (baseDomain != guidDomain) {
            if (!baseDomainKeyField) {
                throw new ApplicationException(guidDomain, "required.baseDomainKeyField")
            }
            if (!guidDomainKeyField) {
                throw new ApplicationException(guidDomain, "required.guidDomainKeyField")
            }
        }
    }

    private void removeGrailsProperties(Map properties) {
        grailsDynamicPropeties.each {
            properties.remove(it)
        }
    }

    public def fetchByFieldValueInList(String fieldName, List fieldValueList) {
        List listResponse
        fieldValueList = fieldValueList.unique()
        List fieldParamObject = []
        fieldValueList.each {
            fieldParamObject << [data: it]
        }
        String query
        DynamicFinder df
        if (baseDomain == guidDomain) {
            //base table has guid field in it we will not join
            query = """FROM ${guidDomain.getName()} a"""
            df = new DynamicFinder(guidDomain, query, "a")
        } else {
            //we will have to perform join
            query = """  FROM ${baseDomain.getName()} a, ${guidDomain.getName()} b
                                WHERE a.${baseDomainKeyField} = b.${guidDomainKeyField}
                                AND b.${ldmNameField} = '${ldmNameValue}'"""
            df = new DynamicFinder(baseDomain, query, "a")
        }

        Map queryParams = [:]
        List criteria = []
        queryParams.put(fieldName, fieldParamObject)
        criteria.add([key: fieldName, binding: fieldName, operator: Operators.IN])
        listResponse = df.find([params: queryParams, criteria: criteria], [:])

        return decorateListResponse(listResponse)
    }

    @PostConstruct
    public void setDecorator(){
        if(!this.decorator){
            this.decorator = GenericBasicValidationDecorator.class
        }
    }
}
