package com.fineio.v1.io.file;

import com.fineio.storage.Connector;
import com.fineio.v1.io.Buffer;
import com.fineio.v1.io.ByteBuffer;
import com.fineio.v1.io.CharBuffer;
import com.fineio.v1.io.DoubleBuffer;
import com.fineio.v1.io.FloatBuffer;
import com.fineio.v1.io.IntBuffer;
import com.fineio.v1.io.LongBuffer;
import com.fineio.v1.io.ShortBuffer;
import com.fineio.v1.io.write.ByteWriteBuffer;
import com.fineio.v1.io.write.CharWriteBuffer;
import com.fineio.v1.io.write.DoubleWriteBuffer;
import com.fineio.v1.io.write.FloatWriteBuffer;
import com.fineio.v1.io.write.IntWriteBuffer;
import com.fineio.v1.io.write.LongWriteBuffer;
import com.fineio.v1.io.write.ShortWriteBuffer;

import java.net.URI;

/**
 * Created by daniel on 2017/2/10.
 */
public final class WriteIOFile<T extends Buffer> extends IOFile<T> {

    public static final WriteModel<ByteBuffer> BYTE = ByteWriteBuffer.MODEL;

    public static final WriteModel<DoubleBuffer> DOUBLE = DoubleWriteBuffer.MODEL;

    public static final WriteModel<LongBuffer> LONG = LongWriteBuffer.MODEL;

    public static final WriteModel<IntBuffer> INT = IntWriteBuffer.MODEL;

    public static final WriteModel<FloatBuffer> FLOAT = FloatWriteBuffer.MODEL;

    public static final WriteModel<CharBuffer> CHAR = CharWriteBuffer.MODEL;

    public static final WriteModel<ShortBuffer> SHORT = ShortWriteBuffer.MODEL;

    WriteIOFile(Connector connector, URI uri, WriteModel<T> model){
        super(connector, uri, model);
        this.block_size_offset = (byte) (connector.getBlockOffset() - model.offset());
        single_block_len = (1L << block_size_offset) - 1;
    }

    /**
     * 创建File方法
     * @param connector 连接器
     * @param uri 子路径
     * @param model 子类型
     * @param <E> 继承ReadBuffer的子类型
     * @return
     */
    public static final <E extends Buffer> WriteIOFile<E> createFineIO(Connector connector, URI uri, WriteModel<E> model){
        return  new WriteIOFile<E>(connector, uri, model);
    }


}