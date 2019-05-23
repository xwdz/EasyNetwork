package com.xwdz.httpsimple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyCallback {


    ThreadType threadType() default ThreadType.CallbackMainThread;


    enum ThreadType {

        CallbackMainThread,
        CallbackWorkThread
    }

}
