package org.capstone.mtgwizard;

import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;

import static org.mockito.Mockito.*;

@Disabled
public class GetCriteriaTest {

    AllPrintingsDatabaseHandler testHandler;
    String[] tags;

    GetCriteriaTest() {
        // Dummy all printings handler
        testHandler = new AllPrintingsDatabaseHandler("jdbc:mysql://localhost:3306/mtg", "mtguser", "password");
        // Tags that are searched for in query
        tags = new String[]{"name:", "set:"};
    }

//    @BeforeEach
//    void setup() {
//        // Dummy all printings handler
//        testHandler = mock(AllPrintingsDatabaseHandler.class);
//        // Tags that are searched for in query
//        tags = new String[]{"name:", "set:"};
//    }

    @Test
    void testBothTags() {

        // Dummy query
        String query = "name: Black Lotus set: 2ED";

        // Criteria found from function
        String[] foundCriteria = testHandler.getCriteria(query, tags);
        // Criteria that should have been found
        String[] correctCriteria = {"Black Lotus", "2ED"};

        // Asserting equality
        assertArrayEquals(correctCriteria, foundCriteria);
    }

    @Test
    void testOnlyNameTag() {

        // Dummy query
        String query = "name: Black Lotus";

        // Criteria found from function
        String[] foundCriteria = testHandler.getCriteria(query, tags);
        // Criteria that should have been found
        String[] correctCriteria = {"Black Lotus", ""};

        // Asserting equality
        assertArrayEquals(correctCriteria, foundCriteria);
    }

    @Test
    void testOnlySetTag() {

        // Dummy query
        String query = "set: 2ED";

        // Criteria found from function
        String[] foundCriteria = testHandler.getCriteria(query, tags);
        // Criteria that should have been found
        String[] correctCriteria = {"", "2ED"};

        // Asserting equality
        assertArrayEquals(correctCriteria, foundCriteria);
    }

    @Test
    void testEmptyQuery() {

        // Dummy query
        String query = "";

        // Criteria found from function
        String[] foundCriteria = testHandler.getCriteria(query, tags);
        // Criteria that should have been found
        String[] correctCriteria = {"", ""};

        // Asserting equality
        assertArrayEquals(correctCriteria, foundCriteria);
    }

    @Test
    void testEmptyTags() {

        // Dummy query
        String query = "name:set:";

        // Criteria found from function
        String[] foundCriteria = testHandler.getCriteria(query, tags);
        // Criteria that should have been found
        String[] correctCriteria = {"", ""};

        // Asserting equality
        assertArrayEquals(correctCriteria, foundCriteria);
    }

    @Test
    void testLongQuery() {

        // Dummy query
        String query = "name:Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." +
                "set:Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        // Criteria found from function
        String[] foundCriteria = testHandler.getCriteria(query, tags);
        // Criteria that should have been found
        String[] correctCriteria = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."};

        // Asserting equality
        assertArrayEquals(correctCriteria, foundCriteria);
    }


}
