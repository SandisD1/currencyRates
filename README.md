# currencyRates

## Setup:

### Clone the project repository to your device. Click on the clone button on the top-right and copy the link presented. Enter the following command with the cloned link in your command line, navigated to the directory of your choice:

- git clone https://gitfront.io/r/user-3510169/XCwAq3238u5X/currencyRates.git

### Enter the following command into you command-line to create a docker image for the mariaDB database that is used in this project:

- docker create -p 3306:3306 --name currency-database -e MARIADB_USER=example-user -e MARIADB_PASSWORD=my_cool_secret -e
  MARIADB_ROOT_PASSWORD=my-secret-pw -e MARIADB_DATABASE=currencydb mariadb:latest

### Enter the following command into you command-line to start the mariaDB image in docker:

- docker start currency-database

> **Warning**
> Once this command is executed please wait for the database to initialize and become accessable, before
> starting the app or updating entries.

#### To Check if the database is initialized, run "docker logs currency-database"

#### The database is ready when the last 3 entries of the logs look like this:

- [Note]InnoDB: Buffer pool(s) load completed at (completion time)
- [Note] mariadbd: ready for connections.
- Version: '10.10.2-MariaDB-1:10.10.2+maria~ubu2204' socket: '/run/mysqld/mysqld.sock' port: 3306 mariadb.org binary
  distribution

> **Note**
> For the next command, navigate you command line to the cloned .../currencyRates/ directory on you computer, it must
> contain a Dockerfile.

### Enter the following command into you command-line to create a docker image that will be the basis for the containers you will use to run the app:

- docker build -t currency-javalin .


### Enter the following command into you command-line to create a docker container that updates the currency rate values in your mariadb database:

- docker create --name database-update currency-javalin update

### Enter the following command into you command-line to create a docker container for the web-application with endponts:

- docker create -p 7070:7070 --name javalin-app currency-javalin runApp

### Enter the following command into you command-line to start the docker container that updates the currency rate values in your mariadb database:

- docker start database-update

### Enter the following command into you command-line to start the docker container for the web-application with endponts:

- docker start javalin-app

## To access the application Endpoints:

- Enter http://localhost:7070/latest into your browser to see the latest rates for all currencies.
- Enter http://localhost:7070/AUD into your browser to see the stored history of rates for the specific currency
  specified by the three letter currency code at the end of URL.

### To stop your docker images from running:

- Enter "docker stop currency-database" to stop the database process.
- Enter "docker stop javalin-app" to stop the application process with endpoints.
- The update image is stopped after execution.
