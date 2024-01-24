# Docker Cheat Sheet

## Docker Commands

Start Service
```bash
sudo service docker start
```

Download image
```bash
docker pull $image_name

# optional :xxx to specify version
```

Check existing images locally
```bash
docker images
```

Create container by running the image
```bash
docker run $image_name

# Run in detached mode
docker run -d $image_name

# Containers get random names, but you can name them
docker run --name $your_container_name $image_name
```

List running containers
```bash
docker ps

# In case you don't find the container id, list all running and stopped container
docker ps -a
```

Stop and restart a container
```bash
docker stop $container_id
docker start $container_id
```

Port binding
```bash
docker run -p$host_port:$container_port $image_name
```

Logs
```bash
docker logs $container_id
docker logs $container_name
```

Get the `terminal` inside a container
```bash
docker exec -it $container_id /bin/bash
docker exec -it $container_name /bin/bash

# To exit the terminal
exit
```

Forcefully Remove Containers and Images
```bash
# remove containers
docker rm -f $(docker ps -qa)
# remove images
docker rmi -f $(docker images -aq)
```