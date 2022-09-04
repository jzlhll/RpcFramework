package com.rpcframework.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ServerManager {
    private final Map<Class<?>, Object> businessList = Collections.synchronizedMap(new HashMap<>());
    public final void put(Class<?> clazz, Object instance) {
        businessList.put(clazz, instance);
    }

    /**
     * 先调用get(Class)获取不到，再封装Call过来调用本函数获取。
     *
     * 经过思考，不论是否相同进程，只要下面的get(class)，
     * 我们不是同进程同一个key的interface就必须通过Call来转换。
     * 举例：Client和Server同时可以引用到IOrder.class的接口类，并且是同一个进程。
     * 这样的话，我们就能从Server.Instance, 进而businessList中直接拿到对应的实例。
     *
     * 因为：
     * 1. 不同进程，实例业务对象，都没有显然必须转换；
     * 2. 同进程，由于接口类不通，导致使用的接口不同（模块化开发的解耦，否则你得跟服务端一起更新）。
     *    虽然我们能通过转换，拿到实例对象了，但是由于method未知也同样需要转换。
     */
    public final Object get(String className) {
        //2. 获取不到。证明同进程，但是无法同时引起业务接口类，需要转换。
        //将客户端的接口的注解翻译成服务器中的接口类。
        Class<?> svrInterfaceCls = null;
        try {
            svrInterfaceCls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (svrInterfaceCls != null) {
            return businessList.get(svrInterfaceCls);
        }

        return null;
    }

    public final Object get(Class<?> clientInterfaceClass) {
        //获取到了，同模块（或者共同引用到了sdk的业务接口）直接通过同一个接口class->真实对象
        return businessList.get(clientInterfaceClass);
    }
}
