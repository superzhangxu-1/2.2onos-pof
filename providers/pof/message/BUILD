COMPILE_DEPS = CORE_DEPS + METRICS + [
    "@floodlightpof//jar",
    "//protocols/pof/api:onos-protocols-pof-api",
    "//apps/cpman/api:onos-apps-cpman-api",
]

BUNDLES = [
    "//apps/cpman/api:onos-apps-cpman-api",
    ":onos-providers-pof-message",
]

osgi_jar_with_tests(
    exclude_tests = [
        "org.onosproject.provider.pof.message.impl.OpenFlowControlMessageProviderTest",
    ],
    deps = COMPILE_DEPS,
)

onos_app(
    app_name = "org.onosproject.pof-message",
    category = "Provider",
    description = "ONOS POF control message provider.",
    included_bundles = BUNDLES,
    title = "Control Message Stats Provider",
    url = "http://onosproject.org",
)
