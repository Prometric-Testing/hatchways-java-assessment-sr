
provider "aws" {
  region  = lookup(local.configs, "region", "us-east-1")
}

terraform {
  backend "s3" {}
}

module "config" {
  source = "git@github.com:skafld/skafld-infrastructure.git//infra/terraform/modules/config?ref=v0.7.0"
}

locals {
  secrets      = module.config.secrets
  configs      = module.config.configs
  default_tags = module.config.default_tags
}

module "ecr" {
  source = "git@github.com:skafld/skafld-infrastructure.git//infra/terraform/modules/ecr?ref=v0.7.0"
  default_tags = local.default_tags
  repositories = lookup(local.configs, "ecr_repositories", [])
}

