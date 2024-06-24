package com.gymmanagementsystembackend.tool;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalManage {
    // 私有构造器，防止外部实例化
    private ThreadLocalManage() {
        // 在这里可以初始化threadLocal，但通常这会在使用它的方法中完成
    }

    // 线程安全的单例模式实现（双重检查锁定）
    private static volatile ThreadLocalManage instance;

    public static ThreadLocalManage getInstance() {
        if (instance == null) {
            synchronized (ThreadLocalManage.class) {
                if (instance == null) {
                    instance = new ThreadLocalManage();
                }
            }
        }
        return instance;
    }

    // 使用ThreadLocal来存储线程特定的数据
    // 通常在需要的地方（如方法中）进行初始化
    private ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    // 提供获取ThreadLocal中Map的方法
    public Map<String, Object> getThreadLocalMap() {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            // 如果还没有初始化，则创建一个新的Map
            map = new HashMap<>();
            threadLocal.set(map);
        }
        return map;
    }

    public void setThreadLocalMap(String key,Object data){
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            // 如果还没有初始化，则创建一个新的Map
            map = new HashMap<>();

            map.put(key,data);

            threadLocal.set(map);
        }
    }

    // 提供一个清理ThreadLocal的方法（可选）
    public void clearThreadLocal() {
        threadLocal.remove();
    }
}
