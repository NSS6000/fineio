package com.fineio.io;

/**
 * @author yee
 * @date 2018/9/27
 */
public interface BufferW extends Buffer {
    boolean full();

    void force();

    void write();
}
