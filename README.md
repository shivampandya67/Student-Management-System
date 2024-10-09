# Student-Management-System
# How to run this project in linux

-- First create a project named my-java-mysql-app
-- In that put the src folder, pom.yml, docker-compose.yml and Dockerfile
-- After that open terminal and run the command
  # mvn clean package (Considering you have installed the maven library to create the jar file to run the program)
-- This will create a target folder with all the necessary things to run the java code and through a jar file
-- After that run the command
  # sudo docker-compose up --build (this will create two container from the docker-compose and docker file)
-- One will have all the java configuration and other will be having mysql 8.0 configuration
-- after the above command is excuted completely you need to check if the containers are up and running.
-- So leave the current terminal as is and open a new terminal and write the command
  # sudo docker ps -a (this will show all the container which are exist in your docker out of which you will be seeing "my-java-mysql-app_java-app_1" and "my-java-mysql-app_mysql_1" containers)
-- after that as the project is up and running we can now run our mysql as well as the java project which we created.
-- to run the java container, execute this command
  # sudo docker exec -it my-java-mysql-app_java-app_1 /bin/sh
-- after running the above command this will be the time when you run your java code for that execute the below command
  # java -jar myJavaApp.jar
-- This will execute your java code and you will then see the output which is the project.
-- you can then interact with the program now.
-- after that open another tab and to access the sql container use the below command
  # sudo docker exec -it my-java-mysql-app_mysql_1 /bin/bash
-- this will then give you a bash console, in which you need to write the final command to access the mysql console
  # mysql -u root -p (now for this you will need a password which you will be able to find it in the compose file ;))
-- after you execute this you will be able to access the mysql console.
