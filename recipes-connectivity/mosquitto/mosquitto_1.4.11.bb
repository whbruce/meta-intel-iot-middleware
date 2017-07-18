SUMMARY = "Open source MQTT v3.1 implemention"
DESCRIPTION = "Mosquitto is an open source (BSD licensed) message broker that implements the MQ Telemetry Transport protocol version 3.1. MQTT provides a lightweight method of carrying out messaging using a publish/subscribe model. "
HOMEPAGE = "http://mosquitto.org/"
SECTION = "console/network"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=62ddc846179e908dc0c8efec4a42ef20"

DEPENDS = "openssl util-linux c-ares libwebsockets"
RDEPENDS_${PN} = "c-ares libwebsockets"

PR = "r0"

inherit autotools-brokensep systemd useradd update-rc.d 

SYSTEMD_SERVICE_${PN} = "mosquitto.service"
INITSCRIPT_NAME = "mosquitto"
INITSCRIPT_PARAMS = "defaults"

CFLAGS += "-L${STAGING_LIBDIR}"

EXTRA_OEMAKE += 'STRIP="echo" prefix="/usr"'

SRC_URI = "http://mosquitto.org/files/source/mosquitto-${PV}.tar.gz \
           file://mosquitto.conf \
           file://mosquitto.service \
      	   file://mosquitto.init.d \
"

FILES_${PN} += "${libdir}/python2.*/*"

export LIB_SUFFIX="${@d.getVar('baselib', True).replace('lib', '')}"

do_compile_prepend() {
   cd ${S}; sed -i "s/WITH_WEBSOCKETS:=no/WITH_WEBSOCKETS:=yes/g" config.mk
   cd ${S}; sed -i "s/WITH_EC:=yes/WITH_EC:=no/g" config.mk
}

do_install_append() {
    install -d -m 0755 ${D}${sysconfdir}/init.d
    install -d -m 0755 ${D}${sysconfdir}/mosquitto/

    install -m 0755 ${WORKDIR}/mosquitto.conf ${D}${sysconfdir}/mosquitto/
	install -m 0755 ${WORKDIR}/mosquitto.init.d ${D}${sysconfdir}/init.d/mosquitto

	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/mosquitto.service ${D}${systemd_unitdir}/system
}

# useradd and user group for mosquitto
USERADD_PACKAGES = "${PN}"
M_USER = "mosquitto"
M_GROUP = "mosquitto"
GROUPADD_PARAM_${PN} = "--system ${M_GROUP}"
USERADD_PARAM_${PN} = "--system -s /bin/bash -g ${M_GROUP} ${M_USER}"
M_OWNER_AND_GROUP = "-o ${M_USER} -g ${M_GROUP}"


SRC_URI[md5sum] = "c217dea4bdc7573a2eaea8387c18a19e"
SRC_URI[sha256sum] = "ca47533bbc1b7c5e15d6e5d96d3efc59677f2515b6692263c34b7c48f33280c5"

PACKAGES += "libmosquitto1 libmosquittopp1 ${PN}-clients ${PN}-python"

FILES_${PN} = "${sbindir}/mosquitto \
               ${bindir}/mosquitto_passwd \
               ${sysconfdir}/mosquitto \
               ${sysconfdir}/init.d/mosquitto \
               ${systemd_unitdir}/system/mosquitto.service \
"

FILES_libmosquitto1 = "${libdir}/libmosquitto.so.1"

FILES_libmosquittopp1 = "${libdir}/libmosquittopp.so.1"

FILES_${PN}-clients = "${bindir}/mosquitto_pub \
                       ${bindir}/mosquitto_sub \
"

FILES_${PN}-staticdev += "${libdir}/libmosquitto.a"

FILES_${PN}-python = "/usr/lib/python2.7/site-packages"

