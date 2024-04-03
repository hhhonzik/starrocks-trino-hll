package org.hhhonzik.starrocks;


import com.facebook.airlift.stats.cardinality.HyperLogLog;
import java.util.Base64;
import static io.airlift.slice.Slices.wrappedBuffer;


import io.airlift.slice.Slice;

public class TrinoHLLCardinality {
    // taken from prestodb/presto
    public static final double DEFAULT_STANDARD_ERROR = 0.01625;

    public static int standardErrorToBuckets(double maxStandardError) {
        return log2Ceiling((int) Math.ceil(1.0816 / (maxStandardError * maxStandardError)));
    }

    public final Long evaluate(String serializedHll) {
        if (serializedHll != null) {
            byte[] bytes = Base64.getDecoder().decode(serializedHll);
            Slice s = wrappedBuffer(bytes);

            return HyperLogLog.newInstance(s).cardinality();
        }
        
        return null;
    }

    private static int log2Ceiling(int value) {
        return Integer.highestOneBit(value - 1) << 1;
    }
}
