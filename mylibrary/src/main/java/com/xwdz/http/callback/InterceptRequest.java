package com.xwdz.http.callback;

import com.xwdz.http.core.Request;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public interface InterceptRequest {

    Request onInterceptRequest(Request request);

}
