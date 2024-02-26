# Terraform

This directory contains Terraform configurations to provision the AWS infrastructure.

## Environment configuration
The directory [config](config) contains configurations.

- `config.tfvars.json` contains environment specific configurations.
- `secrets.tfvars.json` contains environment specific secrets encrypted using AWS [KMS](https://aws.amazon.com/kms/) using [sops](https://github.com/mozilla/sops).

Use the variable `ENV` to run terraform against a specific environment. for instance :
```
make ENV=qa-environment terraform/plan
```

To connect to aws you need to configure a profile with the same name as the environment  :

```ini
# ~/.aws/credentials

[qa-environment]
aws_access_key_id = {AWS_ACCESS_KEY_ID}
aws_secret_access_key = {AWS_SECRET_ACCESS_KEY}
```

## Configure terraform
1. Terraform must first be installed on the host machine.
See [Installing Terraform](https://learn.hashicorp.com/terraform/getting-started/install.html) for more details


2. run the following commands to initialize Terraform:
```bash
# Install terraform plugins
make ENV=qa-environment terraform/configure

# Initialize the terraform working directory
make ENV=qa-environment terraform/init
```

## Verifying the Installation
To verify that the installation is successful run :
```bash
make ENV=qa-environment terraform/validate

# Success! The configuration is valid.
```

## Applying terraform configurations
1. Apply state:

```
make ENV=qa-environment terraform/apply
```
