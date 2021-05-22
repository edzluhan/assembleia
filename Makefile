clean:
	./gradlew clean

build: clean
	./gradlew build

local-db-start:
	docker-compose up -d

run-local: local-db-start
	./gradlew bootRun