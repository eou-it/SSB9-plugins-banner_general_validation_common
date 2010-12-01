/** *****************************************************************************
  © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */


import com.sungardhe.banner.configuration.ApplicationConfigurationUtils as ConfigFinder
import grails.plugins.springsecurity.SecurityConfigType

// ******************************************************************************
//
//                       +++ EXTERNALIZED CONFIGURATION +++
//
// ******************************************************************************
//
// Config locations should be added to the map used below. They will be loaded based upon this search order:
// 1. Load the configuration file if its location was specified on the command line using -DmyEnvName=myConfigLocation
// 2. Load the configuration file if it exists within the user's .grails directory (i.e., convenient for developers)
// 3. Load the configuration file if its location was specified as a system environment variable
//
// Map [ environment variable or -D command line argument name : file path ]
grails.config.locations = [] // leave this initialized to an empty list, and add your locations
// in the APPLICATION CONFIGURATION section below.

def locationAdder = ConfigFinder.&addLocation.curry(grails.config.locations)

[bannerGrailsAppConfig: "${userHome}/.grails/banner_on_grails-local-config.groovy",
        customRepresentationConfig: "grails-app/conf/CustomRepresentationConfig.groovy",
].each { envName, defaultFileName -> locationAdder(envName, defaultFileName) }



grails.project.groupId = "com.sungardhe" // used when deploying to a maven repo

grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html: ['text/html', 'application/xhtml+xml'],
        xml: ['text/xml', 'application/xml', 'application/vnd.sungardhe.student.v0.01+xml'],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        all: '*/*',
        json: ['application/json', 'text/json'],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data'
]

// The default codec used to encode data with ${}
grails.views.default.codec = "html" // none, html, base64  **** Charlie note: Setting this to html will ensure html is escaped, to prevent XSS attack ****
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"

grails.converters.domain.include.version = true
grails.converters.json.date = "javascript"
grails.converters.json.pretty.print = true

banner {
    sso {
        authenticationProvider = 'default'
        authenticationAssertionAttribute = 'udcId'
    }
}

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = false

// enable GSP preprocessing: replace head -> g:captureHead, title -> g:captureTitle, meta -> g:captureMeta, body -> g:captureBody
grails.views.gsp.sitemesh.preprocess = true

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
}

// ******************************************************************************
//
//                       +++ DATA ORIGIN CONFIGURATION +++
//
// ******************************************************************************
// This field is a Banner standard, along with 'lastModifiedBy' and lastModified.
// These properties are populated automatically before an entity is inserted or updated
// within the database. The lastModifiedBy uses the username of the logged in user,
// the lastModified uses the current timestamp, and the dataOrigin uses the value
// specified here:
dataOrigin = "Banner"

// ******************************************************************************
//
//                       +++ LOGGER CONFIGURATION +++
//
// ******************************************************************************
// See http://grails.org/doc/1.1.x/guide/3.%20Configuration.html#3.1.2%20Logging
// for more information about log4j configuration.
log4j = {
    appenders {
        file name: 'file', file: 'target/logs/development.log'
    }
    root {
        off 'stdout', 'file'
        additivity = true
    }

    // Configure logging for other classes (e.g., in src/ or grails-app/utils/) here:
    // info 'com.sungardhe.banner.representations'
    // info 'com.sungardhe.banner.supplemental.SupplementalDataService'
    off  'com.sungardhe.banner.security'
    off  'com.sungardhe.banner.db'
    off  'com.sungardhe.banner.student'
    off  'com.sungardhe.banner.general.system.CollegeController'
    off  'com.sungardhe.banner.general.system.CollegeService'

    // Grails framework classes
    off  'org.codehaus.groovy.grails.web.servlet'        // controllers
    off  'org.codehaus.groovy.grails.web.pages'          // GSP
    off  'org.codehaus.groovy.grails.web.sitemesh'       // layouts
    off  'org.codehaus.groovy.grails.web.mapping.filter' // URL mapping
    off  'org.codehaus.groovy.grails.web.mapping'        // URL mapping
    off  'org.codehaus.groovy.grails.commons'            // core / classloading
    off  'org.codehaus.groovy.grails.plugins'            // plugins
    off  'org.codehaus.groovy.grails.orm.hibernate'      // hibernate integration
    off  'org.springframework'                           // Spring IoC
    off  'org.hibernate'                                 // hibernate ORM

    off  'grails.plugins.springsecurity'
    off  'org.springframework.security'

    // Grails provides a convenience for enabling logging within artefacts, using 'grails.app.XXX'.
    // Unfortunately, this configuration is not effective when 'mixing in' methods that perform logging.
    // Therefore, for controllers and services it is recommended that you enable logging using the controller
    // or service class name (see above 'class name' based configurations).  For example:
    //     all  'com.sungardhe.banner.testing.FooController' // turns on all logging for the FooController
    //
    off 'grails.app' // apply to all artefacts
    // off 'grails.app.<artefactType>.ClassName // where artefactType is in:
    //  bootstrap  - For bootstrap classes
    //  dataSource - For data sources
    //  tagLib     - For tag libraries
    //  service    // Not effective with mixins -- see comment above
    //  controller // Not effective with mixins -- see comment above
    //  domain     - For domain entities

    off 'com.sungardhe.banner.student.system'
}

// ******************************************************************************
//
//                       +++ FORM-CONTROLLER MAP +++
//
// ******************************************************************************
// This map relates controllers to the Banner forms that it replaces.  This map
// supports 1:1 and 1:M (where a controller supports the functionality of more than
// one Banner form.  This map is critical, as it is used by the security framework to
// set appropriate Banner security role(s) on a database connection. For example, if a
// logged in user navigates to the 'medicalInformation' controller, when a database
// connection is attained and the user has the necessary role, the role is enabled
// for that user and Banner object.
formControllerMap = [
        'validation': ['GTVPARS', 'GTVINSM', 'STVDEPT', 'STVDISA', 'STVGMOD', 'STVSUBJ', 'STVLEVL', 'STVCAMP', 'STVDIVS', 'STVMDEQ', 'STVCOLL', 'STVSCHD'],
        'medicalinformation': ['GOAMEDI'],
        'integrationpartnersystemrule': ['GORINTG'],
        'function': ['GTVFUNC'],
        'integrationpartner': ['GTVINTP'],
        'instructionalmethod': ['GTVINSM'],
        'meetingtype': ['GTVMTYP'],
        'partition': ['GTVPARS'],
        'scheduletoolstatus': ['GTVSCHS'],
        'durationunit': ['GTVDUNT'],
        'personidentificationname': ['SPAIDEN'],
        'lookup': ['SPAIDEN', 'GTVPARS', 'GTVINSM'],
        'awardcategory': ['STVACAT'],
        'admissionrequiest': ['STVADMR'],
        'catalogapproval': ['STVAPRV'],
        'assignmenttype': ['STVASTY'],
        'attribute': ['STVATTR'],
        'studentattribute': ['STVATTS'],
        'block': ['STVBLCK'],
        'building': ['STVBLDG'],
        'campus': ['STVCAMP'],
        'cipcode': ['STVCIPC'],
        'controlrule': ['STVCNTR'],
        'college': ['STVCOLL'],
        'committeeandservicetype': ['STVCOMT'],
        'coursestatus': ['STVCSTA'],
        'catalogelementone': ['STVCUDA'],
        'catalogelementtwo': ['STVCUDB'],
        'catalogelementthree': ['STVCUDC'],
        'catalogelementfour': ['STVCUDD'],
        'catalogelementfive': ['STVCUDE'],
        'catalogelementsix': ['STVCUDF'],
        'classcode': ['STVCLAS'],
        'classification': ['STVCSSL'],
        'cohort': ['STVCHRT'],
        'degree': ['STVDEGC'],
        'degreelevel': ['STVDEGC'],
        'department': ['STVDEPT'],
        'disability': ['STVDISA'],
        'division': ['STVDIVS'],
        'evaluationquestion': ['STVEVAL'],
        'facultycontract': ['STVFCNT'],
        'facultystatus': ['STVFCST'],
        'facultycategory': ['STVFCTG'],
        'facultystafftype': ['STVFSTP'],
        'feetype': ['STVFTYP'],
        'gradingmode': ['STVGMOD'],
        'interest': ['STVINTS'],
        'level': ['STVLEVL'],
        'disabilityassistance': ['STVMDEQ'],
        'medicalequipment': ['STVMDEQ'],
        'medicalcondition': ['STVMEDI'],
        'occupationalcourse': ['STVOCCS'],
        'prerequisitewaiver': ['STVPWAV'],
        'feeassessmentrate': ['STVRATE'],
        'residency': ['STVRESD'],
        'termrestriction': ['STVRTRM'],
        'sourceandbackgroundinformation': ['STVSBGI'],
        'schedulecontract': ['STVSCCD'],
        'scheduletype': ['STVSCHD'],
        'studenttype': ['STVSTYP'],
        'subject': ['STVSUBJ'],
        'testscore': ['STVTESC'],
        'term': ['STVTERM'],
        'taxonomyofprograms': ['STVTOPS'],
        'visatype': ['STVVTYP'],
        'workloadrule': ['STVWKLD'],
        'programrules': ['SMAPRLE'],
        'coursegeneralinformation': ['SCACRSE'],
        'courseroomattribute': ['SCACRSE'],
        'coursepartition': ['SCACRSE'],
        'coursedetailinformation': ['SCADETL'],
        'courseattribute': ['SCADETL'],
        'coursecorequisite': ['SCADETL'],
        'coursesupplementaldata': ['SCADETL'],
        'coursedescription': ['SCADETL'],
        'coursetext': ['SCADETL'],
        'coursefee': ['SCADETL'],
        'courseintegrationpartners': ['SCADETL'],
        'coursetransferinstituitions': ['SCADETL'],
        'courseequivalent': ['SCADETL'],
        'coursedetailinformationpage': ['SCADETL'],
        'coursetestscorerestrictionandprerequisites': ['SCAPREQ'],
        'coursearearestriction': ['SCAPREQ'],
        'coursetestscorerestrictioninformationpage': ['SCAPREQ'],
        'coursemajorrestriction': ['SCARRES'],
        'courseregistrationrestrictions': ['SCARRES'],
        'coursedepartmentrestriction': ['SCARRES'],
        'coursecollegerestriction': ['SCARRES'],
        'courseprogramrestriction': ['SCARRES'],
        'catalogcampusrestriction': ['SCARRES'],
        'coursesearch': ['SCASRCH'],
        'coursetermrestriction': ['SCASRES'],
        'coursecampusrestriction': ['SCASRES'],
        'courselevelrestriction': ['SCASRES'],
        'courseclassrestriction': ['SCASRES'],
        'coursedegreerestriction': ['SCASRES'],
        'coursecollegedepartmenttext': ['SCATEXT'],
        'coursecollegedepartmentdescription': ['SCATEXT'],
        'coursecollegedepartment': ['SCATEXT'],
        'facultymemberbase': ['SIBINST'],
        'facultyinstructionalassignment': ['SIRASGN'],
        'campussecurity': ['SOAPROF'],
        'arealibrary': ['SSAPREQ'],
        'classmeetingtime': ['SSASECT'],
        'sectionscheduleexclusiondate': ['SSAEXCL'],
        'coursechortrestriction': ['SCASRES'],
        'coursestudentattributerestriction': ['SCARRES'],
        'institutionaldescription': ['GUAINIT'],
        'sectionstatus': ['STVSSTS'],
        'partofterm': ['STVPTRM'],
        'sessioncode': ['STVSESS'],
		'schedule': ['SSASECT'],
        'section': ['SSASECT'],
        'specialapproval': ['STVSAPR'],
        'academiccalendartype': ['STVACC:'],
        'attendanceaccountingmethod': ['STVACCT'],
        'termbase': ['SOATERM'],
        'partoftermrule': ['SOATERM'],
        'coursesyllabus': ['SCASYLB'],
        'coursesyllabuslearningobective': ['SCASYLB'],
        'coursesyllabusrequiredmaterial': ['SCASYLB'],
        'coursesyllabustechnicalrequirement': ['SCASYLB'],
        'courselabordistribution': ['SCACLBD'],
        'coursebasemaintenance': ['SCABASE'],
        'catalogprerequisiteandtestscorerestrictions': ['SCAPREQ'],
        'buildingandroomattribute': ['STVRDEF'],
        'dummyController': ['ANY_FORM', 'ANOTHER_FORM'],
        'basiccourseinformation': ['SCACRSE'],
        'mutualcourseexclusion': ['SCAMEXC'],
        'fund': ['SCAMEXC'],
        'collegeanddepartmenttext': ['SCATEXT'],
        'catalogschedulerestrictions': ['SCASRES'],
        'historygradingcodemaintenance': ['SHAGRDE'],
        'studentaccountsdetailchargepayment': ['TSADETC'],
        'detailchargepaymentcategory': ['TTVDCAT'],
        'sectiontestscoreandprerequisiterestriction': ['SSAPREQ'],
        'sectioncomment': ['SSATEXT'],
        'sectiondescription': ['SSATEXT'],
        'sectionlabordistribution': ['SSACLBD'],
        'sectionfacultywebdisplaycontrol': ['SSAWSEC'],
        'sectionstudentwebdisplaycontrol': ['SSAWSEC'],
        'sectionschedulevoerride': ['SSAOVRR'],
        'sectionarearestriction': ['SSAPREQ'],
        'sectioncorequisite': ['SSADETL'],
        'sectionlink': ['SSADETL'],
        'sectiondegreeprogramattribute': ['SSADETL'],
        'sectionmajorrestriction': ['SSADETL'],
        'sectioncontract': ['SSADETL'],
        'sectionfee': ['SSADETL'],
        'sectionblock': ['SSADETL', 'SSABLCK', 'SSABLKQ', 'SSABSCQ'],
        'sectionevaluation': ['SSAEVAL'],
        'sectionsyllabus': ['SSASYLB'],
        'sectionsyllabuslongcoursetitle': ['SSASYLB'],
        'sectionsyllabuslearningobjective': ['SSASYLB'],
        'sectionsyllabusrequiredmaterial': ['SSASYLB'],
        'sectionsyllabustechnicalrequirement': ['SSASYLB'],
        'scheduleprocessingrules': ['SSARULE'],
        'sectionolrregistrationstatus': ['SSARULE'],
        'sectionolrextensionrule': ['SSARULE'],
        'sectionolrrefunding': ['SSARULE'],
		'waitlistautomationsectioncontrol'    : [ 'SSAWLSC'] ,
        'sectionprerequisiteandtestscorerestrictions': ['SSAPREQ'],
        'facultymemberworkloadtermrule': ['SIAFLRT'],
        'facultyworkloadtermrules': ['SIAFLRT'],
        'scheduledetail': ['SSADETL'],
        'studentaccountsreceivableuserrestrictions': ['TGAUPRF'],
        'studentaccountsreceivablebillingControl': ['TGACTRL', 'TSACTRL'],
        'studentaccountsreceivableuseridprofile': ['TGAUPRF', 'TGIUPRF'],
        'studentbase': ['SGASTDN'],
        'studentcooperativeeducationduty': ['SGACOOP'],
        'studentcooperativeeducation': ['SGACOOP'],
        'nametype': ['GTVNTYP'],
        'fgacdomain': ['GTVFDMN'],
        'historyinstitutionalcoursetermmaintenance': ['SHATCKN'],
        'historyinstitutionalcoursemaintenancetermheader': ['SHAINST'],
        'historyinstitutionalcoursemaintenancelevelapplied': ['SHATCKN'],
        'studentcentricperiod': ['SOASCPS'],
        'registrationstudentregistration': ['SFAREGS'],
        'basicskill': ['STVBSKL', 'SHATCKN'],
        'cohortreason': ['STVCHRT' , 'SGASTDN'],
        'gradecomment': ['STVGCMT', 'SHATCKN'],
        'combinedacademicstanding': ['STVCAST', 'SHAINST'],
        'cooperativeeducation': ['STVCOPC', 'SGACOOP'],
        'employer': ['STVEMPL', 'SHACOOP'],
        'enrollemntStatus': ['STVESTS', 'SFAREGS'],
        'incomerange': ['STVINTC', 'SGASTDN'],
        'studentcentricperiodcyclevalidation': ['STVSCPC', 'SGASTDN'],
        'studentstatus': ['STVSTYP', 'SGASTDN'],
        'withdrawalreason': ['STVWRSN', 'SGASTDN'],
        'withdrawalstatus': ['STVWDRL', 'SGASTDN'],
        'geographicregionascostcenterinformationbydisctirctordivision': ['GTVDICD', 'SPAIDEN'],
        'nation': ['STVNATN', 'SPAIDEN'],
        'site': ['STVSITE', 'SGASTDN'],
        'state': ['STVSTAT', 'SPAIDEN'],
        'academicstanding': ['STVASTD', 'SPAIDEN'],
        'admissiontype': ['STVADMT', 'SGASTDN'],
        'apprenticeship': ['STVAPRN', 'SGASTDN'],
        'careerplanning': ['STVCAPL', 'SGASTDN'],
        'curriculumactivitystatus': ['STVCACT', 'SGASTDN'],
        'curriculumstatus': ['STVCSTS', 'SGASTDN'],
        'educationgoal': ['STVEGOL', 'SGASTDN'],
        'educationlevel': ['STVEGLV', 'SGASTDN'],
        'employmentexpectation': ['STVEMEX', 'SGASTDN'],
        'examination': ['STVEXAM', 'SGASTDN'],
        'gainstatus': ['STVgain', 'SGASTDN'],
        'leaveofabsence': ['STVLEAV', 'SGASTDN'],
        'orientationsession': ['STVORNT', 'SGASTDN'],
        'practicaltraining': ['STVPRAC', 'SGASTDN'],
        'progressevaluation': ['STVPREV', 'SGASTDN'],
        'registrationreason': ['STVRGRE', 'SGASTDN'],
        'timestatus': ['STVTMST', 'SGASTDN'],
        'transfercenter': ['STVTRNS', 'SGASTDN'],
        'vocationaleducation': ['STVVOED', 'SGASTDN'],
        'registrationstudentcourseregistration': ['SFAREGS'] ,
     'sectionwebcontrols'                  : [ 'SSAWSEC' ],
     	'sectionrestrictions'   			  : [ 'SSARRES' ],
        'sectionmajorrestriction'             : [ 'SSARRES' ],
     	'sectiondepartmentrestriction'        : [ 'SSARRES' ],
]

grails.plugins.springsecurity.useRequestMapDomainClass = false
grails.plugins.springsecurity.providerNames = ['bannerAuthenticationProvider']
//grails.plugins.springsecurity.rejectIfNoRule = true

grails.plugins.springsecurity.filterChain.chainMap = [
    '/api/**': 'authenticationProcessingFilter,basicAuthenticationFilter,securityContextHolderAwareRequestFilter,anonymousProcessingFilter,basicExceptionTranslationFilter,filterInvocationInterceptor',
    '/**': 'securityContextPersistenceFilter,logoutFilter,authenticationProcessingFilter,securityContextHolderAwareRequestFilter,anonymousProcessingFilter,exceptionTranslationFilter,filterInvocationInterceptor'
]

grails.plugins.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap


// ******************************************************************************
//
//                       +++ INTERCEPT-URL MAP +++
//
// ******************************************************************************

grails.plugins.springsecurity.interceptUrlMap = [
        '/': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/zkau/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/zkau**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/login/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/mainPage**': ['ROLE_SCACRSE_BAN_DEFAULT_M'],
        '/menu/**': ['ROLE_SCACRSE_BAN_DEFAULT_M'],
        '/index**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/logout/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/js/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/css/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/images/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/plugins/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/errors/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],

        // ALL URIs specified with the BannerAccessDecisionVoter.ROLE_DETERMINED_DYNAMICALLY
        // 'role' (it's not a real role) will result in authorization being determined based
        // upon a user's role assignments to the corresponding form (see 'formControllerMap' above).
        // Note: This 'dynamic form-based authorization' is performed by the BannerAccessDecisionVoter
        // registered as the 'roleVoter' within Spring Security.
        //
        // Only '/name_used_in_formControllerMap/' and '/api/name_used_in_formControllerMap/'
        // URL formats are supported.  That is, the name_used_in_formControllerMap must be first, or
        // immediately after 'api' -- but it cannot be otherwise nested. URIs may be protected
        // by explicitly specifying true roles instead -- as long as ROLE_DETERMINED_DYNAMICALLY
        // is NOT specified.
        //
       '/**': [ 'ROLE_DETERMINED_DYNAMICALLY' ]
]





     