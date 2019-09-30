# Develooping Spring Boot Application with Vaadin Framework

## Introduction

This document provides a quick reference guide for creating Spring Boot application using Vaadin as the UI framework.  Spring Boot documentation can be found at [spring.io](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) and Vaadin doucment is located [here](https://vaadin.com/docs/v10/index.html).  Couple good examples can be found at [Using Vaadin with Spring Boot](https://github.com/vaadin/flow-spring-examples/blob/master/pom.xml) and [Creating CRUD UI with Vaadin](https://spring.io/guides/gs/crud-with-vaadin/).

## Branching Strategy

[**Note**: Any branches needed to be merged into ***master*** or ***develop*** branch must be branched off from either ***master*** or ***develop*** branch as outlined below]

We are using [GitFlow](https://datasift.github.io/gitflow/IntroducingGitFlow.html) as our branching strategy with the following conventions,

* ***master*** branch is the production branch and it is protected from any direct commits and only allow merging via pull request from *hotfix* and *release* branches
* ***develop*** branch is used to create new feature and release branches.  It is cloned from ***master*** branch originally.  It is also protected from any direct commits and only allow merge via pull request from *hotfix*, *release*, and *feature* branches.
* *feature* branches are branched from ***develop*** and is used to develop new features based on Jira story board.  Its naming convention should follow *"feature/[Jira-Task-numbers]"* naming scheme, i.e., *"feature/PS2-853/PS2-776"*. Pull request will need to be created thorugh Bitbucket so pre-assigned approvers will perform code review to sign it off before the *feature* branch can be merged into the ***develop*** branch.  *feature* branches will be removed after certain period, one month for now.
* *release* branches are branched off from ***develop*** branch also and is used to create release artifacts to be deployed to different environments.  Its naming convention should have *"release/[spirnt-number]"* naming scheme, i.e., *"release/SP6"*.  Pull request will need to be created thorugh Bitbucket so pre-assigned approvers will perform code review to sign it off before the *release* branch can be merged into the ***develop*** and ***master*** branches.  Every time a new merge to ***master*** branch, it will accompany with a new tag.  The *relase* branch will be deleted after two more new releases deployed to production environment successfully.
* *hotfix* branches are directly branched from ***master*** branch in case of the need for fixing production issues. Its naming convention should follow *"hotfix/[Jira-Defect-numbers]"* naming scheme, i.e., *"hotfix/DF-101/DF-201"*.  Pull request will need to be created thorugh Bitbucket so pre-assigned approvers will perform code review to sign off the pull request before the *hotfix* branch can be merged into the ***develop*** and ***master*** branches.  Each time a *hotfix* branch is merged to ***master*** branch, it will accompany with a new tag.  The *hotfix* branch will be deleted after two more new releases deployed to production environment successfully.

## Code Quality

Every project developed for MeS SOA initiative will use a Jenkins pipeline to invoke SonarQube scanner to inspect and analyze the code quality and security vulnerabilties for every pull request to ***develop*** branch.  We will use the default quality gate from SonarQube to prevent the merge of pull requests that fail the criterias defined in the quality gate.  We will tighten the criterias once the development group becomes more mature.  The detault quality gate in SonarQube is,

* Coverage on New Code must more than 80%;
* Duplicated Lines on New Code must be less than 3%;
* Maintainability Rating on New Code must be not less than "A";
* Reliability Rating on New Code must be not less than "A";
* Security Rating on New Code must be not less than "A".

## Naming Conventions

### Project

The naming convention of a project should have the follow the [Apache Maven Project](https://maven.apache.org/guides/mini/guide-naming-conventions.html) and we will use "-" to separate words if there are more than one word to describe the project name, such as *host-checkout* and etc.  Also, the repository name in Bitbucket should be same as the project name.

### Java

For different aspects of Java naming conventions, we will refer to the original [Java Naming Conventions](https://www.oracle.com/technetwork/java/codeconventions-135099.html). However, all of the Java package should prefix with "*com.mes*".

## Major Versions in Development

* Java - [OpenJDK 8](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
* Spring Boot - [2.1.6.RELEASE](https://github.com/spring-projects/spring-boot/releases/tag/v2.1.6.RELEASE)
* Spring Frawework - [5.1.8.RELEASE](https://github.com/spring-projects/spring-framework/releases)
* Vaadin Framework- [14.0.4](https://vaadin.com/releases/vaadin-14)
