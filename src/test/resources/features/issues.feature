@GitHub @issue:GRA-1
Feature: GitHub REST API - Issue endpoints test
  For documentation, see: https://docs.github.com/en/rest/issues/issues?apiVersion=2022-11-28

  @smoke @issue:GRA-9
  Scenario: Create new issue
    Given I am authenticated as a GitHub user
    And I have access to a repository with the following properties
      | name                     | description | homepage | private | has_issues | auto_init |
      | GitHub-REST-API-Practice | @random     | @random  | false   | true       | true      |
    When I create a new issue for my repository with the following details
      | title   | body    | assignee | labels                |
      | @random | @random | @owner   | bug, good first issue |
    Then the received status code is 201
    And the issue response JSON schema matches the one expected
    And the create issue response contains the correct data
    And the get issue endpoint response reflects the changes

  @regression @issue:GRA-10
  Scenario: Update issue
    Given I have successfully created a new issue
    When I update the newly created issue with the following details
      | state  | state_reason |
      | closed | completed    |
    Then the received status code is 200
    And the issue response JSON schema matches the one expected
    And the update issue response contains the correct data
    And the get issue endpoint response reflects the changes

  @regression @final @issue:GRA-11
  Scenario: Lock issue
    Given I have successfully created a new issue
    When I lock the newly created issue with the following reason
      | lock_reason |
      | resolved    |
    Then the received status code is 204
    And the get issue endpoint response reflects the updated lock status