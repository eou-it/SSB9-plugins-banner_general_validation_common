/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.exceptions.NotFoundException
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

import javax.servlet.http.HttpServletRequest
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

abstract class NonLdmService {

    static Date convertString2Date(String strDate, String format = "yyyy-MM-dd") {
        DateFormat dateFormat = new SimpleDateFormat(format)
        dateFormat.lenient = false

        Date date
        try {
            date = dateFormat.parse(strDate)
        } catch (ParseException pe) {
            throw new ApplicationException(GlobalUniqueIdentifierService.API, new BusinessLogicValidationException("date.invalid.format.message", ["BusinessLogicValidationException"]))
        }
        return date
    }


    static void throwBusinessLogicValidationException(ApplicationException ae) {
        if (ae.wrappedException instanceof NotFoundException) {
            throw new ApplicationException(ae.getEntityClassName(), new BusinessLogicValidationException("not.found.message", [ae.getUserFriendlyName(), ae.wrappedException.id]))
        } else if (ae.getType() == "RuntimeException" && ae.wrappedException.message.startsWith("@@r1")) {
            String message = ae.wrappedException.message
            Map rcps = getResourceCodeAndParams(message)
            throw new ApplicationException(ae.getEntityClassName(), new BusinessLogicValidationException(rcps.resourceCode, rcps.bindingParams))
        } else {
            throw ae
        }
    }


    private static Map getResourceCodeAndParams(String msg) {
        List<String> bindingParams = parse(extractAPIErrorText(msg))
        if (bindingParams.size() < 2) {
            return [resourceCode: "unknown.banner.api.exception"]
        }
        if (!bindingParams.get(0).equals("r1")) {
            return [resourceCode: "unknown.banner.api.exception"]
        } else {
            String resourceCode = bindingParams.get(1)
            bindingParams.remove(0)
            bindingParams.remove(0)

            [resourceCode: resourceCode, bindingParams: bindingParams]
        }
    }


    private static List<String> parse(String source) {
        List<String> values = new ArrayList<String>()
        String currentChar = null
        StringBuffer sb = new StringBuffer()
        boolean lastCharWasEscape = false
        for (int i = 0; i < source.length(); i++) {
            currentChar = source.substring(i, i + 1)
            if (lastCharWasEscape) {
                if (currentChar.equals("\\")) {
                    lastCharWasEscape = false
                    sb.append("\\")
                    currentChar = null
                } else if (currentChar.equals(":")) {
                    lastCharWasEscape = false
                    sb.append(":")
                    currentChar = null
                } else {
                    // didn't escape anything, discard
                    sb.append(currentChar)
                    lastCharWasEscape = false
                    currentChar = null
                }
            } else if (currentChar.equals("\\")) {
                lastCharWasEscape = true
            } else if (currentChar.equals(":")) {
                // field separater
                values.add(sb.toString())
                sb = new StringBuffer()
                currentChar = null
            } else {
                sb.append(currentChar)
                currentChar = null
            }
        }
        if (currentChar != null) {
            sb.append(currentChar)
        }
        values.add(sb.toString())
        return values
    }


    private static String extractAPIErrorText(String message) {
        if (message == null) {
            return message
        }
        String tmp = message.substring(message.indexOf("@@") + 2, message.length())
        int end = tmp.indexOf("@@")
        return tmp.substring(0, end)
    }


    static String getAcceptVersion(List<String> apiVersions) {
        String representationVersion = getResponseRepresentationVersion()
        if (apiVersions) {
            if (representationVersion == null) {
                // Assume latest (current) version
                representationVersion = getMaximumVersion(apiVersions)
                setRequestAttributeOverwriteMediaType(representationVersion)
            } else {
                representationVersion = getPossibleVersion(apiVersions, representationVersion)
            }
        }
        return representationVersion
    }


    private static String getResponseRepresentationVersion() {
        String version
        String acceptHeader = responseBodyMediaType()
        if (acceptHeader) {
            int indexOfDotBeforeVersion = acceptHeader.lastIndexOf(".")
            int indexOfPlus = acceptHeader.lastIndexOf("+")
            if (indexOfDotBeforeVersion != -1 && indexOfPlus != -1 && indexOfDotBeforeVersion + 1 < indexOfPlus) {
                version = acceptHeader.substring(indexOfDotBeforeVersion + 1, indexOfPlus)
                if (!version?.toLowerCase()?.startsWith("v")) {
                    // May be generic Accept header like "application/vnd.hedtech.integration+json"
                    version = null
                }
            }
        }
        return version?.toLowerCase()
    }


    private static String responseBodyMediaType() {
        HttpServletRequest request = getHttpServletRequest()
        Enumeration<String> values = request.getHeaders("Accept")
        if (values) {
            String accept
            while (values.hasMoreElements()) {
                accept = values.nextElement()
            }
            return accept
        } else {
            return request?.getHeader("Accept")
        }
    }


    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes()
        if (requestAttributes && requestAttributes instanceof ServletRequestAttributes) {
            request = ((ServletRequestAttributes) requestAttributes).getRequest()
        }
        return request
    }


    private static String getMaximumVersion(Collection<String> versions) {
        String val
        if (versions) {
            Collection<Integer> list = versions.collect { it.substring(1).toInteger() }.sort()
            val = "v${list.last()}"
        }
        return val
    }


    private static String getPossibleVersion(Collection<String> versions, String representationVersion) {
        String val
        if (versions && representationVersion) {
            Collection<Integer> list = versions.collect { it.substring(1).toInteger() }.sort()
            Integer version = representationVersion.substring(1).toInteger()
            if (version < list.first()) {
                val = "v${list.first()}"
            } else if (version > list.last()) {
                val = "v${list.last()}"
            } else {
                int index = list.findLastIndexOf { it <= version }
                if (index != -1) {
                    val = "v${list.get(index)}"
                }
            }
        }
        return val
    }


    private static void setRequestAttributeOverwriteMediaType(String version) {
        HttpServletRequest request = getHttpServletRequest()
        if (request.getAttribute("overwriteMediaTypeHeader") == null) {
            request.setAttribute("overwriteMediaTypeHeader", "application/vnd.hedtech.${version}+json")
        }
    }

}
