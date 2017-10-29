## BUILD & RUN

* RUN MAVEN BUILD - mvn clean install
* RUN DOCKER COMPOSE - docker-compose up --build
* GOTO http://localhost:8080 - admin:admin

## API DOCUMENTATION

* HTTP-GET: http://localhost:8080/tasks/all                         -> GET ALL TASKS <br>
* HTTP-GET: http://localhost:8080/tasks/status?status=FINISHED      -> GET TASK BY STATUS (WAITING, RUNNING, FINISHED)
* HTTP-GET: http://localhost:8080/tasks/1                           -> GET TASK BY ID
* HTTP-DELETE: http://localhost:8080/tasks/1                        -> DELETE TASK BY ID
* HTTP-POST:  http://localhost:8080/tasks/create                    -> CREATE TASK (Content-Type:application/json)
* HTTP-POST:  http://localhost:8080/tasks/update                    -> UPDATE TASK (Content-Type:application/json)

## DEVELOP DB IN DOCKER
* docker run -i -t -p 5432:5432  -e POSTGRES_USER=demo -e POSTGRES_PASSWORD=demo -e POSTGRES_DB=tasks-db postgres
* run application via ide (switch spring.datasource.url in application.properties)