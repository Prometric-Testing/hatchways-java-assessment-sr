bucket               = "skafld-terraform-state-prod"
key                  = "sample/terraform.tfstate"
dynamodb_table       = "terraform_lock"
workspace_key_prefix = "terraform"
region               = "us-east-1"
encrypt              = true
