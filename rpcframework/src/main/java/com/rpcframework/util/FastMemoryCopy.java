//package com.rpcframework.util;
//
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class FastMemoryCopy {
//    /**
//     * 快速从某个class的实例对象，转义copy到另外一个同样的class类创建对象并复制内容。
//     * 内部采用hashMap进行了缓存。
//     * ===
//     * todo：参考别的框架优化如下2个问题，定义bean类型有如下不算很难接受的规则：
//     * todo 第1：必须要求bean class，有默认空构造函数；
//     * todo 第2：设置参数必须是public或者小写set开头的设置参数的函数。protected，pkg protected, private都被忽略。
//     * todo 第3：get函数，不能有参数。(这个很容易遵守吧。)
//     * todo 第4：不得使用大小写做不同参数。比如aa, aA会被当做同一个参数。todo：很容易优化，后续再说。
//     * todo 第5：transient字段jdk本意约定不做序列化。这里也会做序列化。
//     */
//    public static final class FastMemorySerialUtil {
//        private FastMemorySerialUtil() {}
//
//        private static final ConcurrentHashMap<Class<?>, PublicGetMethodsFields> getsKv = new ConcurrentHashMap<>();
//        private static final ConcurrentHashMap<Class<?>, PublicSetMethodsFields> setsKv = new ConcurrentHashMap<>();
//
//        //    public static Field[] getAllFields(Class<?> cls) {
////        final List<Field> allFieldsList = getAllFieldsList(cls);
////        return allFieldsList.toArray(new Field[allFieldsList.size()]);
////    }
//
//        public static HashSet<Field> getAllPublicFields(Class<?> cls) {
//            assert cls != null;
//            final HashSet<Field> allFields = new HashSet<>();
//
//            Class<?> currentClass = cls;
//            while (currentClass != null) {
//                final Field[] declaredFields = currentClass.getFields();
//                allFields.addAll(Arrays.asList(declaredFields));
//                currentClass = currentClass.getSuperclass();
//            }
//            return allFields;
//        }
//
//        public static HashSet<Method> getAllPublicSetMethods(Class<?> cls, String setOrGet) {
//            assert cls != null;
//            final HashSet<Method> allMethods = new HashSet<>();
//
//            Class<?> currentClass = cls;
//            while (currentClass != null) {
//                var methods = currentClass.getMethods();
//                for (var m : methods) {
//                    if (m.getName().startsWith(setOrGet)) {
//                        allMethods.add(m);
//                    }
//                }
//                currentClass = currentClass.getSuperclass();
//            }
//            return allMethods;
//        }
//
//        public static PublicSetMethodsFields getClassPublicSetMethodsFields(Class<?> cls) {
//            var o = setsKv.get(cls);
//            if (o != null) {
//                return o;
//            }
//
//            var allPublicSetMethods = getAllPublicSetMethods(cls, "set");
//            var allPublicFields = getAllPublicFields(cls);
//            //todo 过滤一遍，去掉同名的set method和field
//            var r = new PublicSetMethodsFields();
//            r.fields = allPublicFields.toArray(new Field[0]);
//            r.methods = allPublicSetMethods.toArray(new Method[0]);
//
//            setsKv.put(cls, r);
//            return r;
//        }
//
//        public static PublicGetMethodsFields getClassPublicGetMethodsFields(Class<?> cls) {
//            var o = getsKv.get(cls);
//            if (o != null) {
//                return o;
//            }
//
//            var allPublicSetMethods = getAllPublicSetMethods(cls, "get");
//            var allPublicFields = getAllPublicFields(cls);
//            //todo 过滤一遍，去掉同名的set method和field
//            var r = new PublicGetMethodsFields();
//            r.fields = allPublicFields.toArray(new Field[0]);
//            r.methods = allPublicSetMethods.toArray(new Method[0]);
//            getsKv.put(cls, r);
//            return r;
//        }
//
//        /**
//         * 将原来的类型参数转变为另外一个类型的对象
//         * transient 也会被忽略
//         * @return
//         */
//        public static Object object2object(Object srcInstance, Class<?> target) throws IllegalAccessException, InvocationTargetException {
//            Class<?> origClass = srcInstance.getClass();
//            var getKv = getClassPublicGetMethodsFields(origClass);
//            for (var s : getKv.fields) {
//                System.out.println("field get: " + s.getName() + ", " + s.get(srcInstance));
//            }
//            for (var m : getKv.methods) {
//                System.out.println("method get: " + m.getName() + ", " + m.invoke(srcInstance));
//            }
//
//            var fs = origClass.getFields();
//            var dfs = origClass.getDeclaredFields();
//            System.out.println("fs " + fs + " " + dfs);
//            return null;
//        }
//    }
//
//    //所有set开头的函数和公开的变量综合信息
//    final class PublicSetMethodsFields {
//        Field[] fields;
//        Method[] methods;
//    }
//
//    final class PublicGetMethodsFields {
//        Field[] fields;
//        Method[] methods;
//    }
//
//}
