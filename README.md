## About
This project is an automated test suite for the [GitHub Rest API](https://docs.github.com/en/rest?apiVersion=2022-11-28). It contains test cases for validating various CRUD functionalities related to repositories, issues and comments.

It is also possible to generate HTML reports detailing the test results. These reports are automatically uploaded to GitHub Pages: https://vanessahoamea.github.io/github-api-test/reports-serenity.

## Setup
To run the tests locally, you will need an `.env` file in the root directory, containing your [personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic):

```
ACCESS_TOKEN=<your_personal_access_token>
```

To get two-way integration with your Jira project (linking Jira issues in generated reports, linking reports in Jira cards through comments, updating issue statuses automatically), you will also need a `serenity.properties` file, also in the root directory:

```
jira.url=https://<your_server>.atlassian.net
jira.project=<your_project_key>
jira.username=<your_username>
jira.password=<your_api_token>
serenity.public.url=<serenity_reports_url>
serenity.jira.workflow.active=true
```

Be sure to modify the `@issue` annotations present in the `.feature` files to reflect your own Jira card identifiers. The status types listed in the `jira-workflow.groovy` file might also need to be modified, depending on how your board is set up.

To run the test suite and generate the corresponding HTML reports, simply run the command:
```
mvn clean verify
```

You can then find the reports at `target/site/serenity/index.html`.

## Postman
This repository also contains the exported Postman collection that served as a base for the Cucumber tests, found at `postman/GitHub-API-Collection.json`.

The tests in this collection can also be run locally using the Newman CLI tool.

```
newman run postman/GitHub-API-Collection.json \
    --env-var "baseURL=https://api.github.com" \
    --env-var "accessToken=<your_personal_access_token>" \
    --delay-request 1500 \
    --reporters cli,htmlextra \
    --reporter-htmlextra-export postman/newman/index.html
```

Running the command above will also generate its own HTML report. An example can be found on this project's GitHub Pages: https://vanessahoamea.github.io/github-api-test/reports-newman.

## Tech stack
- Java
- REST Assured
- Cucumber
- Serenity
- JUnit