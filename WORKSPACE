workspace(name = "org_onosproject_onos")

load("//tools/build/bazel:bazel_version.bzl", "check_bazel_version")

check_bazel_version()

load("//tools/build/bazel:local_jar.bzl", "local_atomix", "local_jar", "local_yang_tools")

# Use this to build against locally built arbitrary 3rd party artifacts
#local_jar(
#    name = "atomix",
#    path = "/Users/tom/atomix/core/target/atomix-3.0.8-SNAPSHOT.jar",
#)

#local_jar(
#    name = "floodlightpof",
#    path = "/home/zhangxu/2.2onos/onos/floodlightpof-1.0.3.onos.jar",
#)

#maven_jar(
#    name = "floodlightpof",
#    artifact = "com.github.ustc-infinitelab:floodlightpof:1.0.3.onos",
#    sha1 = "7461817e5bc7318d9fdfb416354204524f38e6bc",
#)

#maven_jar (
#    name = "org_apache_felix_scr_annotations",
#    artifact = "org.apache.felix:org_apache_felix_scr_annotations:jar:1.9.12",
#    sha1 = "5fdc34da641dda8b9165c2be93211479a186da9c",
#)

# Use this to build against locally built Atomix
#local_atomix(
#    path = "/Users/tom/atomix",
#    version = "3.0.8-SNAPSHOT",
#)

# Use this to build against locally built YANG tools
#local_yang_tools(
#    path = "/Users/andrea/onos-yang-tools",
#    version = "2.6-SNAPSHOT",
#)

load("//tools/build/bazel:generate_workspace.bzl", "generated_maven_jars")

generated_maven_jars()

load("//tools/build/bazel:protobuf_workspace.bzl", "generate_protobuf")

generate_protobuf()

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

load("//tools/build/bazel:grpc_workspace.bzl", "generate_grpc")

generate_grpc()

load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")

# We omit as many dependencies as we can and instead import the same via
# deps.json, so they get wrapped properly for Karaf runtime.
grpc_java_repositories(
    omit_bazel_skylib = False,
    omit_com_google_android_annotations = True,
    omit_com_google_api_grpc_google_common_protos = True,
    omit_com_google_auth_google_auth_library_credentials = True,
    omit_com_google_auth_google_auth_library_oauth2_http = True,
    omit_com_google_code_findbugs_jsr305 = True,
    omit_com_google_code_gson = True,
    omit_com_google_errorprone_error_prone_annotations = True,
    omit_com_google_guava = True,
    omit_com_google_guava_failureaccess = True,
    omit_com_google_j2objc_j2objc_annotations = True,
    omit_com_google_protobuf = True,
    omit_com_google_protobuf_javalite = True,
    omit_com_google_truth_truth = True,
    omit_com_squareup_okhttp = True,
    omit_com_squareup_okio = True,
    omit_io_grpc_grpc_proto = True,
    omit_io_netty_buffer = True,
    omit_io_netty_codec = True,
    omit_io_netty_codec_http = True,
    omit_io_netty_codec_http2 = True,
    omit_io_netty_codec_socks = True,
    omit_io_netty_common = True,
    omit_io_netty_handler = True,
    omit_io_netty_handler_proxy = True,
    omit_io_netty_resolver = True,
    omit_io_netty_tcnative_boringssl_static = True,
    omit_io_netty_transport = True,
    omit_io_opencensus_api = True,
    omit_io_opencensus_grpc_metrics = True,
    omit_io_perfmark = True,
    omit_javax_annotation = True,
    omit_junit_junit = True,
    omit_net_zlib = True,
    omit_org_apache_commons_lang3 = True,
    omit_org_codehaus_mojo_animal_sniffer_annotations = True,
)

load("//tools/build/bazel:p4lang_workspace.bzl", "generate_p4lang")

generate_p4lang()

load("//tools/build/bazel:gnmi_workspace.bzl", "generate_gnmi")

generate_gnmi()

load("//tools/build/bazel:gnoi_workspace.bzl", "generate_gnoi")

generate_gnoi()

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "build_bazel_rules_nodejs",
    commit = "70406e05de721520ca568a17186de73e972d7651",
    remote = "https://github.com/bazelbuild/rules_nodejs.git",
    shallow_since = "1551145517 -0800",
)

load("@build_bazel_rules_nodejs//:defs.bzl", "node_repositories")

node_repositories(
    node_version = "8.11.1",
    package_json = ["//tools/gui:package.json"],
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# buildifier is written in Go and hence needs rules_go to be built.
# See https://github.com/bazelbuild/rules_go for the up to date setup instructions.
http_archive(
    name = "io_bazel_rules_go",
    sha256 = "9fb16af4d4836c8222142e54c9efa0bb5fc562ffc893ce2abeac3e25daead144",
    urls = [
        "https://storage.googleapis.com/bazel-mirror/github.com/bazelbuild/rules_go/releases/download/0.19.0/rules_go-0.19.0.tar.gz",
        "https://github.com/bazelbuild/rules_go/releases/download/0.19.0/rules_go-0.19.0.tar.gz",
    ],
)

load("@io_bazel_rules_go//go:deps.bzl", "go_register_toolchains", "go_rules_dependencies")

go_rules_dependencies()

go_register_toolchains()

http_archive(
    name = "bazel_gazelle",
    sha256 = "be9296bfd64882e3c08e3283c58fcb461fa6dd3c171764fcc4cf322f60615a9b",
    urls = [
        "https://storage.googleapis.com/bazel-mirror/github.com/bazelbuild/bazel-gazelle/releases/download/0.18.1/bazel-gazelle-0.18.1.tar.gz",
        "https://github.com/bazelbuild/bazel-gazelle/releases/download/0.18.1/bazel-gazelle-0.18.1.tar.gz",
    ],
)

load("@bazel_gazelle//:deps.bzl", "gazelle_dependencies")

gazelle_dependencies()

http_archive(
    name = "com_github_bazelbuild_buildtools",
    sha256 = "05eb52437fb250c7591dd6cbcfd1f9b5b61d85d6b20f04b041e0830dd1ab39b3",
    strip_prefix = "buildtools-0.29.0",
    url = "https://github.com/bazelbuild/buildtools/archive/0.29.0.zip",
)
