Feature: secret feature

  Background: Root is created
    Given I have created the group 'Root'

  Scenario: I should be able to create a secret value
    Given I have declared the secret 'GMail' with value '@sol1dd;58b'
    When I create the secret 'GMail'
    Then the secret 'GMail' is created
    And the secret 'GMail' is child of 'Root'

  Scenario: I should be able to create a secret group
    Given I have declared the group 'Credentials'
    When I create the secret 'Credentials'
    Then the secret 'Credentials' is created
    And the secret 'Credentials' is child of 'Root'

  Scenario: I should be able to create a secret value under a dedicated group
    Given I have created the group 'Credentials'
    And I have declared, under 'Credentials', the secret 'GMail' with value '@sol1dd;58b'
    When I create the secret 'GMail'
    Then the secret 'GMail' is created
    And the secret 'GMail' is child of 'Credentials'

  Scenario: I should be able to create a secret group under a dedicated group
    Given I have created the group 'Credentials'
    And I have declared, under 'Credentials', the group 'GMail'
    When I create the secret 'GMail'
    Then the secret 'GMail' is created
    And the group 'Credentials' should contain 'GMail'

  Scenario: I should be able to move a secret value from a group to another
    Given I have created the group 'Credentials'
    And I have created the secret 'GMail' with value '@sol1dd;58b'
    When I move the secret 'GMail' to group 'Credentials'
    Then the group 'Credentials' should contain 'GMail'
    But the group 'Root' should not contain 'GMail'

  Scenario: I should be able to move a secret group from a group to another
    Given I have created the secret 'GMail' with value '@sol1dd;58b'
    When I remove the secret 'GMail'
    Then the secret 'GMail' is removed
    And the group 'Root' should not contain 'GMail'

  Scenario: I should be able to remove a secret value
    Given I have created the group 'Credentials'
    When I remove the secret 'Credentials'
    Then the secret 'Credentials' is removed
    And the group 'Root' should not contain 'Credentials'

  Scenario: I should be able to remove a secret group
    Given I have created the group 'Credentials'
    When I remove the secret 'Credentials'
    Then the secret 'Credentials' is removed
    And the group 'Root' should not contain 'Credentials'