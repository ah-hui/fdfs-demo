package com.example.fdfsdemo;

/**
 * 功能描述：FastDFS操作失败.
 *
 * @author liuguanghui
 * @since 2019-05-16
 */
public class FdfsFailException extends RuntimeException {

    public FdfsFailException() {
        super("FastDFS操作失败！");
    }

    public FdfsFailException(String message) {
        super(message);
    }
}
