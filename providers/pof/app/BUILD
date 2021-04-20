APPS = [
    "org.onosproject.hostprovider",
    "org.onosproject.lldpprovider",
    "org.onosproject.pof-base",
]

onos_app(
    app_name = "org.onosproject.pof",
    category = "Provider",
    description = "Suite of the pof base providers bundled together with ARP/NDP host " +
                  "location provider and LLDP link provider.",
    included_bundles = ["//utils/osgi:onlab-osgi"],  # FIXME to allow hollow oar files
    required_apps = APPS,
    title = "Pof Provider Suite",
    url = "http://onosproject.org",
)
