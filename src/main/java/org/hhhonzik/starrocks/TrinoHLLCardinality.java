package org.hhhonzik.starrocks;
// import com.facebook.airlift.stats.cardinality.HyperLogLog;

import com.facebook.airlift.stats.cardinality.HyperLogLog;

public class TrinoHLLCardinality {

    // taken from prestodb/presto
    public static final double DEFAULT_STANDARD_ERROR = 0.01625;

    public static int standardErrorToBuckets(double maxStandardError) {
        return log2Ceiling((int) Math.ceil(1.0816 / (maxStandardError * maxStandardError)));
    }

    public final Long evaluate(String serializedHll) {
        // return HyperLogLog.newInstance(serializedHll).cardinality();
        return 123L;
    }

    private static int log2Ceiling(int value) {
        return Integer.highestOneBit(value - 1) << 1;
    }
}
