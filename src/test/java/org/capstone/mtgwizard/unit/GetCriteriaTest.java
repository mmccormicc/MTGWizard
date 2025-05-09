package org.capstone.mtgwizard.unit;

import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GetCriteriaTest {

    AllPrintingsDatabaseHandler testHandler;
    String[] tags;


    @BeforeEach
    void setup() {
        // All printings handler from local MySQL database for faster testing
        testHandler = new AllPrintingsDatabaseHandler("jdbc:mysql://localhost:3306/mtg", "mtguser", "password");
        // Tags that are searched for in query
        tags = new String[]{"name:", "set:"};
    }

    @Test
    void getCriteria_BothTagsInQuery_ReturnsCorrectCriteria() {

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
    void getCriteria_OnlyNameTagInQuery_ReturnsCorrectCriteria() {

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
    void getCriteria_OnlySetTagInQuery_ReturnsCorrectCriteria(){

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
    void getCriteria_EmptyQuery_ReturnsEmptyCriteria() {

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
    void getCriteria_EmptyTags_ReturnsEmptyCriteria() {

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
    void getCriteria_LongQueryWithBothTags_ReturnsCorrectCriteria() {

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
