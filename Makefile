clean:
	./gradlew clean

build: clean
	./gradlew build

local-db-start:
	docker-compose up -d

test: local-db-start
	./gradlew test

run-local: local-db-start
	./gradlew bootRun

debug: local-db-start
	./gradlew bootRun --debug-jvm