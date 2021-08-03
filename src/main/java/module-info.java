module yakclient.web.utils {
    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires spring.core;
    requires spring.hateoas;
    requires spring.web;
    requires org.slf4j;
    requires spring.context;
    requires spring.boot;
    requires spring.beans;
    requires java.sql;
    requires spring.boot.autoconfigure;

    exports net.yakclient.web.utils;
    exports net.yakclient.web.utils.extension;
    exports net.yakclient.web.utils.annotation;
    exports net.yakclient.web.utils.helper;
    exports net.yakclient.web.utils.reflection;

    opens net.yakclient.web.utils to spring.core;
}