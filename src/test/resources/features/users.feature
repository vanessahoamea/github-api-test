@GitHub @issue:GRA-4
Feature: GitHub REST API - User endpoints test
  For documentation, see: https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28

  @smoke @issue:GRA-5
  Scenario: Get currently authenticated user
    Given I have a valid access token
    When I fetch the current GitHub user
    Then the received status code is 200
    And the user response JSON schema matches the one expected