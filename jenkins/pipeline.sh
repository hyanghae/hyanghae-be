pipeline {
    agent any

    environment {
        awsServerUsername = 'ubuntu'               // Spring Boot 서버 사용자 이름
        awsBootServerIP = '3.38.9.145'                // Spring Boot 서버 IP 주소
        sshCredentialID = 'ssh'                           // SSH 자격 증명 ID
        githubCredentialID = 'github'                     // GitHub 자격 증명 ID
        githubRepoSSHUrl = 'git@github.com:liveinsoha/flask_spring.git'  // GitHub 리포지토리 SSH URL
        githubBranch = 'main'                              // Clone 받아올 브랜치 이름
    }

    stages {

        stage('Clone Repository and Build Gradle') {
            steps {
                echo 'Cloning Repository and Building Gradle'
                script {
                    // 깃 리포지토리 클론
                    git url: githubRepoSSHUrl, branch: githubBranch, credentialsId: githubCredentialID
                }
            }
            post {
                success {
                    echo 'Successfully Cloned Repository'
                }
                failure {
                    error 'Failed to Clone Repository'
                }
            }
        }

        stage('Prepare') {
            steps {
                echo 'Stopping Docker Container via SSH'
                script {
                    sshagent(credentials: [sshCredentialID]) {
                        // 모든 컨테이너를 중지합니다.
                        echo 'stop Docker Compose via SSH'
                        sh "ssh -o StrictHostKeyChecking=no ${awsServerUsername}@${awsBootServerIP} 'cd ~/backend && docker-compose down || true'"
                    }
                }
            }
            post {
                success {
                    echo 'Successfully stopped Docker Container'
                }
                failure {
                    error 'Failed to stop Docker Container'
                }
            }
        }

        stage('Delete Files on Server') {
            steps {
                echo 'Deleting files on server via SSH'
                script {
                    sshagent(credentials: [sshCredentialID]) {
                        // 서버에서 이전에 복사한 파일들을 삭제합니다.
                        sh "ssh -o StrictHostKeyChecking=no ${awsServerUsername}@${awsBootServerIP} 'sudo rm -rf ~/backend/*'"
                    }
                }
            }
            post {
                success {
                    echo 'Successfully deleted files on server'
                }
                failure {
                    error 'Failed to delete files on server'
                }
            }
        }

        stage('Copy Files to Server') {
            steps {
                echo 'Copying files to server via SSH'
                script {
                    sshagent(credentials: [sshCredentialID]) {
                        // 데모 및 flask-sqlAlchemy 디렉토리가 존재하는지 확인합니다.
                        sh "ls demo && ls flask-sqlAlchemy || exit 1"
                        // build 폴더를 재귀적으로 복사합니다.
                        sh "scp -o StrictHostKeyChecking=no -r demo ${awsServerUsername}@${awsBootServerIP}:~/backend/"
                        sh "scp -o StrictHostKeyChecking=no -r flask-sqlAlchemy ${awsServerUsername}@${awsBootServerIP}:~/backend/"
                        sh "scp -o StrictHostKeyChecking=no docker-compose.yml ${awsServerUsername}@${awsBootServerIP}:~/backend/"
                    }
                }
            }
            post {
                success {
                    echo 'Successfully copied files to server'
                }
                failure {
                    error 'Failed to copy files to server'
                }
            }
        }

        stage('Remove Previous Docker Image') {
            steps {
                echo 'Removing previous Docker image via SSH'
                script {
                    sshagent(credentials: [sshCredentialID]) {
                        // SSH를 통해 Spring Boot 서버에 접속하여 이전에 생성한 도커 이미지를 삭제합니다.
                        sh "ssh -o StrictHostKeyChecking=no ${awsServerUsername}@${awsBootServerIP} 'docker rmi flask_spring-spring || true'"
                        sh "ssh -o StrictHostKeyChecking=no ${awsServerUsername}@${awsBootServerIP} 'docker rmi flask_spring-flask || true'"
                    }
                }
            }
            post {
                success {
                    echo 'Successfully removed previous Docker image'
                }
                failure {
                    error 'Failed to remove previous Docker image'
                }
            }
        }

        stage('Run Docker Compose') {
            steps {
                echo 'Running Docker Compose via SSH'
                script {
                    sshagent(credentials: [sshCredentialID]) {
                         // SSH를 통해 Spring Boot 서버에 접속하여 ~/ 디렉토리로 이동한 후 Docker Compose를 실행합니다.
                        sh "ssh -o StrictHostKeyChecking=no ${awsServerUsername}@${awsBootServerIP} 'cd ~/backend && docker-compose up -d'"
                    }
                }
            }
            post {
                success {
                    echo 'Successfully ran Docker Compose'
                }
                failure {
                    error 'Failed to run Docker Compose'
                }
            }
        }
    }
}
