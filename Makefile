SHORTENER_IMAGE=url-shortener

build:
	docker build -t $(SHORTENER_IMAGE) .

run:
	docker run -p 8080:8080 --rm --name $(SHORTENER_IMAGE) \
	-e SPRING_PROFILES_ACTIVE=dev \
	$(SHORTENER_IMAGE)

up: build run

stop:
	docker stop $(SHORTENER_IMAGE)

test-local:
	mvn clean test