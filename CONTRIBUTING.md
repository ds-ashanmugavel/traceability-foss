# Contributing to Eclipse Tractus-X

Thanks for your interest in this project.

# Table of Contents
1. [Project description](#project_description)
2. [Project licenses](#project_licenses)
3. [Terms of Use](#terms_of_use)
4. [Developer resources](#developer_ressources)
5. [Problem Eclipse Development Process](#eclipse_commitment)
6. [Eclipse Contributor Agreement](#eclipse_agreement)
7. [General contribution to the project](#general)
8. [Contributing as a Consultant](#consultant)
9. [Contributing as a Developer](#developer)
10. [Contact](#contact)


## Project description

The companies involved want to increase the automotive industry's
competitiveness, improve efficiency through industry-specific cooperation and
accelerate company processes through standardization and access to information
and data. A special focus is also on SMEs, whose active participation is of
central importance for the network's success. That is why Catena-X has been
conceived from the outset as an open network with solutions ready for SMEs,
where these companies will be able to participate quickly and with little IT
infrastructure investment. Tractus-X is meant to be the PoC project of the
Catena-X alliance focusing on parts traceability.

- https://projects.eclipse.org/projects/automotive.tractusx
- https://github.com/eclipse-tractusx/traceability-foss

## Project licenses

The Tractus-X project uses the following licenses:

- [Apache-2.0 for code](LICENSE)
- [CC-BY-4.0 for non-code](LICENSE_non-code)

## Terms of Use

This repository is subject to the Terms of Use of the Eclipse Foundation

- https://www.eclipse.org/legal/termsofuse.php

## Developer resources

Information regarding source code management, builds, coding standards, and
more.

- https://projects.eclipse.org/projects/automotive.tractusx/developer

Getting started:

- https://eclipse-tractusx.github.io/docs/developer

The project maintains the source code repositories in the following GitHub organization:

- https://github.com/eclipse-tractusx/

## Eclipse Development Process

This Eclipse Foundation open project is governed by the Eclipse Foundation
Development Process and operates under the terms of the Eclipse IP Policy.

- https://eclipse.org/projects/dev_process
- https://www.eclipse.org/org/documents/Eclipse_IP_Policy.pdf

## Eclipse Contributor Agreement

In order to be able to contribute to Eclipse Foundation projects you must
electronically sign the Eclipse Contributor Agreement (ECA).

- http://www.eclipse.org/legal/ECA.php

The ECA provides the Eclipse Foundation with a permanent record that you agree
that each of your contributions will comply with the commitments documented in
the Developer Certificate of Origin (DCO). Having an ECA on file associated with
the email address matching the "Author" field of your contribution's Git commits
fulfills the DCO's requirement that you sign-off on your contributions.

For more information, please see the Eclipse Committer Handbook:
https://www.eclipse.org/projects/handbook/#resources-commit

## General contribution to the project

### Maintaining [CHANGELOG.md](CHANGELOG.md)
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


* Add release notes for new version in [CHANGELOG.md](CHANGELOG.md)
* Features of your product(s) or component(s), available for market entry
* Please provide minimum 5 to maximum 10 key feature descriptions
* Use category “ADDED” as per good practice
* Known knowns as of the baseline of our recent Gate4 reviews (for example these can be unresolved bugs (also medium ones) or SEC weaknesses etc.)
* Put yourself into the position of an “external” customer/user of your software and focus on what is worth mentioning (if anything)
* Create a category „KNOWN KNOWNS“ if applicable
* Make sure, your [CHANGELOG.md](CHANGELOG.md)  fully correlates to your market-entry version

_**For better traceability add the corresponding GitHub issue number in each changelog entry, please.**_

### git-hooks
Use git-hooks to ensure commit message consistency.
Detailed pattern can be found here: [commit-msg](dev/commit-msg#L4)

#### How to use

````
cp dev/commit-msg .git/hooks/commit-msg && chmod 500 .git/hooks/commit-msg
````
For further information, please see https://github.com/hazcod/semantic-commit-hook

**Good practices**

The commit messages have to match a pattern in the form of:

````
< type >(optional scope):[<Ticket_ID>] < description >
````

````
fix(api):[#123] Fix summary what is fixed.
chore(repos):[#123] Configuration change of ci cd pipeline for new repository.
docs(arc42):[#123] Added level 1 description for runtime view.
chore(helm):[#113] Moving the values under the global key - increasing the version
````

## Pull Request & Reviews

The goal is that the maximal life cycle of a pull request: 1.5 days.

**Steps:**

- Every developer creating a pull request is responsible to assign a reviewer.
- Add comment for pull request with required information.
- Please check the availability of a reviewer. The daily might be a good way to check the availability of team members.
- If Review needs to be planned: Assign corresponding Jira ticket to reviewer, with link to pull-request

### Dash IP
Prerequisites:
1) Create access token
   https://gitlab.eclipse.org/-/profile/personal_access_tokens

### Branching system and release workflow

We are using the [GitHub Flow](https://docs.github.com/en/get-started/quickstart/github-flow) for our branching system.

The general idea behind this approach is that you keep the main code in a constant deployable state.
You start off with the main branch, then a developer creates a feature branch directly from main.
After the feature is developed the code is reviewed and tested on the branch.
Only after the code is stable it can be merged to main.

- Main dev work is done on feature branches
  - Branches must be prefixed according to their nature:
  - feature/* - for implementing user stories
  - fix/* - for fixing bugs that appeared in the main branch
  - chore/* - any small change without any impact
- Branch Name:
  - MUST contain : Issue ID in the format #XXX
  - MUST contain: Subject of issue (Abbreviation of pbi summary without using spaces / use "-" to connect)

![github-flow-branching-model](docs/images/github-flow-branching-model.svg "github-flow-branching-model")

### Commit messages
- The commit messages have to match a pattern in the form of:
  `<type>(optional scope): <Ticket_ID> <description>`
- Allowed types are `chore`, `fix` and `feature`.

Examples:
- `feature(users): #DDD description`
- `fix: #322 make X work again`

The detailed pattern can be found here: [commit-msg](dev/commit-msg)

#### How to use
```shell
cp dev/commit-msg .git/hooks/commit-msg && chmod 500 .git/hooks/commit-msg
```

## Contributing as a Consultant

### Conceptual work and specification guidelines
1. The prerequisite for a concept is always a github issue that defines the business value and the acceptance criteria that are to be implemented with the concept
2. Copy and rename directory /docs/#000-concept-name-template /docs/#<DDD>-<target-name>
3. Copy file /docs/Concept_TEMPLATE.md into new directory  /docs/#<DDD>-<target-name>

## Contributing as a Developer (Developer Hints)

### Coding styles

To maintain coding styles we utilize [EditorConfig](https://editorconfig.org/) tool, see [configuration](.editorconfig)
file for the details.

### Static Code Analysis with SonarCloud

#### Overview
This project utilizes SonarCloud for static code analysis, which provides feedback on issues such as code smells, bugs, and security vulnerabilities.

#### How to Use (IntelliJ Example)
To connect your remote SonarCloud instance with your IntelliJ IDE, follow these steps:

1. Install the SonarLint plugin for IntelliJ:
    * Open IntelliJ and go to File > Settings (or IntelliJ IDEA > Preferences on macOS)
    * Select Plugins and search for "SonarLint"
    * Click on the "Install" button and follow the prompts to complete the installation

2. Configure SonarLint with your SonarCloud account:
    * Go to File > Settings (or IntelliJ IDEA > Preferences on macOS)
    * Select Other Settings > SonarLint
    * Click on the "+ Add" button and select "SonarCloud"
    * Add your project key
    * Click on "Create Token" and log in
    * Copy and paste the token
    * Click on the "Test Connection" button to verify the connection

3. Analyze your code with SonarLint:
    * Open your project in IntelliJ IDEA
    * Right-click on your project folder in the Project Explorer and select "SonarLint > Analyze <project_name>"

By following these steps, you can connect your remote SonarCloud instance with your IntelliJ IDE and analyze your code with SonarLint.

### Frontend coding guidelines
These guidelines are defined to maintain homogeneous code quality and style. It can be adapted as the need arises.

New and old developers should regularly review this [guide](frontend/GUIDELINES.md) to update it as new points emerge and to sync themselves with the latest changes.

#### Angular Template Attribute Convention

Attributes in Angular template should be properly ordered by groups:

1. `*` - Structural Directives
2. `[]` - Attribute Directives or Input parameters
3. `()` - Event listeners
4. All other attributes

### IDE plugins

* IntelliJ IDEA ships with built-in support for .editorconfig files
* [Eclipse plugin](https://github.com/ncjones/editorconfig-eclipse#readme)
* [Visual studio code plugin](https://marketplace.visualstudio.com/items?itemName=EditorConfig.EditorConfig)

#### Backend
##### Generate Dependencies

`mvn package org.eclipse.dash:license-tool-plugin:license-check -DskipTests=true -Ddash.summary=DEPENDENCIES_BACKEND -Ddash.fail=true`

##### Request Review

`mvn org.eclipse.dash:license-tool-plugin:license-check -Ddash.iplab.token=<token> -Ddash.projectId=automotive.tractusx`

#### Frontend
##### Generate Dependencies
`cd frontend`
`yarn install`
`yarn run dependencies:generate`

##### Request Review

`java -jar scripts/download/org.eclipse.dash.licenses-0.0.1-SNAPSHOT.jar yarn.lock -review -token <token> -project automotive.tractusx`

## Contact

Contact the Eclipse Tractus-X developers via the developer mailing list.

* https://accounts.eclipse.org/mailing-list/tractusx-dev

Contact the project developers via eclipse matrix chat.

* Eclipse Matrix Chat https://chat.eclipse.org/#/room/#tractusx-trace-x:matrix.eclipse.org
