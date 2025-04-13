call mvn clean package
docker build -t library .
docker stop library
docker rm library
docker run -d -p 8080:8080 --name library library
