package net.mckitsu.lib.util.pool;

public class BufferPools {
    /* **************************************************************************************
     *  Abstract method
     */

    /* **************************************************************************************
     *  Construct method
     */

    /* **************************************************************************************
     *  Override method
     */

    /* **************************************************************************************
     *  Public method
     */
    public static ByteBufferPool newFixedBufferPoolPool(int nBuffers, int bufferSize){
        return new ByteBufferPool(nBuffers, nBuffers, bufferSize);
    }

    public static ByteBufferPool newCacheBufferPoolPool(int nBuffersMin, int nBuffersMax, int bufferSize){
        return new ByteBufferPool(nBuffersMin, nBuffersMax, bufferSize);
    }

    /* **************************************************************************************
     *  protected method
     */

    /* **************************************************************************************
     *  Private method
     */
}
