SHELL:=/bin/bash
DOCKER_IMAGE_TAG:=latest
DOCKER_PUSH_CMD:=docker push
DOCKER_BUILD_CMD:=docker build
DOCKER_REPOSITORY_BASE:=docker.io/sample
MODULE_NAME:=$(notdir $(lastword $(subst :, ,$(MAKECMDGOALS))))
MAVEN_BIN:=mvn
MAVEN_OPTS:=-ntp
CODE_REPOSPTORY:=github.com/skafld/sample
OPENCONTAINERS:=
# Include other tasks
-include *.mk

# Include module hooks
ifneq (,$(wildcard $(CURDIR)/$(MODULE_NAME)/hooks.mk))
-include $(wildcard $(CURDIR)/$(MODULE_NAME)/hooks.mk)
endif

## Support building multi platform docker images
ifdef DOCKER_PLATFORM
DOCKER_PUSH_CMD:=docker buildx build
DOCKER_BUILD_CMD:=docker buildx build
DOCKER_BUILD_CMD_ARGS+= --platform=$(DOCKER_PLATFORM)
DOCKER_PUSH_CMD_ARGS+= $(CURDIR)/$(MODULE_NAME) --platform=$(DOCKER_PLATFORM) --build-arg DOCKER_IMAGE_TAG=$(DOCKER_IMAGE_TAG) --push -t
OPENCONTAINERS= --label org.opencontainers.image.created=$(shell date -u +"%Y-%m-%dT%H:%M:%SZ") --label org.opencontainers.image.revision=$(shell git rev-parse HEAD) --label org.opencontainers.image.source=$(CODE_REPOSPTORY) --label org.opencontainers.image.version=$(DOCKER_IMAGE_TAG)
endif

ifdef DOCKER_IMAGE_TAG
DOCKER_BUILD_CMD_ARGS+= --build-arg DOCKER_IMAGE_TAG=$(DOCKER_IMAGE_TAG) -t $(DOCKER_REPOSITORY_BASE)/$(notdir $@):$(DOCKER_IMAGE_TAG)
endif

maven/verify:
	$(MAVEN_BIN) $(MAVEN_OPTS) clean verify

maven/verify/%:
	$(MAVEN_BIN) $(MAVEN_OPTS) --projects $(notdir $@) verify

maven/build:
	$(MAVEN_BIN) $(MAVEN_OPTS) -DskipTests=true clean package

maven/build/%:
	$(MAVEN_BIN) $(MAVEN_OPTS) --projects $(notdir $@) -DskipTests=true clean package

docker/build:
	@$(foreach f, $(wildcard $(CURDIR)/*/Dockerfile), make docker/build/$(lastword $(subst /, ,$(dir $f)));)

docker/build/%: $(HOOK_PRE_DOCKER_BUILD)
ifndef DOCKER_IMAGE_TAG
	@echo "DOCKER_IMAGE_TAG not defined, try :"
	@echo "	make DOCKER_IMAGE_TAG=latest $(MAKECMDGOALS)"
	@echo
	@exit 1
endif
	$(DOCKER_BUILD_CMD) $(OPENCONTAINERS) $(CURDIR)/$(notdir $@) $(DOCKER_BUILD_CMD_ARGS)

docker/push:
	@$(foreach f, $(wildcard $(CURDIR)/*/Dockerfile), make docker/push/$(lastword $(subst /, ,$(dir $f)));)

docker/push/%: $(HOOK_PRE_DOCKER_PUSH)
ifndef DOCKER_IMAGE_TAG
	@echo "DOCKER_IMAGE_TAG not defined, try :"
	@echo "	make DOCKER_IMAGE_TAG=latest $(MAKECMDGOALS)"
	@echo
	@exit 1
endif
	$(DOCKER_PUSH_CMD) $(DOCKER_PUSH_CMD_ARGS) $(DOCKER_REPOSITORY_BASE)/$(notdir $@):$(DOCKER_IMAGE_TAG)

kubectl/%:
ifndef ENV
	@echo "ENV not defined, try :"
	@echo "	make ENV=local $(MAKECMDGOALS)"
	@echo
	@exit 1
endif
	@make ENV=$(ENV) -C $(CURDIR)/infra/kubernetes $@

terraform/%:
ifndef ENV
	@echo "ENV not defined, try :"
	@echo "	make ENV=qa-environment $(MAKECMDGOALS)"
	@echo
	@exit 1
endif
	@make ENV=$(ENV) -C $(CURDIR)/infra/terraform $@

