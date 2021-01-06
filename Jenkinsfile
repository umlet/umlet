pipeline {
  agent {
    docker {
      image 'maven:maven:3.6.3-openjdk-8'
      args '-v /root/.m2:/root/.m2'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'echo aaaa'
        sh 'java -version'
        sh 'mvn -v'
        sh 'mvn -B -DskipTests clean package'
      }
    }

  }
}