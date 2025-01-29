PWD                     := $(shell pwd)
JAVA-TARGET-DIR         := $(PWD)/target

MAKE                    ?= make
NPM                     := npm
RM                      := rm -f -r
MAVEN                   := $(PWD)/mvnw

.PHONY: all
all:
	$(MAKE) clean
	$(MAKE) build
	$(MAKE) test

.PHONY: clean
clean:
	-$(MAVEN) compile
	$(RM) $(JAVA-TARGET-DIR)

.PHONY: build
build:
	$(MAVEN) compile
	cd $(PWD)/web && $(NPM) install && $(NPM) run build

.PHONY: test
test:
	$(MAVEN) test
