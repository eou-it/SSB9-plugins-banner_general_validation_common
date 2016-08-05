/*********************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.utility

import grails.test.GrailsUnitTestCase
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.query.operators.Operators
import org.junit.Test

class JSONQueryTests extends GrailsUnitTestCase {

    @Test
    void testObjectCreation() {
        String temp = """ { "status": "A", "\$or" : [{"qty" : {"\$gt" : 10}}, {"qty" : {"\$lt" : 20}}]} """
        JSONQuery jsonQuery = new JSONQuery(temp)
        def jsonQueryOM = jsonQuery.getObjectModel()
        assertNotNull jsonQueryOM
        assertTrue jsonQueryOM instanceof Map
        assertEquals 2, jsonQueryOM.size()
    }


    @Test
    void testObjectCreationWithInvalidJSONQuery() {
        // closing double quote is missing for description
        String temp = """ {"description : "Beginning mathematics", "number" : "101"} """
        shouldFail(groovy.json.JsonException) {
            JSONQuery jsonQuery = new JSONQuery(temp)
        }
    }


    @Test
    void testSimpleJSONQueryWithoutOperator() {
        String temp = """ {"title" : "MATH101"} """
        JSONQuery jsonQuery = new JSONQuery(temp)
        def predicateList = jsonQuery.getWhereClauseConditions()
        assertNotNull predicateList
        assertEquals 1, predicateList.size()
        assertEquals "title", predicateList[0].property
        assertEquals Operators.EQUALS, predicateList[0].operator
        assertEquals "MATH101", predicateList[0].value
    }


    @Test
    void testSimpleJSONQueryWithOperator() {
        String temp = """ {"count" : {"\$gt" : 10}} """
        JSONQuery jsonQuery = new JSONQuery(temp)
        def predicateList = jsonQuery.getWhereClauseConditions()
        assertNotNull predicateList
        assertEquals 1, predicateList.size()
        assertEquals "count", predicateList[0].property
        assertEquals Operators.GREATER_THAN, predicateList[0].operator
        assertEquals "10", predicateList[0].value
    }


    @Test
    void testSimpleJSONQueryWithInvalidOperator() {
        String temp = """ {"count" : {"temp" : 10}} """
        JSONQuery jsonQuery = new JSONQuery(temp)
        shouldFail(ApplicationException) {
            def predicateList = jsonQuery.getWhereClauseConditions()
        }
    }


    @Test
    void testLogicalOperatorAND() {
        String temp = """ {"description" : "Beginning mathematics", "number" : {"\$lte" : 20}} """
        JSONQuery jsonQuery = new JSONQuery(temp)
        def predicateList = jsonQuery.getWhereClauseConditions()
        assertNotNull predicateList
        assertEquals 2, predicateList.size()

        def condition = predicateList[0]
        assertEquals "description", condition.property
        assertEquals Operators.EQUALS, condition.operator
        assertEquals "Beginning mathematics", condition.value

        condition = predicateList[1]
        assertEquals "number", condition.property
        assertEquals Operators.LESS_THAN_EQUALS, condition.operator
        assertEquals "20", condition.value
    }


    @Test
    void testLogicalOperatorOR() {
        String temp = """ {"\$or" : [{"qty" : {"\$gt" : 10}}, {"amt" : {"\$lt" : 20}}]} """
        JSONQuery jsonQuery = new JSONQuery(temp)
        def predicateList = jsonQuery.getWhereClauseConditions()
        assertNotNull predicateList
        assertEquals 1, predicateList.size()
        assertNull predicateList[0].property
        assertEquals JSONQuery.LOGICAL_OR, predicateList[0].operator
        assertTrue predicateList[0].value instanceof List
        assertEquals 2, predicateList[0].value.size()

        def condition = predicateList[0].value[0]
        assertEquals "qty", condition.property
        assertEquals Operators.GREATER_THAN, condition.operator
        assertEquals "10", condition.value

        condition = predicateList[0].value[1]
        assertEquals "amt", condition.property
        assertEquals Operators.LESS_THAN, condition.operator
        assertEquals "20", condition.value
    }

}
