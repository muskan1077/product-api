# Kubernetes deployment

This folder contains the local learning setup for:
- the `product-api` Spring Boot application
- a MySQL database pod and service
- the config/secrets that connect them together

Apply the manifests:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/mysql-secret.yaml
kubectl apply -f k8s/mysql-pvc.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/mysql-service.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

Before applying the deployment, update the image in `k8s/deployment.yaml` to the image you pushed to your registry.

Useful commands:

```bash
kubectl get all -n product-api
kubectl logs deployment/product-api -n product-api
kubectl port-forward svc/product-api 8080:8080 -n product-api
```

This setup creates a MySQL service named `mysql`, so the app can use `jdbc:mysql://mysql:3306/productdb` from `k8s/configmap.yaml`.
