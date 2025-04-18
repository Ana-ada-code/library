call mvn clean package
docker build -t library .
docker stop library || true
docker rm library || true
docker run -d -p 8080:8080 --name library library
