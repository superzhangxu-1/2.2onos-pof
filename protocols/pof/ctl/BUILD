COMPILE_DEPS = CORE_DEPS + NETTY + JACKSON + [
    "@floodlightpof//jar",
	"@io_netty_netty//jar",
    "@io_netty_netty_codec//jar",
    "@io_netty_netty_handler//jar",
    "@io_netty_netty_transport//jar",
    "@io_netty_netty_transport_native_epoll//jar",
    "@io_netty_netty_transport_native_unix_common//jar",
    "//protocols/pof/api:onos-protocols-pof-api",
]

TEST_DEPS = TEST + [
    "//core/api:onos-api-tests",
]

osgi_jar_with_tests(
    test_deps = TEST_DEPS,
    deps = COMPILE_DEPS,
)
