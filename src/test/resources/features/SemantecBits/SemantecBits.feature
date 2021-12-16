@SemanticBits
Feature: Some of interview practices for Semantic Bits

  @3001
  Scenario: Data manipulations
    Given I create new issue and save payload by name "3001-1" with data
      | snowids        | 123, 456, 789                         |
      | date_reported  | firstDayOfMonth::                     |
      | status         | Open                                  |
      | category       | Patient Assessment                    |
      | description    | Initial validation of main complaints |
      | date_resolved  | null::                                |
      | provider_types | HHA, IRF, LTCH                        |
    Then I validate the issue by id "3001-1:id" with data
      | snowids        | 3001-1[snowids        |
      | date_reported  | 3001-1:date_reported  |
      | status         | 3001-1:status         |
      | category       | 3001-1:category       |
      | description    | 3001-1:description    |
      | date_resolved  | null                  |
      | provider_types | 3001-1[provider_types |
    When I update the issue by id "3001-1:id" and save by name "3001-2" with data
      | date_reported | lastDayOfMonth::                          |
      | status        | Pending                                   |
      | category      | Patient Assessment by phycitian assistant |
      | description   | nitial tiryage                            |
    Then I validate the issue by id "3001-2:id" with data
      | snowids        | 3001-2[snowids        |
      | date_reported  | 3001-2:date_reported  |
      | status         | 3001-2:status         |
      | category       | 3001-2:category       |
      | description    | 3001-2:description    |
      | date_resolved  | null                  |
      | provider_types | 3001-2[provider_types |

  @3002
  Scenario: Database exercise
    Given I create new issue and save payload by name "3002-1" with data
      | snowids        | 123, 456, 789                         |
      | date_reported  | firstDayOfMonth::                     |
      | status         | Open                                  |
      | category       | Patient Assessment                    |
      | description    | Initial validation of main complaints |
      | date_resolved  | null::                                |
      | provider_types | HHA, IRF, LTCH                        |
    Then I validate the issue by id "3002-1:id" in database with data
      | snowids        | 3002-1[snowids        |
#      | date_reported  | 3002-1:date_reported  |
      | status         | 3002-1:status         |
      | category       | 3002-1:category       |
      | description    | 3002-1:description    |
      | date_resolved  | null                  |
      | provider_types | 3002-1[provider_types |
      