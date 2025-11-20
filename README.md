# DB Application

Maven can be installed from their [official website](https://maven.apache.org/install.html), then run this command to
install the dependencies:

```shell
mvn install
```

## Compiling

```shell
mvn compile
```

## Running

```shell
# replace {} with actual proper values
mvn exec:java -Dexec.args="{db_user} {db_password}"
```
