Feature: create a secret

  As a user,
  I want to be able to create secrets

  Scenario: I should be able to create a secret
    Given I have declared the following secret:
      | field       | value                  |
      | name        | mySecret               |
      | description | my secret is beautiful |
      | value       | ght19kc;               |
    When I create this secret
    Then this secret is created with correct information