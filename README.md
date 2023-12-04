# Flink Docker Images

This repository offers a set of Apache Flink Docker images maintained by LakeSail.
The LakeSail versions of the Flink Docker images are based on the official Flink Docker images and include additional features such as Python support.

Use the following command to pull the image from GitHub Container Registry.
Replace `$TAG` with the desired tag.
A list of available tags can be found in the [package homepage](https://github.com/orgs/lakehq/packages/container/package/flink).

```bash
docker pull ghcr.io/lakehq/flink:$TAG
```

## GitHub Container Registry Packages

* [lakehq/flink](https://github.com/orgs/lakehq/packages/container/package/flink)
* [lakehq/flink-build-cache](https://github.com/orgs/lakehq/packages/container/package/flink-build-cache) (This package contains Docker build cache and is not meant for consumption outside GitHub Actions.)

## Local Development

Use the following command(s) to build the images locally.

```bash
docker build . -f docker/flink1.17/Dockerfile -t lakehq/flink:1.17.1-python3.10
```

## License

Licensed under the Apache License, Version 2.0: https://www.apache.org/licenses/LICENSE-2.0

Apache Flink, Flink®, Apache®, the squirrel logo, and the Apache feather logo are either registered trademarks or trademarks of The Apache Software Foundation.
