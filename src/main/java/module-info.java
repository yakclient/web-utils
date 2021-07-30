module yakclient.web.utils {
    requires kotlin.stdlib;
    requires kotlin.reflect;

    requires spring.core;
    requires spring.hateoas;
    requires spring.web;
    requires org.slf4j;

    exports net.yakclient.web.utils;
    exports net.yakclient.web.utils.annotation;
    exports net.yakclient.web.utils.helper;
    exports net.yakclient.web.utils.reflection;
}