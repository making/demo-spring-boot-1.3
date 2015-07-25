#!/bin/bash

VAGRANT_PROVISION=/var/vagrant/provison

if [ ! -d $VAGRANT_PROVISION ];then
    echo "==== Create $VAGRANT_PROVISION ===="
    mkdir -p $VAGRANT_PROVISION
    yum update
fi

if [ ! -f $VAGRANT_PROVISION/ja ];then
    echo "==== Configure ja settings ===="
    localectl set-locale LANG=ja_JP.UTF-8
    mv /etc/localtime /etc/localtime.org
    ln -s /usr/share/zoneinfo/Asia/Tokyo /etc/localtime
    touch $VAGRANT_PROVISION/ja
fi


if [ ! -f $VAGRANT_PROVISION/jdk ];then
    echo "==== Install JDK ===="
    yum -y -q install java-1.8.0-openjdk-devel
    touch $VAGRANT_PROVISION/jdk
fi


if [ ! -f $VAGRANT_PROVISION/demo ];then
    echo "==== Install Demo ===="
    mkdir -p /var/demo
    cp /vagrant/target/demo*.jar /var/demo/demo

    cat <<'EOF' > /etc/systemd/system/demo.service
[Unit]
Description=demo
After=syslog.target

[Service]
ExecStart=/var/demo/demo

[Install]
WantedBy=multi-user.target
EOF

    systemctl daemon-reload
    systemctl start demo
    systemctl status demo
    systemctl enable demo.service

    touch $VAGRANT_PROVISION/demo
fi

