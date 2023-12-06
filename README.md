# cq-microshift-demo

## How to setup the microshift virtual machine ?

Proceed with initial installation step as below:
 + Create the Microshift virtual machine following instructions [there](https://github.com/openshift/microshift/blob/main/docs/user/getting_started.md)
 + Deploy AMQ Broker on Microshift following instructions [there](https://github.com/openshift/microshift/blob/main/docs/user/howto_amq_broker.md)

## How to build the demo project and push to quay.io ?

Clone this git project, build and push the image to quay.io:

```
git clone git@github.com:aldettinger/microshift-demo.git microshift-demo
cd microshift-demo
docker login registry.redhat.io
mvn package
docker build -f src/main/docker/Dockerfile.jvm -t quay.io/rhagallice/microshift-demo .
docker push quay.io/rhagallice/microshift-demo
```

## How to deploy the demo project to microshift

First, start the microshift virtual machine, e.g. `Virtual Manager/microshift-starter/Run`.
Then ssh into the microshift virtual machine:

```
USHIFT_IP=$(sudo virsh -q domifaddr microshift-starter | awk '{print $4}' | cut -d/ -f 1)
ssh redhat@${USHIFT_IP}
```

Finally, clean any previous deployment and update the cluster:

```
oc delete all -l app.kubernetes.io/name=camel-quarkus-examples-microshift
oc apply -f https://raw.githubusercontent.com/aldettinger/microshift-demo/main/src/main/kubernetes/microshift-demo.yml
```
