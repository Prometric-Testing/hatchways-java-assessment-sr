SHELL:=/bin/bash
AWS_PROFILE:=$(ENV)

TERRAFORM_BACKEND_FILE:=$(CURDIR)/config/$(ENV)/backend.tfvars
TERRAFORM_BIN:=terraform

# Include other tasks
-include *.mk

terraform/init:
ifndef ENV
	@echo "ENV not defined, try :"
	@echo "	make ENV=qa-environment $(MAKECMDGOALS)"
	@echo
	@exit 1
endif
	@AWS_PROFILE=$(AWS_PROFILE) $(TERRAFORM_BIN) init -input=false -backend-config=$(TERRAFORM_BACKEND_FILE)

terraform/validate: terraform/configure
	@AWS_PROFILE=$(AWS_PROFILE) $(TERRAFORM_BIN) validate $(CURDIR)

terraform/configure:
ifndef ENV
	@echo "ENV not defined, try :"
	@echo "	make ENV=qa-environment $(MAKECMDGOALS)"
	@echo
	@exit 1
endif
ifeq (,$(wildcard $(CURDIR)/config/$(ENV)/config.yaml))
	@echo "Invalid environment $(ENV)"
	@echo
	@exit 1
endif
ifeq (,$(wildcard $(CURDIR)/.terraform/terraform.tfstate))
	@echo "Itialization required. Please run :"
	@echo "	make ENV=$(ENV) terraform/init"
	@echo
	@exit 1
endif
	@AWS_PROFILE=$(AWS_PROFILE) $(TERRAFORM_BIN) workspace list | grep $(ENV) >/dev/null || AWS_PROFILE=$(AWS_PROFILE) $(TERRAFORM_BIN) workspace new $(ENV)
	@AWS_PROFILE=$(AWS_PROFILE) $(TERRAFORM_BIN) workspace show | grep $(ENV) >/dev/null || AWS_PROFILE=$(AWS_PROFILE) $(TERRAFORM_BIN) workspace select $(ENV)

terraform/%: terraform/configure
	@AWS_PROFILE=$(AWS_PROFILE) $(TERRAFORM_BIN) $(notdir $@) -input=false $(TF_CMD_ARGS)
