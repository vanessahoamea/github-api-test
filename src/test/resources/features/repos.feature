@GitHub @issue:GRA-2
Feature: GitHub REST API - Repo endpoints test
  For documentation, see: https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28

  @smoke @issue:GRA-6
  Scenario: Create new repo
    Given I am authenticated as a GitHub user
    When I create a new repository for my user with the following details
      | name                     | description | homepage | private | has_issues | auto_init |
      | GitHub-REST-API-Practice | @random     | @random  | false   | true       | true      |
    Then the received status code is 201
    And the repo response JSON schema matches the one expected
    And the create repo response contains the correct data
    And the get repo endpoint response reflects the changes

  @regression @issue:GRA-7
  Scenario: Update repo
    Given I have successfully created a new repository
    When I update the newly created repository with the following details
      | description | private |
      | @random     | true    |
    Then the received status code is 200
    And the repo response JSON schema matches the one expected
    And the update repo response contains the correct data
    And the get repo endpoint response reflects the changes

  @regression @wait @issue:GRA-8
  Scenario: Delete repo
    Given I have successfully created a new repository
    When I delete the newly created repository
    Then the received status code is 204
    And the get repo endpoint returns 404 after the repo deletion