#!/bin/groovy

def microservice = "logging-utility-service" 
def service_git_repo = "logging-utility-service" 
def test_git_repo = service_git_repo + "-tests"
def docker_repo = "loggingutilityservice"
def docker_name = "loggingutilityservice-microservice"
def docker_tag = "1.0.2"
def job_number ="9"
def dockerlogin = "mespocadmin"
def dockerpass = "MeS_PoC2019"
def git_user ="aswinradhakrishnan93"
def git_pass = "Redminote%404"
def slack_channel = '#jenkins'
def EMAIL_TO = 'kchen@merchante-solutions.com'

// type maybe 0=master; 1=develop; 2=release; 3=feature; 4=hotfix; 5=other. Drop to the floor if the branch is 5
def build_type = 5
def jdk_tool_name = "Java_1.8"
def maven_tool_name = "LocalMaven"
def use_mvn_global_settings_file_path = false
def maven_settings_file_id = "LocalMavenSettingsFile"
def sonar_sever_name = "LocalSonarQubeServer"
def sonar_scanner_name = "LocalSonarScanner"
def artifact_group_id = ''
def artifact_id = ''
def artifact_version = ''
def artifact_packaging = ''
def ssh_user = 'docker'
def docker_registry = 'localhost:5000'
def docker_container = ''
def docker_container_name_postfix = ''

node() { 
def workspace = pwd() 
}

pipeline {
	agent any
	stages {
		stage('Preparation') {
			steps {
				script {
					echo docker_registry.toUpperCase();
					def branch_indicator = GIT_BRANCH.minus("origin/").split("/");
					println("branch_indicator: " + branch_indicator);
					switch (branch_indicator.toUpperCase()) {
						case "MASTER":
							build_type = 0;
							break;
						case "DEVELOP":
							build_type = 1;
							break;
						case "RELEASE":
							build_type = 2;
							break;
						case "FEATURE":
							build_type = 3;
							break;
						case "HOTFIX":
							build_type = 4;
							break;
						default:
							build_type = 5;
							break;
					}
					artifact_group_id = readMavenPom().getGroupId()
					artifact_id = readMavenPom().getArtifactId()
					artifact_version = readMavenPom().getVersion()
					artifact_packaging = readMavenPom().getPackaging()
				}
			}
		}
		stage('Build') {
			when {
				expression { return build_type < 5 }
			}
			steps {
				script {
					echo 'Building artifacts and performing unit tests'
					if ( use_mvn_global_settings_file_path ) {
						withMaven(maven: maven_tool_name, jdk: jdk_tool_name, globalMavenSettingsFilePath: maven_settings_file_id) {
							sh 'mvn clean install'
						}
					}
					else {
						withMaven(maven: maven_tool_name, jdk: jdk_tool_name, mavenSettingsConfig: maven_settings_file_id) {
							sh 'mvn clean install'
						}
					}
				}
			}
			post {
				success {
					junit 'target/surefire-reports/**/*.xml'
				}
			}
		} 
		stage('Static Analysis & Code Coverage') {
			when {
				expression { return build_type < 5 }
			}
			steps {
				echo 'Working on Static Analysis & Code Coverage...'
				script {
					def scannerInstance = tool sonar_scanner_name
					// if parameter is not defined in the withSonarGubeEnv, the defualt sonar server is used
					withSonarQubeEnv(sonar_sever_name) {
						sh """${scannerInstance}/bin/sonar-scanner"""
					}
					sleep(20)
					timeout(time: sonar_qg_wait_time, unit: 'MINUTES') {
						// need to configure the webhook for the project first
						def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
						if (qg.status != 'OK') {
							error "Pipeline aborted due to quality gate failure: ${qg.status}"
						}
					}
				}
			}   
		}	
		stage('Component tests') {
			when {
				expression { return build_type < 5 }
			}
			steps {
				script {
					echo 'Retrieving corresponding test project'
					git credentialsId: git_credentials, url: 'bitbucket.org:merchante-solutions/${test_git_repo}.git'
					sh 'cd ./${test_git_repo}/ComponentTest/'
					echo 'Performing Component tests'
					if ( use_mvn_global_settings_file_path ) {
						withMaven(maven: maven_tool_name, jdk: jdk_tool_name, globalMavenSettingsFilePath: maven_settings_file_id) {
							sh 'mvn clean package -Dmaven.test.failure.ignore=true'
						}
					}
					else {
						withMaven(maven: maven_tool_name, jdk: jdk_tool_name, mavenSettingsConfig: maven_settings_file_id) {
							sh 'mvn clean package -Dmaven.test.failure.ignore=true'
						}
					}
				}
			} 
		}
		stage('Repository Tagging & Artifact Building & Artifact Publishing') {
			steps {        
				// sh """ 
				// 	docker build -t  "${docker_name}" .
				// 	docker login --username=${dockerlogin} --password=${dockerpass}	
				// 	docker tag ${docker_name}:latest ${dockerlogin}/${docker_repo}:${docker_tag}
				// 	docker push ${dockerlogin}/${docker_repo}:${docker_tag}
				// 	"""
				// sh 'mvn clean package dockerfile:build dockerfile:push'

				echo 'Buiding docker images and pushing it to a docker registry defined in pom.xml'
				script {
					if ( use_mvn_global_settings_file_path ) {
						withMaven(maven: maven_tool_name, jdk: jdk_tool_name, globalMavenSettingsFilePath: maven_settings_file_id) {
							sh 'mvn dockerfile:build dockerfile:push'
						}
					}
					else {
						withMaven(maven: maven_tool_name, jdk: jdk_tool_name, mavenSettingsConfig: maven_settings_file_id) {
							sh 'mvn dockerfile:build dockerfile:push'
						}
					}
				}
			}    
		}
		stage('Team Specific Environment Artifact Deployment') { 	 
			steps {
				sh """
					ssh -l root 18.188.142.122 rm -rf /home/ubuntu/Microservices/${microservice} 
					ssh -l root 18.188.142.122 mkdir /home/ubuntu/Microservices/${microservice}
					ssh -l root 18.188.142.122 git clone -b"Kubernetes" https://aswinradhakrishnan93:Redminote%404@bitbucket.org/merchante-solutions/${service_git_repo}.git  /home/ubuntu/Microservices/${microservice} 
					ssh -l root 18.188.142.122 sed  -i 's/tag/${docker_tag}/g' /home/ubuntu/Microservices/${microservice}/application.yaml
					ssh -l root 18.188.142.122 kubectl apply -f /home/ubuntu/Microservices/${microservice}/application.yaml 
					ssh -l root 18.188.142.122 kubectl apply -f /home/ubuntu/Microservices/${microservice}/service.yaml 
					"""
			}
		}
		stage('Sanity & Smoke Test Execution') {
			steps {
				sh """
					cd ${workspace}/${test_git_repo}/IntegrationTest/ 
					mvn clean package
					cd ${workspace}/${test_git_repo}/FunctionalTest/ 
					mvn clean package
					cd ${workspace}/${test_git_repo}/SerenityTest/
					mvn clean install
					curl --header "Content-Type: application/json" --request POST   --data '{}' http://18.188.203.173:8080/em/api/v2/jobs/${job_number}/histories?async=false
					"""
			}			
		}
		stage('Manual & Regression Tests Execution') {
			steps {
				sh 'echo "Test completed"'
			}   
		}		
	}
	post {
		always {
			// script {
			// 	allure([
			// 		includeProperties: false,
			// 		jdk: '',
			// 		properties: [],
			// 		reportBuildPolicy: 'ALWAYS',
			// 		results: [[path: "${workspace}/${test_git_repo}/IntegrationTest/target/allure-results"], [path: "${workspace}/${test_git_repo}/FunctionalTest/target/allure-results"], [path: "${workspace}/${test_git_repo}/ComponentTest/target/allure-results"]]
			// 	])
			// 	publishHTML(target: [
			// 		reportName : 'Serenity Report',
			// 		reportDir:   "${workspace}/${test_git_repo}/SerenityTest/target/site/serenity",
			// 		reportFiles: 'index.html',
			// 		keepAll:     true,
			// 		alwaysLinkToLastBuild: true,
			// 		allowMissing: false
			// 	])
			// }
			echo "This is always executed and will be used to clean up if needed"
		}
		success {
			emailext body: 'Check console output at $BUILD_URL to view the results. \n\n ${CHANGES} \n\n -------------------------------------------------- \n${BUILD_LOG, maxLines=100, escapeHtml=false}', 
			to: EMAIL_TO, 
			subject: 'Build successfull in Jenkins: $PROJECT_NAME - #$BUILD_NUMBER'
			//slackSend (channel: slack_channel, color: '#00FF00',message: "The pipeline ${currentBuild.fullDisplayName} completed successfully.")
		}
		failure {
			emailext body: 'Check console output at $BUILD_URL to view the results. \n\n ${CHANGES} \n\n -------------------------------------------------- \n${BUILD_LOG, maxLines=100, escapeHtml=false}', 
			to: EMAIL_TO, 
			subject: 'Build failed in Jenkins: $PROJECT_NAME - #$BUILD_NUMBER'
			//slackSend (channel: slack_channel, color: '#FF0000',message: "The pipeline ${currentBuild.fullDisplayName} has failed.")
		}
		unstable {
			emailext body: 'Check console output at $BUILD_URL to view the results. \n\n ${CHANGES} \n\n -------------------------------------------------- \n${BUILD_LOG, maxLines=100, escapeHtml=false}', 
			to: EMAIL_TO, 
			subject: 'Unstable build in Jenkins: $PROJECT_NAME - #$BUILD_NUMBER'
			//slackSend (channel: slack_channels, color: '#FFA500',message: "The pipeline ${currentBuild.fullDisplayName} is unstable.")
		}
		changed {
			emailext body: 'Check console output at $BUILD_URL to view the results.', 
			to: EMAIL_TO, 
			subject: 'Jenkins build is back to normal: $PROJECT_NAME - #$BUILD_NUMBER'
		}			
    }
}