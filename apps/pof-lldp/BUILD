COMPILE_DEPS = CORE_DEPS + JACKSON + [
    @floodlight//jar,
    "//protocols/pof/api:onos-protocols-pof-api",
]

osgi_jar_with_tests(
    deps = COMPILE_DEPS,
)

onos_app (
    category = "Utility",
	description = "Pof link layer discovery auxiliary application.",
	title = "Pof Link Layer Discovery Auxiliary App",
    url = "http://onosproject.org",
    
)
