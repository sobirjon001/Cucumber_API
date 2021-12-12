Feature: Create, update, delete data

  This is fundamental functionality for data manipulation
  All other scenarios depend on this crucial features
  This Feature is a great candidate for Smoke test

  @P-1001
  Scenario: User data manipulations
    Given I create new user saving payload with name "P-1001-user-1" with data
      | id         | uiid::      |
      | username   | username::  |
      | firstName  | firstName:: |
      | lastName   | lastName::  |
      | email      | email::     |
      | password   | password::  |
      | phone      | phone::     |
      | userStatus | 1           |
    And I wait for 2 minutes
    Then I validate user by username "P-1001-user-1:username" created with data
      | id         | P-1001-user-1:id         |
      | username   | P-1001-user-1:username   |
      | firstName  | P-1001-user-1:firstName  |
      | lastName   | P-1001-user-1:lastName   |
      | email      | P-1001-user-1:email      |
      | password   | P-1001-user-1:password   |
      | phone      | P-1001-user-1:phone      |
      | userStatus | P-1001-user-1:userStatus |
    And I wait for 2 minutes
    When I update user by username "P-1001-user-1:username" and save with name "P-1001-user-2" with data
      | id         | uiid::      |
      | username   | username::  |
      | firstName  | firstName:: |
      | lastName   | lastName::  |
      | email      | email::     |
      | password   | password::  |
      | phone      | phone::     |
      | userStatus | 1           |
    And I wait for 2 minutes
    Then I validate user by username "P-1001-user-1:username" does not exist
    Then I validate user by username "P-1001-user-2:username" created with data
      | id         | P-1001-user-2:id         |
      | username   | P-1001-user-2:username   |
      | firstName  | P-1001-user-2:firstName  |
      | lastName   | P-1001-user-2:lastName   |
      | email      | P-1001-user-2:email      |
      | password   | P-1001-user-2:password   |
      | phone      | P-1001-user-2:phone      |
      | userStatus | P-1001-user-2:userStatus |
    And I wait for 2 minutes
    When I delete a user by username "P-1001-user-2:username"
    And I wait for 2 minutes
    Then I validate user by username "P-1001-user-2:username" does not exist

