/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.plugin.location.'banner-core' = "../banner_core.git"
grails.plugin.location.'banner-seeddata-catalog' = "../banner_seeddata_catalog.git"
grails.plugin.location.'banner-codenarc' = "../banner_codenarc.git"
grails.plugin.location.'i18n-core'="../i18n_core.git"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        if (System.properties['PROXY_SERVER_NAME']) {
            mavenRepo "${System.properties['PROXY_SERVER_NAME']}"
        } else {
            grailsPlugins()
            grailsHome()
            grailsCentral()
            mavenCentral()
            mavenRepo "http://repository.jboss.org/maven2/"
            mavenRepo "http://repository.codehaus.org"
        }
    }

    plugins {
        runtime  ":hibernate:3.6.10.10"
        build ":tomcat:7.0.52.1"
        compile ":functional-test:2.0.0" // If the functional-test plugin is being used
        test ':code-coverage:1.2.5'

    }
	
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.5'
    }

}
