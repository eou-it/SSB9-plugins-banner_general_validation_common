/** *****************************************************************************
 Copyright 2014-2018 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.plugin.location.'banner-seeddata-catalog' = "../banner_seeddata_catalog.git"
grails.plugin.location.'banner-core'="../banner_core.git"
grails.plugin.location.'banner-general-utility' = "../banner_general_utility.git"
grails.plugin.location.'banner-spring-security-cas'   = "../banner_spring_security_cas.git"
grails.plugin.location.'banner-spring-security-saml'   = "../banner_spring_security_saml.git"


grails.project.dependency.resolver = "maven"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        if (System.properties['PROXY_SERVER_NAME']) {
            mavenRepo "${System.properties['PROXY_SERVER_NAME']}"
        }
        grailsCentral()
        mavenCentral()
        mavenRepo "http://repository.jboss.org/maven2/"
        mavenRepo "https://code.lds.org/nexus/content/groups/main-repo"
    }

    plugins {

    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
    }

}

// CodeNarc rulesets
codenarc.ruleSetFiles="rulesets/banner.groovy"
codenarc.reportName="target/CodeNarcReport.html"
codenarc.propertiesFile="grails-app/conf/codenarc.properties"