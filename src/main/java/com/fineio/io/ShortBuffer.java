package com.fineio.io;

import com.fineio.io.file.FileBlock;
import com.fineio.memory.MemoryConstants;
import com.fineio.memory.MemoryUtils;
import com.fineio.storage.Connector;

/**
 * @author yee
 * @date 2018/9/19
 */
public class ShortBuffer extends BaseBuffer<ShortBuffer.ShortReadBuffer, ShortBuffer.ShortWriteBuffer> {
    public ShortBuffer(Connector connector, FileBlock block, int maxOffset, Listener listener) {
        super(connector, block, maxOffset, listener);
    }

    @Override
    protected int getOffset() {
        return MemoryConstants.OFFSET_SHORT;
    }

    @Override
    public ShortWriteBuffer asWrite() {
        return new ShortBufferW();
    }

    @Override
    public ShortReadBuffer asRead() {
        return new ShortBufferR();
    }

    public interface ShortReadBuffer extends BufferR {
        short get(int pos);
    }

    public interface ShortWriteBuffer extends BufferW {
        void put(short value);
    }

    private class ShortBufferR extends ReadBuffer implements ShortReadBuffer {
        @Override
        public short get(int pos) {
            checkRead(pos);
            return MemoryUtils.getShort(readAddress, pos);
        }
    }

    private class ShortBufferW extends WriteBuffer implements ShortWriteBuffer {
        @Override
        public void put(short value) {
            ensureCapacity(++writeCurrentPosition);
            MemoryUtils.put(address, writeCurrentPosition, value);
        }
    }
}

