/*******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.restfulapi

/**
 * Duck-typed ApplicationException that will be treated by RestfulApiController as ValidationException.
 *
 * Duck-typed Application Exceptions that have a returnMap closure will be passed a localizer instance that
 * uses the controller's message() method to localize strings.
 *
 * Note: As per Ellucian REST strategy, if an application exception represents a validation exception,
 * it needs to return a 400 status code, and should also return an 'X-Status-Reason:Validation failed' header.
 *
 */
class RestfulApiValidationException extends RuntimeException {

    private Integer httpStatusCode = 400
    private def headers = ['X-Status-Reason': 'Validation failed']
    private String messageCode //  key in resource bundle
    private def messageArgs //  parameter substitution

    RestfulApiValidationException(def messageCode, def args = null) {
        this.messageCode = messageCode
        this.messageArgs = args
    }


    public def getHttpStatusCode() {
        return httpStatusCode
    }


    public def returnMap = { localize ->
        def map = [:]

        map.headers = headers

        // localized string to be returned in the X-hedtech-message header
        map.message = localize(code: messageCode, args: messageArgs)

        // object to be rendered as JSON or xml in the response body
        map.errors = [[type: "validation", messageCode: messageCode, message: map.message]]

        return map
    }

}
