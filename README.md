# Develooping Spring Boot Application with Vaadin Framework

## Introduction

This document provides a quick reference guide for creating Spring Boot application using Vaadin as the UI framework.  Spring Boot documentation can be found at [spring.io](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) and Vaadin doucment is located [here](https://vaadin.com/docs/v10/index.html).  Couple good examples can be found at [Using Vaadin with Spring Boot](https://github.com/vaadin/flow-spring-examples/blob/master/pom.xml) and [Creating CRUD UI with Vaadin](https://spring.io/guides/gs/crud-with-vaadin/).

## Branching Strategy

[**Note**: Any branches needed to be merged into **master** or **develop** branch must be branched off from either ***master*** or ***develop*** branch as outlined below]

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

### Repository

The naming convention of a repository should follow the category of application function.  Currently, there are six (6) different categories,

* ***data*** - This category is used for RESTFul services that retrieve resources from different places, such as database, third party micro-services, files, and etc.  The repository for this type of applications will use *"soa-data"* prefix plus *"[application-name]"* as the naming scheme, such as *"soa-data-merchant-service"*.
* ***task*** - This category is used for micro-services that perform specific business logics.  It should not have any function to access resource directly.  The repository for this type of applications will have *"soa-task"* prefix plus *"[application-name]"* as the naming scheme, such as *"soa-task-netsuite-transform-service"*.
* ***util*** - This category is used for micro-services that perform common or infrastructure related services.  The repository for this type of applications will have *"soa-util"* prefix plus *"[application-name]"* as the naming scheme, such as *"soa-util-logging-service"* and *"soa-util-email-service"*.
* ***bpmn*** - This category is specified for modules that will be deployed into BPMN processing engine, such as a JavaDelegate used in a BPMN workflow.  The repository for this type of applications will use *"soa-bpmn"* prefix plus *"[application-name]"* as the naming scheme, such as *"soa-bpmn-clearing-summary-delegate"*.
* ***dmon*** - This category is used for micro-services that perform specific business logics as daemon or background services. The repository for this type of applications will have *"soa-dmon"* prefix and *"[application-name]"* as the naming scheme, such as *"soa-dmon-settlement-clearing-daemon"*.
* ***frwk*** - This category groups framework/common libraries that will be used by most of the applications and services outlined above. The repository for this type of applications will have *"soa-frwk"* prefix plus *"[application-name]"* as the naming scheme, such as *"soa-frwk-exception-handling"*, *"soa-frwk-request-validation"*, *"soa-frwk-caching", and etc.

Please note, the *"groupId"* and *"artifactId"* of the maven pom of a repository should have the corresponding values.  Use the above *"soa-frwk-request-validation"* as example, the *"groupId"* should be *"com.mes.soa.frwk" and the *"artifactId"* should be *"request-validation"*, and *"soa-data-merchant-service"* has "com.mes.soa.data" as the *"groupId"* and *"merchant-service"* as the *"artifactId"*.

### Java

For different aspects of Java naming conventions, we will refer to the original [Java Naming Conventions](https://www.oracle.com/technetwork/java/codeconventions-135099.html). However, all of the Java package should prefix with one of the following string depending on the naming convention of their corresponding repositories,

* ***com.mes.soa.data*** - This is for repository with *"soa-data"* prefix.
* ***com.mes.soa.task*** - This is for repository with *"soa-task"* prefix.
* ***com.mes.soa.util*** - This is for repository with *"soa-util"* prefix.
* ***com.mes.soa.bpmn*** - This is for repository with *"soa-bpmn"* prefix.
* ***com.mes.soa.dmon*** - This is for repository with *"soa-dmon"* prefix.
* ***com.mes.soa.frwk*** - This is for repository with *"soa-frwk"* prefix.

## Major Versions of Core Modules

* Java - [OpenJDK 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
* Spring Boot - [2.1.5.RELEASE](https://github.com/spring-projects/spring-boot/releases/tag/v2.1.5.RELEASE)
* Spring Frawework - [5.1.7.RELEASE](https://github.com/spring-projects/spring-framework/releases)
* Apache Camel - 2.24.1
* Apache Tomcat - [9.0.19](https://tomcat.apache.org/tomcat-9.0-doc/changelog.html)
* Flyway - [5.2.4](https://flywaydb.org/blog/flyway-5.2.3) (Java-based database tools for refactoring and versioning)
* Apache Ignite - [2.7.5](https://github.com/apache/ignite/blob/master/RELEASE_NOTES.txt) (In-memory data grid). For integrating with spring using ignite-spring-data_2.0, see [this](https://stackoverflow.com/questions/54463865/spring-boot-2-1-ignite-repository-2-7-0-workaround-for-spring-data-commons-is) to solve the problem
* Apache Maven - 3.6.1
* Comunda - [7.11.0](https://blog.camunda.com/post/2019/05/camunda-bpm-7110-released/) (Community Platform)
* PostGreSQL - [11.5](https://www.postgresql.org/support/versioning/)
* MongoDB - [4.2](https://docs.mongodb.com/manual/release-notes/4.2/)
* Jenkins - 2.176.2
* SonarQube - [7.9.x](https://www.sonarqube.org/downloads/)
* Artifactory - [6.12](https://www.jfrog.com/confluence/display/RTF/Release+Notes#ReleaseNotes-Artifactory6.12)
* Docker Registry - [2.7.2](https://hub.docker.com/_/registry)
* Kubernetes - [v1.15.3](https://kubernetes.io/docs/setup/release/notes/#kubernetes-v1-15-release-notes)
* Docker Engine - [19.03.1](https://docs.docker.com/engine/release-notes/)
* EFK Stack - [?]
* NGINX Plus - [R18](https://docs.nginx.com/nginx/releases/)
* Kafka - [Confluent Platform Community 5.3.0](https://docs.confluent.io/5.3.0/release-notes/index.html)
* OpenAPI - [3.0.0](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md)
* kops - [1.13.0](https://github.com/kubernetes/kops) (Kubernetes Operations)
