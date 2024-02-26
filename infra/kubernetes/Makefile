SHELL:=/bin/bash
AWS_PROFILE:=$(ENV)
KUBECTL_CONTEXT:=$(ENV)
KUBECTL_BIN:=kubectl
KUBECTL_ROLLOUT_STATUS_TIMEOUT=600s
SOPS_BIN:=sops
PORT_FORWARD?=8080
DEBUG_PORT_FORWARD?=8000
K8S_NAMESPACE:=sample

ifeq ($(KUBECTL_CONTEXT),local)
KUBECTL_CONTEXT:=docker-desktop
endif

# Include other tasks
-include *.mk

kubectl/configure:
ifndef ENV
	@echo "ENV not defined, try :"
	@echo "	make ENV=local $(MAKECMDGOALS)"
	@echo
	@exit 1
endif
ifneq ($(shell $(KUBECTL_BIN) config current-context),$(KUBECTL_CONTEXT))
	@echo "Invalid kubectl context '$(shell $(KUBECTL_BIN) config current-context)'. try :"
	@echo "	kubectl config use-context $(KUBECTL_CONTEXT)"
	@echo
	@exit 1
endif
ifeq (,$(wildcard $(CURDIR)/config/$(ENV)/kustomization.yaml))
	@echo "Invalid environment $(ENV)"
	@echo
	@exit 1
endif
	@# dist files
	@$(foreach f, $(wildcard $(CURDIR)/config/$(ENV)/*/*/*.dist), test -f $(dir $f)/$(subst .dist,,$(notdir $f)) || cp $(f) $(dir $f)/$(subst .dist,,$(notdir $f));)
	@# sops secrets
	@$(foreach f, $(wildcard $(CURDIR)/config/$(ENV)/*/*/*.enc.*), AWS_PROFILE=$(AWS_PROFILE) $(SOPS_BIN) -d $(f) > $(dir $f)/$(subst .enc.,.,$(notdir $f));)

kubectl/docker-pull: kubectl/configure
	@$(KUBECTL_BIN) kustomize $(CURDIR)/config/$(ENV) $(K8S_CMD_ARGS) | grep 'image:' | sort | uniq | sed -e "s/image://" | xargs -I  {} docker pull {}

kubectl/apply: kubectl/configure
	$(KUBECTL_BIN) apply -k $(CURDIR)/config/$(ENV) $(K8S_CMD_ARGS)

kubectl/delete: kubectl/configure
	$(KUBECTL_BIN) delete -k $(CURDIR)/config/$(ENV) $(K8S_CMD_ARGS)

kubectl/kustomize: kubectl/configure
	$(KUBECTL_BIN) kustomize $(CURDIR)/config/$(ENV) $(K8S_CMD_ARGS)

kubectl/diff: kubectl/configure
	$(KUBECTL_BIN) diff -k $(CURDIR)/config/$(ENV) $(K8S_CMD_ARGS)

kubectl/dry-run: kubectl/configure
	$(KUBECTL_BIN) apply -k $(CURDIR)/config/$(ENV) --dry-run $(K8S_CMD_ARGS)

kubectl/rollout-status: kubectl/configure
	$(KUBECTL_BIN) -n $(K8S_NAMESPACE) rollout status deployment sample-api --timeout=$(KUBECTL_ROLLOUT_STATUS_TIMEOUT)
	$(KUBECTL_BIN) -n $(K8S_NAMESPACE) rollout status deployment sample-worker --timeout=$(KUBECTL_ROLLOUT_STATUS_TIMEOUT)

kubectl/pods/sample: kubectl/configure
	$(KUBECTL_BIN) -n $(K8S_NAMESPACE) get pods $(K8S_CMD_ARGS)

kubectl/services/sample: kubectl/configure
	$(KUBECTL_BIN) -n $(K8S_NAMESPACE) get services $(K8S_CMD_ARGS)

kubectl/logs/sample/%: kubectl/configure
	$(KUBECTL_BIN) -n $(K8S_NAMESPACE) logs -f -l app=sample-$(notdir $@) $(K8S_CMD_ARGS)

kubectl/reload/sample/%: kubectl/configure
	$(KUBECTL_BIN) -n $(K8S_NAMESPACE) delete pod -l app=sample-$(notdir $@) $(K8S_CMD_ARGS)

kubectl/port-forward/sample/%: kubectl/configure
	$(KUBECTL_BIN) -n $(K8S_NAMESPACE) port-forward service/sample-$(notdir $@) $(K8S_CMD_ARGS) $(PORT_FORWARD)

kubectl/debug/sample/%: kubectl/configure
	$(KUBECTL_BIN) -n $(K8S_NAMESPACE) port-forward service/sample-$(notdir $@) $(K8S_CMD_ARGS) $(DEBUG_PORT_FORWARD)

kubectl/exec/sample/%: kubectl/configure
	$(KUBECTL_BIN) -n $(K8S_NAMESPACE) exec -it service/sample-$(notdir $@) $(K8S_CMD_ARGS)  -it ash
