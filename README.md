# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## Server Design Architecture
[[Sequence Diagram](Server Design)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUe4cOpO+51d42X9oQOuFIiO73DCTyltiaJ4movZYKBcLtn6pavIaSpLPU+wgheep-gBaHLJcCaUO2KYYPU4ROE4WYTChnwwOhwLLFh8Q4fWdH4Q26AcKYXi+AE0DsIyMQinAEbSHACgwAAMhAWSFCRzBOtQ-rNG0XS9AY6j5GgVGKrR9HfL8-y7Gx+xXIhgpwf6yHlnp7EgoZOx4aZmCWfCn6dvUCAyeKGLSbJBJEmApJAYYu40vuDJMlOulcje4UjhUy4wGKEpujKcplu8SqYCqwYagAQiGMAooSajMBi4ogNAKLgJlnJzvF76KVQSIwD2fbbqFzX+oVHABWVUYxnGgHVERH7yWmFEwMMMDZvyebzCZRYlvUejriVgUcU2b6Ls1rVvB6IUVCF9RVcWKDgE0+h-Ds06bvIawOQCpK5eqMC9cVKClTkMAVWgZ01cwT0cMFX4IV+9QAGYmgYnXg0ppa9f1OSDSgsZaYRUDjcgqbpgAjFROaqAtTnLdAq1FRtZVbVxIXwy1LowBYqSdRUO2jjAh5yCgz7xOel7XuziXCo+AYC1u7P061TEsSNnbtq59R+eKGSqDBLmPPBH4djULzUTZi36Zhv7-qx+lmaNbnwDjpEwORlGjPrWW2RhjEm7hJkEZx3HeH4-heCg6AxHEiQB0Hfm+Fg8mCoh9QNNIEaSRG7QRt0PQaaoWnDDLpty4mlSKz+2G5xrsJje5DPfl59iR75MmR8jQVw2FvIjpFYC8-zxcAQ1rfvpUSUpU+4vyLK8o5wBOWqhqzLTJe0BIAAXig61fYFcV97tFete1-Z09rseSnPeoL8vHCo+j8bmdrE0wPjhPzfmS3FuTCrH-Ep8rzTG8LgKB8Q0XK8W4jrlHZvUDgKBuDHkvF3IB8he6-3vMKGQkCmSGF5odMG2tC4R1PKrdWrl6ZWRgBbBGxEbZgDIlNGaIwSGNi4p4X2AQUTrn8NgcUGpJJohgAAcSVBoaO3VSwNB4cnNO9glTZ3dugTGFlNb+gnjIwhe1GbIByL5NEjdQbyzZrefk7dO6KLQAg-cSD6hDzFi+bQY9AGyynnlI+TFP6r2+lgSW-8PJtV7HvLBQjXTv2cRfYasib4UMmgTR2RMSbPxWm-JxUAl5fyuN7dxKjvxMUwTo0Bej6QwEZGAPhOZYGCxyX-AeyCLGFLUDYjJEtSlS0ZhInMXCcjNxAvI0sLSwD4IQLBDpRCkJjCaWoPCDRxjDIAJLSDwnjcIwRAhrGGEsTY8RdQoDdJyJyDExjJFAGqDZqFPYHCWcMgAcoc-SJCVguBuZ0Uh+drYlEoXbCiVFhmqFGeMpUUyZlzIWdNNAyzVmThoobOyyxdkgH2aCrZxzAVnIuexK5NyXB3PoT7Xi-gOAAHY3BOBQE4GIEZghwBEgANngBOQwVSYBFAoTHS2cdWgdHEZI9+ss3lKnOS7Ai18C4dPqEYzlcxuVgucso7ejMubogxHAKlWjm7ZMark-JhjpHGJ-qYpcFTxTDysaPDKRj7FvVnvExJLj16pMld+XebSK7+lNfPBJZ8gkY2vsmMJd8nARLmrmJ+6Eyalh8AE51ST0VWp1tLEewBFUyFKe3KpGIEVzBMXebV5jdV5IrDqPUWaRXZVehqGlOb9TJsMCaM0FprQ5FtPUjxld6hVLtTrf0gZK1RhtK6q+ltyFPPqAAVleZEx+YrA0MgrBW4M5oYBWhtN-CNJ0vEdX3kqzeHNpUoETWW1N-chT1FFsW3UpauXZQXQAyZ0hm2FzlUeFAPS+ll3hH42aQzvnTLYrM+Z9yxoVFvvbAFL6L2-K-eixhmLLCQK8psYOSAEhgAg32CA0GABSEBxS8IrP4SFao6VPIZQjJlzI1I9GGVI7u6AqLYAQMACDUA4AQC8lAPCAB1FgEyU49HypJBQcAADSKwlhAY-X879VtC5CsdlRmjlB6OMZY2xjjXGeP8cE2+4DgRoT9LSfUAAVmhtAGJUMqzXmVbRldKRgLyUyNV5GNXuPKRmiUGDrGGvVcameIbzWfVcZqtN2ml0+J0c+x1J9Q3n2jGjYJ7rKi33vsOv1o6X5Bs82feddb-O1PkLGyz+St1vp3YuBzyVM3DO1EevNKApnuYw3MMruay1GAgNUtAEBmCztaWezxtr97PukE11QpzWsdpRhFy+ecf0xc9YOh2vrib+sLEl8dtWOD9baq1md1awBpeVX-Y6ACm0rsjYzbIsnoBhgAkmt9j1qO0dO1AF6094AMegJWStTNwx50HP55mYgev2tLG2qdlhzuRlG1FntHq+1eszPFubYrJ3Vne7hZJTY-tHe-NDBAsMV2WZDBwJoN3MQxUWWgKTt3ntQDWBegrZS90BjXDAMnRFIAVbq6WpANA0Ccwp1egVMAjNoHvaXMCAyXh0Oi481MLyZt0O9mBv2XgaMwbg4r+UiBgywGANgKjhA8gFFpYI-7ccE5JxTmnYwIT+WPvqMLrW1rTrcDwBiMzg4qTxs5o7qAVTVDO987upK6u8CVgQBVjQOP3cgE91OGnZjrYa+D5lTJ5ndE7YdxrzuMf01x6DyaEPmWY1o+vZ7oXyjn20NE6EqH9saGy6bEAA)

<!-- Editable Version: (https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUe4cOpO+51d42X9oQOuFIiO73DCTyltiaJ4movZYKBcLtn6pavIaSpLPU+wgheep-gBaHLJcCaUO2KYYPU4ROE4WYTChnwwOhwLLFh8Q4fWdH4Q26AcKYXi+AE0DsIyMQinAEbSHACgwAAMhAWSFCRzBOtQ-rNG0XS9AY6j5GgVGKrR9HfL8-y7Gx+xXIhgpwf6yHlnp7EgoZOx4aZmCWfCn6dvUCAyeKGLSbJBJEmApJAYYu40vuDJMlOulcje4UjhUy4wGKEpujKcplu8SqYCqwYagAQiGMAooSajMBi4ogNAKLgJlnJzvF76KVQSIwD2fbbqFzX+oVHABWVUYxnGgHVERH7yWmFEwMMMDZvyebzCZRYlvUejriVgUcU2b6Ls1rVvB6IUVCF9RVcWKDgE0+h-Ds06bvIawOQCpK5eqMC9cVKClTkMAVWgZ01cwT0cMFX4IV+9QAGYmgYnXg0ppa9f1OSDSgsZaYRUDjcgqbpgAjFROaqAtTnLdAq1FRtZVbVxIXwy1LowBYqSdRUO2jjAh5yCgz7xOel7XuziXCo+AYC1u7P061TEsSNnbtq59R+eKGSqDBLmPPBH4djULzUTZi36Zhv7-qx+lmaNbnwDjpEwORlGjPrWW2RhjEm7hJkEZx3HeH4-heCg6AxHEiQB0Hfm+Fg8mCoh9QNNIEaSRG7QRt0PQaaoWnDDLpty4mlSKz+2G5xrsJje5DPfl59iR75MmR8jQVw2FvIjpFYC8-zxcAQ1rfvpUSUpU+4vyLK8o5wBOWqhqzLTJe0BIAAXig61fYFcV97tFete1-Z09rseSnPeoL8vHCo+j8bmdrE0wPjhPzfmS3FuTCrH-Ep8rzTG8LgKB8Q0XK8W4jrlHZvUDgKBuDHkvF3IB8he6-3vMKGQkCmSGF5odMG2tC4R1PKrdWrl6ZWRgBbBGxEbZgDIlNGaIwSGNi4p4X2AQUTrn8NgcUGpJJohgAAcSVBoaO3VSwNB4cnNO9glTZ3dugTGFlNb+gnjIwhe1GbIByL5NEjdQbyzZrefk7dO6KLQAg-cSD6hDzFi+bQY9AGyynnlI+TFP6r2+lgSW-8PJtV7HvLBQjXTv2cRfYasib4UMmgTR2RMSbPxWm-JxUAl5fyuN7dxKjvxMUwTo0Bej6QwEZGAPhOZYGCxyX-AeyCLGFLUDYjJEtSlS0ZhInMXCcjNxAvI0sLSwD4IQLBDpRCkJjCaWoPCDRxjDIAJLSDwnjcIwRAhrGGEsTY8RdQoDdJyJyDExjJFAGqDZqFPYHCWcMgAcoc-SJCVguBuZ0Uh+drYlEoXbCiVFhmqFGeMpUUyZlzIWdNNAyzVmThoobOyyxdkgH2aCrZxzAVnIuexK5NyXB3PoT7Xi-gOAAHY3BOBQE4GIEZghwBEgANngBOQwVSYBFAoTHS2cdWgdHEZI9+ss3lKnOS7Ai18C4dPqEYzlcxuVgucso7ejMubogxHAKlWjm7ZMark-JhjpHGJ-qYpcFTxTDysaPDKRj7FvVnvExJLj16pMld+XebSK7+lNfPBJZ8gkY2vsmMJd8nARLmrmJ+6Eyalh8AE51ST0VWp1tLEewBFUyFKe3KpGIEVzBMXebV5jdV5IrDqPUWaRXZVehqGlOb9TJsMCaM0FprQ5FtPUjxld6hVLtTrf0gZK1RhtK6q+ltyFPPqAAVleZEx+YrA0MgrBW4M5oYBWhtN-CNJ0vEdX3kqzeHNpUoETWW1N-chT1FFsW3UpauXZQXQAyZ0hm2FzlUeFAPS+ll3hH42aQzvnTLYrM+Z9yxoVFvvbAFL6L2-K-eixhmLLCQK8psYOSAEhgAg32CA0GABSEBxS8IrP4SFao6VPIZQjJlzI1I9GGVI7u6AqLYAQMACDUA4AQC8lAPCAB1FgEyU49HypJBQcAADSKwlhAY-X879VtC5CsdlRmjlB6OMZY2xjjXGeP8cE2+4DgRoT9LSfUAAVmhtAGJUMqzXmVbRldKRgLyUyNV5GNXuPKRmiUGDrGGvVcameIbzWfVcZqtN2ml0+J0c+x1J9Q3n2jGjYJ7rKi33vsOv1o6X5Bs82feddb-O1PkLGyz+St1vp3YuBzyVM3DO1EevNKApnuYw3MMruay1GAgNUtAEBmCztaWezxtr97PukE11QpzWsdpRhFy+ecf0xc9YOh2vrib+sLEl8dtWOD9baq1md1awBpeVX-Y6ACm0rsjYzbIsnoBhgAkmt9j1qO0dO1AF6094AMegJWStTNwx50HP55mYgev2tLG2qdlhzuRlG1FntHq+1eszPFubYrJ3Vne7hZJTY-tHe-NDBAsMV2WZDBwJoN3MQxUWWgKTt3ntQDWBegrZS90BjXDAMnRFIAVbq6WpANA0Ccwp1egVMAjNoHvaXMCAyXh0Oi481MLyZt0O9mBv2XgaMwbg4r+UiBgywGANgKjhA8gFFpYI-7ccE5JxTmnYwIT+WPvqMLrW1rTrcDwBiMzg4qTxs5o7qAVTVDO987upK6u8CVgQBVjQOP3cgE91OGnZjrYa+D5lTJ5ndE7YdxrzuMf01x6DyaEPmWY1o+vZ7oXyjn20NE6EqH9saGy6bEAA) -->
