Feature: secret feature

  Background:
    Given I have created the group 'Root'

  Scenario: a secret value should be able to be created under Root
    Given I have declared the secret 'GMail' under group 'Root' with value '@sol1dd;58b'
    When I create the secret 'GMail'
    Then the secret 'GMail' is created
    And the secret 'GMail' is child of 'Root'

  Scenario: a secret group should be able to be created under Root
    Given I have declared the group 'Credentials' under group 'Root'
    When I create the secret 'Credentials'
    Then the secret 'Credentials' is created
    And the secret 'Credentials' is child of 'Root'

  Scenario: a secret value should be able to be created under another group
    Given I have created the group 'Credentials' under group 'Root'
    And I have declared the secret 'GMail' under group 'Credentials' with value '@sol1dd;58b'
    When I create the secret 'GMail'
    Then the secret 'GMail' is created
    And the secret 'GMail' is child of 'Credentials'

  Scenario: a secret group should be able to be created under another group
    Given I have created the group 'Credentials' under group 'Root'
    And I have declared the group 'GMail' under group 'Credentials'
    When I create the secret 'GMail'
    Then the secret 'GMail' is created
    And the secret 'GMail' is child of 'Credentials'

  Scenario: a secret value should be able to be moved from a group to another group
    Given I have created the group 'Credentials' under group 'Root'
    And I have created the secret 'GMail' under group 'Root' with value '@sol1dd;58b'
    When I move the secret 'GMail' to group 'Credentials'
    Then the secret 'GMail' is child of 'Credentials'
    But the secret 'GMail' is not child of 'Root'