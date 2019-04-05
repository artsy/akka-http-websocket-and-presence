FROM artsy/openjdk:corretto8-jre

ADD https://github.com/Yelp/dumb-init/releases/download/v1.2.2/dumb-init_1.2.2_amd64 /usr/local/bin/dumb-init
RUN chmod +x /usr/local/bin/dumb-init

RUN mkdir /app
WORKDIR /app
# Add package directory
ADD scala-times1 .

EXPOSE 8080

ENTRYPOINT ["/usr/local/bin/dumb-init", "--"]
CMD ["/app/bin/akka-http-websocket-and-presence"]
