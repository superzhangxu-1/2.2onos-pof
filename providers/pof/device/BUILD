COMPILE_DEPS = CORE_DEPS + [
	"@floodlightpof//jar",
    "//protocols/pof/api:onos-protocols-pof-api",
    "//apps/optical-model:onos-apps-optical-model",
	"@io_netty_netty//jar",
]

TEST_DEPS = TEST_ADAPTERS

osgi_jar_with_tests(
    test_deps = TEST_ADAPTERS,
    deps = COMPILE_DEPS,
)
