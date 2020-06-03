FROM postgres:latest

# replace shell with bash so we can source files
RUN rm /bin/sh && ln -s /bin/bash /bin/sh

# update the repository sources list
# and install dependencies
RUN apt-get update \
    && apt-get install -y curl python3.8 \
    && apt-get install -y git \
    && apt-get -y autoclean \

RUN apt-get update && apt-get install -y \
    python3-pip

RUN pip3 install --no-cache-dir psycopg2-binary flask flask-sqlalchemy

ENV POSTGRES_DB todoapp