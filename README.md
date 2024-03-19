# Flink Docker Images

This repository offers a set of Apache Flink Docker images maintained by [LakeSail](https://lakesail.com/).
The LakeSail versions of the Flink Docker images are based on the official Flink Docker images and include additional features such as Python support.

Use the following command to pull the images from GitHub Container Registry.
Replace `$TAG` with the desired tag.
A list of available tags can be found in the [package homepage](https://github.com/orgs/lakehq/packages/container/package/flink).

```bash
docker pull ghcr.io/lakehq/flink:$TAG
```

More details about the images can be found in the [documentation](https://docs.lakesail.com/flink-docker-images/).

## GitHub Container Registry Packages

* [lakehq/flink](https://github.com/orgs/lakehq/packages/container/package/flink)
* [lakehq/flink-build-cache](https://github.com/orgs/lakehq/packages/container/package/flink-build-cache) (This package contains Docker build cache and is not meant for consumption outside GitHub Actions.)

## Local Development

Use the following command(s) to build the images locally.

```bash
docker build . -t lakehq/flink:1.17.2-python3.10
```

## References

### Apache Flink Releases

* [Apache Flink 1.17.0 Release Announcement](https://flink.apache.org/2023/03/23/announcing-the-release-of-apache-flink-1.17/)
* [Apache Flink 1.17.1 Release Announcement](https://flink.apache.org/2023/05/25/apache-flink-1.17.1-release-announcement/)
* [Apache Flink 1.17.2 Release Announcement](https://flink.apache.org/2023/11/29/apache-flink-1.17.2-release-announcement/)

### Python Versions

* [Status of Python Versions](https://devguide.python.org/versions/)
* [Python 3.10 Release Schedule](https://peps.python.org/pep-0619/)
* [Python 3.11 Release Schedule](https://peps.python.org/pep-0664/)
* [Python 3.12 Release Schedule](https://peps.python.org/pep-0693/)

## License

Licensed under the Apache License, Version 2.0: https://www.apache.org/licenses/LICENSE-2.0

Apache Flink, Flink®, Apache®, the squirrel logo, and the Apache feather logo are either registered trademarks or trademarks of The Apache Software Foundation.
