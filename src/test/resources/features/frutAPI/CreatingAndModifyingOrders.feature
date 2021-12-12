@all
Feature: Orders Create, Update, delete

  This feature includes scenarios creating and modifying orders

  @2001
  Scenario: Orders Create, Update, Delete
    Given I get all orders and save payload by name "2001-1"
    Given I created new customer by name "2001-customer" with data
      | firstName | firstName:: |
      | lastName  | lastName::  |
    When I create new order for customer by id "2001-customer:id" and save payload by name "2001-order"
    Then I validate order by orderId "2001-order:orderId" created with data
      | createdAt    | today::                    |
      | updatedAt    | today::                    |
      | customer_url | 2001-customer:customer_url |
      | actions      | null::                     |
      | items_url    | 2001-order:items_url       |
    Then I verify order by orderId "2001-order:orderId" created for customer by id "2001-customer:id"
    When I delete order by orderId "2001-order:orderId"
    Then I validate order by orderId "2001-order:orderId" does not exist
    Then I verify order by orderId "2001-order:orderId" does not exist for customer by id "2001-customer:id"
    When I delete a customer by id "2001-customer:id"

  @2002
  Scenario: Add and remove items to order
    Given I created new customer by name "2002-customer" with data
      | firstName | firstName:: |
      | lastName  | lastName::  |
    When I create new order for customer by id "2002-customer:id" and save payload by name "2002-order"