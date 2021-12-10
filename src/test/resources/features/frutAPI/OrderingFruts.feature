Feature: Ordering fruits

  Bla bla

  @1001
  Scenario: Customer Create, Update, delete
    Given I created new customer by name "1001-1" with data
      | firstname | firstname:: |
      | lastname  | lastname::  |
    Then I validate consumer id created by id "1001-1.id"
    When I update customer by name "1001-1a" by id "1001-1.id" with data
      | firstname | firstname:: |
      | lastname  | lastname::  |
    Then I validate consumer id created by id "1001-1.id" with data
      | firstname | 1001-1a.firstname |
      | lastname  | 1001-1a.lastname  |