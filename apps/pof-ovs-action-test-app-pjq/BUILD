COMPILE_DEPS = CORE_DEPS + KRYO + CLI + [
    "//core/store/serializers:onos-core-serializers",
    "//protocols/pof/api:onos-protocols-pof-api",
    "@floodlightpof//jar",
]

TEST_DEPS = TEST_ADAPTERS

osgi_jar_with_tests(
    suppress_checkstyle = True,
    test_deps = TEST_DEPS,
    deps = COMPILE_DEPS,
)

onos_app(
    app_name = "org.onosproject.test.action",
    category = "Utilities",
    description = "Port Load Balance Service",
    title = "Port Load Balance Service",
    url = "http://onosproject.org",
)
