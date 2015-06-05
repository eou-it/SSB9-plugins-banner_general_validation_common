package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by invthannee on 6/4/2015.
 * Integration Test cases for Read Only view
 */
class AcademicDisciplineIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testCount() {
        assert AcademicDiscipline.count() > 0
    }

    @Test
    void testCountByType() {
        assert AcademicDiscipline.countByType("major") > 0
        assert AcademicDiscipline.countByType("minor") > 0
        assert AcademicDiscipline.countByType("concentration") > 0
    }

    @Test
    void testFetchByType() {
        assert AcademicDiscipline.findAllByType("major").size > 0
        assert AcademicDiscipline.findAllByType("minor").size > 0
        assert AcademicDiscipline.findAllByType("concentration").size > 0
    }

    @Test
    void testFetchByguid() {
        assert AcademicDiscipline.findAllByGuid("").size == 0
        assert AcademicDiscipline.findAllByGuid("minor").size == 0
        assert AcademicDiscipline.findAllByGuid(null).size == 0
    }

}
