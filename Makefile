PWD                     := $(shell pwd)
JAVA-TARGET-DIR         := $(PWD)/target

MAKE                    ?= make
MAVEN                   := $(PWD)/mvnw
RM                      := rm -f -r

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

.PHONY: test
test:
	$(MAVEN) test
