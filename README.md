# sample.
This repository contains the source code for sample.

__Table of Contents__
### [Infrastructure](infra)
*  [Kubernetes](infra/kubernetes)
*  [Terraform](infra/terraform)

### [Modules]()
* [sample-api](sample-api)
* [sample-worker](sample-worker)

# Getting Started

## Basics
Before continuing, please read through
- [Docker orientation and setup](https://docs.docker.com/get-started/)
- [Get started with Docker Compose](https://docs.docker.com/compose/gettingstarted/)
- [Kubernetes Basics](https://kubernetes.io/docs/tutorials/kubernetes-basics/)
- [Kustomize Overview](https://kustomize.io/#overview)
- [Troubleshooting Kubernetes](https://kubernetes.io/docs/tasks/debug-application-cluster/debug-application)

## Prerequisite
Required tools:
* [Docker](https://docs.docker.com/docker-for-mac/install)
* [Kubernetes](https://docs.docker.com/docker-for-mac/#kubernetes)
* [Maven](http://maven.apache.org/) (brew install maven)

## Setup
At this point you should have the following tools running :

```bash
# docker
docker --version
  # Docker version xx.xx.xx

# docker-compose
docker-compose --version
  # docker-compose version x.x.x

# kubernetes && kubectl
kubectl get nodes
  # NAME STATUS
  # docker-desktop Ready
```

### Clone the repo
```bash
git clone git@github.com:skafld/sample.git
```

### Compile the code
Navigate to the `sample` directory.

```bash
cd ~/path-to/sample/
```

Make sure you have all the docker images downloaded:
```bash
make ENV=local kubectl/docker-pull
```

Build local docker images and package the code:
```bash
make docker/build
```

### Run the kubernetes manifests
Now you are ready to apply the kubernetes manifests
Before you run any kubernetes manifest make sure you are using the correct kubernetes context.

```bash
kubectl config get-contexts
# CURRENT NAME
# * docker-desktop
#   remote-kube-env
```

To set the kubernetes context run.
```bash
kubectl config use-context docker-desktop
# Switched to context "docker-desktop".
```

Now Navigate to the `sample` directory.
```bash
cd ~/path-to/sample
```
Make sure you are logged in to the ecr docker registry :

```
aws ecr get-login-password --region us-east-1 --profile qa-environment | docker login --username AWS --password-stdin 652224034459.dkr.ecr.us-east-1.amazonaws.com
```

Run the following commands to make sure the manifests are correct.
```bash
# Inspect manifest files
ENV=local make kubectl/kustomize

# kubernetes dry-run
ENV=local make kubectl/dry-run
```

If everything is ok you can now apply the kubernetes manifests.
```bash
ENV=local make docker/build kubectl/apply
```

:warning:
If your docker starts to struggle you might need to increase resources :

https://docs.docker.com/docker-for-mac/#resources

After kubernetes is done deploying the new resources
Run the following command to check the running services.

```bash
# All pods running
kubectl get pods -A

    make ENV=local kubectl/logs/sample/api
    make ENV=local kubectl/logs/sample/worker
```

Now that everything is up and running you can access the kubernetes services.

Forward a local port to a port on the Pod :

```bash
# View the pod status
kubectl get pods -A

# Forward a local port to pod using the pod's namespace, name, and port
kubectl -n <NAMESPACE> port-forward <POD NAME> <LOCAL PORT>:<POD PORT>
# example: kubectl -n vendor port-forward redis-0 7000:6379
```
[Click here for more details on port forwarding with Kubernetes](https://kubernetes.io/docs/tasks/access-application-cluster/port-forward-access-application-cluster/)

To access the sample-api.

```bash
# Use port forwarding to access sample-api
make ENV=local kubectl/port-forward/sample/api

# Open swagger-ui in your browser
open http://127.0.0.1:8080/sample/sample-api/swagger-ui.html
```

```bash
# Use port forwarding to debug sample-api:8000
make ENV=local kubectl/debug/sample/api

# Attach IntelliJ to a remote process
[Tutorial](https://www.jetbrains.com/help/idea/attaching-to-local-process.html#attach-to-remote)
```

If you need to rebuild the image and restart pods run :
```bash
# Reload sample-api
make ENV=local docker/build kubectl/reload/sample/api

# Reload sample-worker
make ENV=local docker/build kubectl/reload/sample/worker
```

### List kafka topics

```
# list topics
kubectl -n kafka exec -i cp-kafka-client -- bash -c 'kafka-topics --bootstrap-server cp-kafka-broker-headless:9092 --list'

# consumer a topic from beginning
kubectl -n kafka exec -i cp-kafka-client -- bash -c 'kafka-console-consumer --bootstrap-server cp-kafka-broker-headless:9092 --topic sample.hello --from-beginning'

# consumer a topic from beginning and pipe to jq
kubectl -n kafka exec -i cp-kafka-client -- bash -c 'kafka-console-consumer --bootstrap-server cp-kafka-broker-headless:9092 --topic sample.hello --from-beginning' | jq .payload.after
```

### Kubernetes Developer cheat sheet
##### Dependencies
- Make sure you have kubernetes enabled https://docs.docker.com/docker-for-mac/#kubernetes
- Make sure you have kubectl https://kubernetes.io/docs/tasks/tools/install-kubectl/
- Make sure you have [sops](https://github.com/mozilla/sops) installed `brew install sops`
- Create the secrets files that are available in `~/path-to/sample/infra/kubernetes/local`
    - Example:
    - `cp infra/kubernetes/config/local/sample/sample-api/secrets.env.dist infra/kubernetes/config/local/sample/sample-api/secrets.env`
- Add the proper environment variables the secrets files that are available in `~/path-to/sample/infra/kubernetes/local`
    - - Example:
    - `vim infra/kubernetes/config/local/sample/sample-api/secrets.env `
- Switch the [context](https://docs.docker.com/engine/context/working-with-contexts/) of your docker for mac to "docker-desktop":
```kubectl config use-context docker-desktop```

##### Debug
```bash
make ENV=local kubectl/debug/sample/api
make DEBUG_PORT_FORWARD=8888:8000 ENV=local kubectl/debug/sample/worker

# Attach IntelliJ to a remote process
## Default port: 8000
[Tutorial](https://www.jetbrains.com/help/idea/attaching-to-local-process.html#attach-to-remote)
```

##### Deploy
- `cd ~/path-to/sample`
- Build the app `make docker/build`
- Deploy the app `make ENV=local kubectl/apply`
- Or all together `make docker/build kubectl/apply`

##### Check running instances
- All pods running `kubectl get pods -A`
- Verify the pods `make ENV=local kubectl/pods/sample`
- Verify the services `make ENV=local kubectl/services/sample`

##### Accessing the Pods and Services
- Port forward using kubectl `kubectl -n <NAMESPACE> port-forward <NAME> <PORT>`
- Forward the port to the localhost `make ENV=local kubectl/port-forward/sample/api`
- SSH into the api pod `make ENV=local kubectl/exec/sample/api`
- Check the api logs `make ENV=local kubectl/logs/sample/api`
- SSH/Bash into the worker pod `make ENV=local kubectl/exec/sample/worker`
- Check the worker logs `make ENV=local kubectl/logs/sample/worker`

##### Troubleshoot Kubernetes Applications

The first step in debugging a Pod is taking a look at it.

Check the current state of the Pod and recent events with the following command:

```
# Use the name and namespace
kubectl -n <NAMESPACE> describe pod <NAME>
```

Look at the state of the containers in the pod.
Are they all Running? Have there been recent restarts?

See [Troubleshooting kubernetes](https://kubernetes.io/docs/tasks/debug-application-cluster/debug-application) for more info


* Insufficient resources
    ```
    0/1 nodes are available: 1 Insufficient memory.
    ```

    You probably need to give kubernetes a bit more resources : https://docs.docker.com/docker-for-mac/#resources


* Failed to pull image
    ```
    Warning Failed      86s (x4 over 3m)   kubelet      Error: ErrImagePull
    ```

    Kubernetes was not able to pull the image, make sure you are logged in to the docker registry and have permissiong to download the image

    ```
    aws ecr get-login-password --region us-east-1 --profile qa-environment | docker login --username AWS --password-stdin 652224034459.dkr.ecr.us-east-1.amazonaws.com

    make kubectl/docker-pull
    ```


#### Run the application through Java Virtual Machine (IntelliJ)
- Top right of the toolbar in-between the green hammer and play button click the `Add Configuration` button
- Click the + icon at the top left of the pop out and select application
- Rename this configuration and select the triple dot ... beside main and select the Application file. should self find it
- Click apply and ok
- Edit the saved configuration, and add the required environment variables to it
- To start up the application click the green play icon, or the green debug icon to allow breakpoints in your code

