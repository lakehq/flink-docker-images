ARG FLINK_VERSION=1.17.1
ARG MAVEN_VERSION=3.8.6

FROM maven:${MAVEN_VERSION}-amazoncorretto-8 AS tools

COPY pom.xml /app/pom.xml
COPY tools /app/tools

RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    cd /app && \
    mvn package && \
    mkdir /output && \
    cp tools/flink-fs-utils/target/*.jar /output

FROM flink:$FLINK_VERSION AS final

ARG FLINK_VERSION
ARG FLINK_SCALA_VERSION=2.12
ARG PYTHON_PYENV_VERSION=2.3.29
ARG PYTHON_VERSION=3.10.11

RUN set -eux; \
    savedAptMark="$(apt-mark showmanual)"; \
    apt-get update; \
    apt-get install -y --no-install-recommends \
        make \
        build-essential \
        libssl-dev \
        zlib1g-dev \
        libbz2-dev \
        libreadline-dev \
        libsqlite3-dev \
        wget \
        curl \
        llvm \
        libncursesw5-dev \
        xz-utils \
        tk-dev \
        libxml2-dev \
        libxmlsec1-dev \
        libffi-dev \
        liblzma-dev \
        # PyFlink depends on "pemja", which requires JDK when building from source.
        openjdk-11-jdk \
    ; \
    # Install Python using the tool from pyenv.
    cd /tmp; \
    wget -O - "https://github.com/pyenv/pyenv/archive/refs/tags/v${PYTHON_PYENV_VERSION}.tar.gz" | tar zxf -; \
    mv pyenv-* .pyenv; \
    /tmp/.pyenv/plugins/python-build/bin/python-build ${PYTHON_VERSION} /usr/local; \
    rm -rf /tmp/.pyenv; \
    # Install PyFlink.
    export PIP_NO_CACHE_DIR=1; \
    pip install --upgrade pip; \
    env JAVA_HOME=$(echo /usr/lib/jvm/java-11-openjdk-*) pip install apache-flink==${FLINK_VERSION}; \
    # We uninstall the Flink jars to reduce image size,
    # since the jar files are already present in the Flink installation.
    pip3 uninstall -y apache-flink-libraries==${FLINK_VERSION}; \
    # Clean up after Python installation.
    # See also: https://github.com/docker-library/python
    find /usr/local -depth \
        \( \
            \( -type d -a \( -name test -o -name tests -o -name idle_test \) \) \
            -o \( -type f -a \( -name '*.pyc' -o -name '*.pyo' -o -name 'libpython*.a' \) \) \
        \) -exec rm -rf '{}' + \
    ; \
    ldconfig; \
    apt-mark auto '.*' > /dev/null; \
    apt-mark manual $savedAptMark; \
    find /usr/local -type f -executable -not \( -name '*tkinter*' \) -exec ldd '{}' ';' \
        | awk '/=>/ { so = $(NF-1); if (index(so, "/usr/local/") == 1) { next }; gsub("^/(usr/)?", "", so); printf "*%s\n", so }' \
        | sort -u \
        | xargs -r dpkg-query --search \
        | cut -d: -f1 \
        | sort -u \
        | xargs -r apt-mark manual \
    ; \
    apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false; \
    apt-get clean -y; \
    rm -rf /var/lib/apt/lists/*; \
    python3 --version

# https://github.com/apache/hudi/issues/8265
RUN rm /opt/flink/lib/flink-table-planner-loader-${FLINK_VERSION}.jar && \
    cp /opt/flink/opt/flink-table-planner_${FLINK_SCALA_VERSION}-${FLINK_VERSION}.jar /opt/flink/lib/

COPY --from=tools /output/*.jar /opt/flink/opt/
COPY bin/flink-fs-cp-${FLINK_VERSION}.sh /opt/flink/bin/flink-fs-cp.sh
