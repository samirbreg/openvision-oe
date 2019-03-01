DESCRIPTION = "Open Vision version info"
SECTION = "base"
PRIORITY = "required"
MAINTAINER = "Open Vision Developers"
require conf/license/license-gplv2.inc

PV = "${VISIONVERSION}"
PR = "${VISIONREVISION}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://settings \
           file://ov.py \
           file://openvision-remover.sh \
           file://openvision-timesync.sh \
          "

FILES_${PN} = "/etc /usr"

INHIBIT_PACKAGE_STRIP = "1"

do_configure[nostamp] = "1"

ALLOW_EMPTY_${PN} = "1"

S = "${WORKDIR}"

PACKAGES = "${PN}"

INITSCRIPT_NAME = "openvision-timesync.sh"
INITSCRIPT_PARAMS = "start 19 3 ."

inherit update-rc.d

do_compile() {
	python -O -m compileall ${S}
}

do_install() {
    install -d ${D}/etc
    echo "STB=${MACHINE}" > ${D}/etc/image-version
    echo "box_type=${MACHINE}" >> ${D}/etc/image-version
    echo "build_type=0" >> ${D}/etc/image-version
    echo "version=${VISIONVERSION}-${VISIONREVISION}" >> ${D}/etc/image-version
    echo "build=${VISIONREVISION}" >> ${D}/etc/image-version
    echo "Python=2.7" >> ${D}/etc/image-version
    echo "date=${DATETIME}" >> ${D}/etc/image-version
    echo "comment=Open Vision" >> ${D}/etc/image-version
    echo "target=9" >> ${D}/etc/image-version
    echo "creator=Open Vision Developers" >> ${D}/etc/image-version
    echo "url=https://openvision.tech" >> ${D}/etc/image-version
    echo "catalog=https://github.com/OpenVisionE2" >> ${D}/etc/image-version
    echo "distro=${DISTRO_NAME}" >> ${D}/etc/image-version
    echo "${MACHINE}" > ${D}/etc/model
    if ${@bb.utils.contains_any("MACHINE_FEATURES", "smallflash", "true", "false", d)}; then
        echo "smallflash" > ${D}/etc/smallflash
    install -d ${D}${sysconfdir}/enigma2
    install -m 0755 ${WORKDIR}/settings	${D}${sysconfdir}/enigma2
    install -d ${D}/usr/share/enigma2/picon
    install -d ${D}/usr/lib/python2.7
    install -m 0644 ${WORKDIR}/ov.pyo	${D}/usr/lib/python2.7
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/openvision-remover.sh	${D}${sysconfdir}/init.d
    install -d ${D}${sysconfdir}/rcS.d
    ln -sf ../init.d/openvision-remover.sh	${D}${sysconfdir}/rcS.d/S09openvision-remover.sh
    install -m 0755 ${WORKDIR}/openvision-timesync.sh	${D}${sysconfdir}/init.d
}
