Feature: Creating, Updating, deleting test data

  This feature will exercise creating, updating, deleting data.
  This functionality is fundamental for future tests dependent on test data

  @1001
  Scenario: Customer Create, Update, Delete
    Given I created new customer by name "1001-1" with data
      | firstname | firstname:: |
      | lastname  | lastname::  |
    Then I validate customer by id "1001-1.id" created
    When I update customer by name "1001-2" by id "1001-1.id" with data
      | firstname | firstname:: |
      | lastname  | lastname::  |
    Then I validate customer by id "1001-1.id" updated with data
      | firstname | 1001-2.firstname |
      | lastname  | 1001-2.lastname  |
    When I delete a customer by id "1001-1.id"
    Then I validate customer by id "1001-1.id" does not exist

  @1002
  Scenario: Vendors Create, Update, Delete
    Given I get all vendors and save payload by name "1002-1"
    When I create new vendor by name "1002-2" with data
      | name | vendorName:: |
    Then I validate vendor by id "1002-2.vendorId" created
    When I update vendor by name "1002-3" by id "1001-1.id" with data
      | name | vendorName:: |
    Then I validate vendor by id "1001-1.id" updated with data


  @1003
  Scenario: Products Create, Update, Delete
    Given I get all products and save payload by name "1003-1"
