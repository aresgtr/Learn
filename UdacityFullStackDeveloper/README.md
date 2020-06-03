## How to run everything in docker

### Create docker image

1. cd into this directory with Dockerfile
2. Create docker image
```
docker build --tag flaskpostgres .
```

### Make sure `flaskpostgres` has been created successfully

```
docker image ls
```

### Run docker image

```
docker run --name flaskpostgres -e POSTGRES_PASSWORD=password -p 5000:5000 -v "$PWD"/:/project/ -d flaskpostgres 
```

Check if the container is running

```
docker ps
```

### Run Bash inside the container

```
docker exec -it flaskpostgres bin/bash
```

### Find project directory
```
cd /project/
```
Then, locate the Flask app.py file

### Run Flask

```
FLASK_APP=app.py FLASK_DEBUG=true flask run -h 0.0.0.0
```
