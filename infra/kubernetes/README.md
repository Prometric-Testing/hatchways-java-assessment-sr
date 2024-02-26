# Kubernetes

This directory contains Kubernetes manifests to provision the core infrastructure into AWS.

## Environment configuration
The directory [config](config) contains environment specific configurations.

- `secrets.enc.env` contains environment specific secrets encrypted using AWS [KMS](https://aws.amazon.com/kms/) using [sops](https://github.com/mozilla/sops).
- `secrets.enc.env` files are not used directly by kubernetes, before the manifest is applied secretes are decrypted into a `secrets.env` file that is included by kubernetes manifests.

Make sure you have sops installed.

```
brew install sops
```

You can use the variable `ENV` to run Kubernetes against a specific environment. for instance :
```
make ENV=local kubectl/dry-run
```

To connect to aws you need to configure a profile with the same name as the environment  :

```ini
# ~/.aws/credentials

[dev]
aws_access_key_id = {AWS_ACCESS_KEY_ID}
aws_secret_access_key = {AWS_SECRET_ACCESS_KEY}
```

## Configure terraform
1. kubectl must first be installed on the host machine.
See [Installing kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) for more details


2. run the following commands to initialize Terraform:
```bash
make ENV=local kubectl/configure
```

## Verifying the Installation
To verify that the installation is successful run :
```bash
make ENV=local kubectl/dry-run
```

## Applying Kubernetes manifests
1. Apply:

```
make ENV=local kubectl/apply
```
