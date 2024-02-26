terraform {
  required_version = ">= 0.14"

  required_providers {
    sops = {
      source  = "carlpett/sops"
      version = "0.7.0"
    }
    aws = {
      source  = "hashicorp/aws"
      version = "4.11.0"
    }
  }
}
