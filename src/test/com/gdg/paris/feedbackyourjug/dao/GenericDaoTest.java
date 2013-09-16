package com.gdg.paris.feedbackyourjug.dao;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Description:
 * User: sfeir
 */
public class GenericDaoTest {

    private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() throws Exception {
        helper.setUp();
    }

    @Test
    public void testDao() {

    }


}
