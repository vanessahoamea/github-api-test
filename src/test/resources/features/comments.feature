@GitHub @issue:GRA-3
Feature: GitHub REST API - Comment endpoints test
  For documentation, see:
  - https://docs.github.com/en/rest/issues/comments?apiVersion=2022-11-28
  - https://docs.github.com/en/rest/reactions/reactions?apiVersion=2022-11-28

  @smoke @issue:GRA-12
  Scenario: Create new comment
    Given I am authenticated as a GitHub user
    And I have access to a repository with the following properties
      | name                     | description | homepage | private | has_issues | auto_init |
      | GitHub-REST-API-Practice | @random     | @random  | false   | true       | true      |
    And the repository has an issue with the following details
      | title   | body    | assignee | labels                |
      | @random | @random | @owner   | bug, good first issue |
    When I create a comment for the issue with the following body
      | body                              |
      | I'm also experiencing this issue. |
    Then the received status code is 201
    And the comment response JSON schema matches the one expected
    And the create comment response contains the correct data
    And the get comment endpoint response reflects the changes

  @regression @issue:GRA-13
  Scenario: Create new reaction to comment
    Given I have successfully created a new comment
    When I create a reaction to the comment with the following content
      | content |
      | eyes    |
    Then the received status code is 201
    And the reaction response JSON schema matches the one expected
    And the create reaction response contains the correct data
    And the get comment endpoint response reflects the updated reactions

  @regression @wait @final @issue:GRA-14
  Scenario: Delete comment
    Given I have successfully created a new comment
    When I delete the newly created comment
    Then the received status code is 204
    And the get comment endpoint returns 404 after the comment deletion