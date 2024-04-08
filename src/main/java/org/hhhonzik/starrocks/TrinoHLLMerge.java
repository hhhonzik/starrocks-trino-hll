package org.hhhonzik.starrocks;

import com.facebook.airlift.stats.cardinality.HyperLogLog;

import java.nio.ByteBuffer;
import java.util.Base64;

import static io.airlift.slice.Slices.wrappedBuffer;

import io.airlift.slice.SizeOf;
import io.airlift.slice.Slice;

public class TrinoHLLMerge {
    public static class State {
        HyperLogLog hll = HyperLogLog.newInstance(standardErrorToBuckets(DEFAULT_STANDARD_ERROR));

        public int serializeLength() {
            return SizeOf.SIZE_OF_INT + hll.serialize().getBytes().length;
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

    public void serialize(State state, ByteBuffer buff) {
        byte[] serialized = state.hll.serialize().getBytes();
        buff.putInt(serialized.length);
        buff.put(serialized);
    }

    public void merge(State state, ByteBuffer buffer) {
        // idk why / where this is coming from
        int len = buffer.getInt();
        byte[] bytes = new byte[len];
        buffer.get(bytes);

        Slice s = wrappedBuffer(bytes);
        try {
            HyperLogLog other = HyperLogLog.newInstance(s);
            state.hll.mergeWith(other);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + " HLL: " + Base64.getEncoder().encodeToString(s.getBytes()));
        }
    }

    public String finalize(State state) {
        return Base64.getEncoder().encodeToString(state.hll.serialize().getBytes());
    }


    public void reset(State state) {
        state.hll = HyperLogLog.newInstance(standardErrorToBuckets(DEFAULT_STANDARD_ERROR));
    }

    public void windowUpdate(State state,
                            int peer_group_start, int peer_group_end,
                            int frame_start, int frame_end,
                            String[] inputs) {
        for (int i = (int)frame_start; i < (int)frame_end; ++i) {
            this.update(state, inputs[i]);
        }
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
