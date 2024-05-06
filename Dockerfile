FROM openjdk:8
COPY service-zt.jar /app.jar
CMD ["--server.port=8787"]
EXPOSE 8787
ENTRYPOINT ["java","-jar","/app.jar"]
