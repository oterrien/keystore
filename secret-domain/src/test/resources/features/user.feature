Feature: user feature

  Background: SetUp
    Given Root is created

  Scenario: a user should be able to create a secret
    Given following secrets:
      | Type  | Name        | Value       | Parent      |
      | Value | GMail       | @sol1dd;58b | Root        |
      | Group | Credentials |             | Root        |
    When I create each secret
    Then each secret is created
    And each secret belongs to parent's children
    Given following secrets:
      | Type  | Name        | Value       | Parent      |
      | Group | Facebook    |             | Credentials |
    When I create each secret
    Then each secret is created

