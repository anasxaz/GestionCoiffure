pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK21'
    }
    
    stages {
        stage('Clone') {
            steps {
                git branch: 'develop',
                    url: 'https://github.com/anasxaz/GestionCoiffure.git'
            }
        }
        
        stage('Compile') {
            steps {
                bat 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        
        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }
        
        stage('SonarQube') {
            steps {
                echo 'SonarQube - Étape 3'
            }
        }
        stage('Java Check') {
            steps {
                bat 'mvn -version'
            }
        }
    }
    
    post {
        success {
            archiveArtifacts artifacts: 'target/*.war', fingerprint: true
        }
    }
}