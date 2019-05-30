package com.xwdz.http.error;

import java.io.Serializable;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class EasyHTTPException extends Throwable implements Serializable {

    private static final long serialVersionUID = -492948457991527824L;

    public static final class Error {
        public static final String CANCEL_REQUEST = "1004";
    }


    public final String message;
    public final String code;


    public EasyHTTPException(String message, String code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String toString() {
        return "EasyHTTPException{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
