package org.hhhonzik.starrocks;
/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

public class TestTrinoHLLMerge {
    @Test
    public void testSingle() {
        TrinoHLLMerge t = new TrinoHLLMerge();

        TrinoHLLMerge.State s = t.create();

        t.update(s, "AgwAAA==");

        assertThat(t.finalize(s)).isEqualTo("AgwAAA==");
    }

    @Test
    public void testMerge() {
        TrinoHLLMerge t = new TrinoHLLMerge();
        TrinoHLLMerge.State s1 = t.create();

        t.update(s1, "AgwAAA==");

        TrinoHLLMerge.State s2 = t.create();

        t.update(s2, "AgwxAAKmBwsAxDQMgMruEQBKRRhDUnwYgYzpGgD6mCeBrv9AwL1CSEAlOFCAmmVSQES5UsBt1lrArHpfgh9/X0E8jmBB6cNlA3CudIHvYXbAfzh9gPjsicIwpJFAbOecABhqngB3P5/BrvigQPWNpsLPxKaAuXGnQIBUqUCbKqrA22ytQSVIsEDb9rECjta9gDHnvYRRFsAARtHEgtxeyoCoytDB5fPWQZP810GgudiAHjHbgMRq6wBu6vGAwZTywKzO+AS/+/g=");

        ByteBuffer b = ByteBuffer.allocate(s2.serializeLength());
        t.serialize(s2, b);
        b.position(0); // reset position to 0 (start of buffer)
        
        t.merge(s1, b);

        assertThat(s1.hll.cardinality()).isEqualTo(49L);
    }
}