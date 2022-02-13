@fruitStore
Feature: Creating, Updating, deleting test data

  This feature will exercise creating, updating, deleting data.
  This functionality is fundamental for future tests dependent on test data

  @1001
  Scenario: Customer Create, Update, Delete
    Given I created new customer by name "1001-1" with data
      | firstName | firstName:: |
      | lastName  | lastName::  |
    Then I validate customer by id "1001-1:id" created
    When I update customer by id "1001-1:id" and save with name "1001-2" with data
      | firstName | firstName:: |
      | lastName  | lastName::  |
    Then I validate customer by id "1001-1:id" updated with data
      | firstName | 1001-2:firstName |
      | lastName  | 1001-2:lastName  |
    When I delete a customer by id "1001-1:id"
    Then I validate customer by id "1001-1:id" does not exist

  @1002
  Scenario: Vendors Create, Update, Delete
    Given I get all vendors and save payload by name "1002-1"
    When I create new vendor by name "1002-2" with data
      | name | vendorName:: |
    Then I validate vendor by vendorId "1002-2:vendorId" created
    When I update vendor by vendorId "1002-2:vendorId" and save with name "1002-3" with data
      | name | vendorName:: |
    Then I validate vendor by vendorId "1002-3:vendorId" updated with data
      | name | 1002-3:name |
    When I delete a vendor by vendorId "1002-2:vendorId"
    Then I validate vendor by vendorId "1002-2:vendorId" does not exist

  @1003
  Scenario: Validate categories data
    When I get all categories and save payload by name "1003-1-categories"
    Then I validate categories payload by name "1003-1-categories" has list
      | Fruits |
      | Dried  |
      | Fresh  |
      | Exotic |
      | Nuts   |
      | Fish   |

  @1004
  Scenario Outline: Validate products in each category
    When I get all products by category "<category>" and save payload by name "<name>"
    Then I validate products payload by name "<name>" contains coma separated list "<products>"
    Examples: Products list by given category
      | name   | category | products                                                   |
      | 1004-1 | Fruits   | Oranges, Pineapples, Cranberries, Raspberries     |
      | 1004-2 | Dried    | Dried Pineapples                                           |
      | 1004-3 | Fresh    | Mango fresh                                                |
      | 1004-4 | Exotic   | Dragon Fruit, Figs, Horn Cucumber, Kaki, Lychee, Rambutan  |
      | 1004-5 | Nuts     | Cashew Nuts, Hazelnuts, Peanuts, Pecan, Pistachio, Watnuts |
      | 1004-6 | Fish     | Sardines                                                   |

  @1005
  Scenario: Products Create, Update, Delete
    Given I get all products and save payload by name "1005-1"
    When I create new vendor by name "1005-2" with data
      | name | vendorName:: |
    When I create new product by name "1005-3" with data
      | name         | Test1                   |
      | price        | 3.55                    |
      | category_url | /shop/categories/Fruits |
      | vendor_url   | 1005-2:vendor_url       |
    Then I validate product by productId "1005-3:productId" created
    When I update product by productId "1005-3:productId" and save with name "1005-4" with data
      | name         | Test2                  |
      | price        | 2.55                   |
      | category_url | /shop/categories/Dried |
      | vendor_url   | 1005-2:vendor_url      |
    Then I validate product by productId "1005-4:productId" updated with data
      | name         | 1005-4:name         |
      | price        | 1005-4:price        |
      | category_url | 1005-4:category_url |
      | vendor_url   | 1005-4:vendor_url   |
    When I delete a product by productId "1005-4:productId"
    Then I validate product by productId "1005-4:productId" does not exist
    When I delete a vendor by vendorId "1005-2:vendorId"
    Then I validate vendor by vendorId "1005-2:vendorId" does not exist
