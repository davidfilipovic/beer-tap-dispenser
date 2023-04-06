.PHONY: down up test

down:
	docker compose down

up:
	docker compose run -p "8080:8080" beer-tap-dispenser gradle clean build bootRun -x test

test:
	docker compose run --rm --no-deps -p "8080:8080" beer-tap-dispenser gradle build test

coverage: test