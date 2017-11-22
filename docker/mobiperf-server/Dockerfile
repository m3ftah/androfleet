FROM python:3
MAINTAINER Lakhdar Meftah <lakhdar.meftah@inria.fr>

WORKDIR /

RUN easy_install web.py


COPY src /

#CMD ["tail","-f","/dev/null"]
EXPOSE 8080
entrypoint ["python","-u","server.py"]
