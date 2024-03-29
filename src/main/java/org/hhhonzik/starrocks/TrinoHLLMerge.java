package org.hhhonzik.starrocks;

import com.facebook.airlift.stats.cardinality.HyperLogLog;
import java.util.Base64;
import static io.airlift.slice.Slices.wrappedBuffer;

import io.airlift.slice.Slice;

public class TrinoHLLMerge {
    public static class State {
        HyperLogLog hll = HyperLogLog.newInstance(standardErrorToBuckets(DEFAULT_STANDARD_ERROR));

        public int serializeLength() {
            return hll.estimatedSerializedSize();
        }
    }

    public State create() {
        return new State();
    }

    public void destroy(State state) {
    }

    public final void update(State state, String serializedHll) {
        if (serializedHll != null) {
            byte[] bytes = Base64.getDecoder().decode(serializedHll);
            Slice s = wrappedBuffer(bytes);
            HyperLogLog other = HyperLogLog.newInstance(s);

            state.hll.mergeWith(other);
        }
    }

    public void serialize(State state, java.nio.ByteBuffer buff) {
        buff.put(state.hll.serialize().getBytes());
    }

    public void merge(State state, java.nio.ByteBuffer buffer) {
        buffer.position(0);
        buffer.limit(buffer.capacity() - 1);
        Slice s = wrappedBuffer(buffer  );
        HyperLogLog other = HyperLogLog.newInstance(s);

        state.hll.mergeWith(other);
    }

    public String finalize(State state) {
        return Base64.getEncoder().encodeToString(state.hll.serialize().getBytes());
    }

    // taken from prestodb/presto
    public static final double DEFAULT_STANDARD_ERROR = 0.01625;

    public static int standardErrorToBuckets(double maxStandardError) {
        return log2Ceiling((int) Math.ceil(1.0816 / (maxStandardError * maxStandardError)));
    }

    private static int log2Ceiling(int value) {
        return Integer.highestOneBit(value - 1) << 1;
    }
}
