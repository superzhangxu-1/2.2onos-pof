BUNDLES = [
	"@floodlightpof//jar",
    "//protocols/pof/api:onos-protocols-pof-api",
    "//protocols/pof/ctl:onos-protocols-pof-ctl",
    "//providers/pof/device:onos-providers-pof-device",
	"//providers/pof/packet:onos-providers-pof-packet",
    "//providers/pof/flow:onos-providers-pof-flow",
    "//providers/pof/table:onos-providers-pof-table",
]

APPS = [
    "org.onosproject.optical-model",
]

onos_app(
    app_name = "org.onosproject.pof-base",
    category = "Provider",
    description = "Provides the base suite of device, flow and packet providers that rely on the " +
                  "Pof protocol to interact with network devices.",
    included_bundles = BUNDLES,
	required_apps = APPS,
    title = "Pof Base Provider",
    url = "http://onosproject.org",
)
