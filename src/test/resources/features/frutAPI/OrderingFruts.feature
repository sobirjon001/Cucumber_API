Feature: Ordering fruits

  Bla bla

  @1001
  Scenario: New customer is able to purchase
    Given I created new customer by name "1001-1" with data
      | firstname | firstname:: |
      | lastname  | lastname::  |
    When I ask for name "1001-1.lastname"