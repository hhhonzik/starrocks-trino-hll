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
import org.junit.jupiter.api.Test;

public class TestTrinoHLLCardinality {
    @Test
    public void testEmpty() {
        TrinoHLLCardinality c = new TrinoHLLCardinality();

        assertThat(c.evaluate("AgwAAA==")).isEqualTo(0L);
    }

    @Test
    public void testCardinality()
    {
        TrinoHLLCardinality c = new TrinoHLLCardinality();
        Long cardinality = c.evaluate("AgwxAAKmBwsAxDQMgMruEQBKRRhDUnwYgYzpGgD6mCeBrv9AwL1CSEAlOFCAmmVSQES5UsBt1lrArHpfgh9/X0E8jmBB6cNlA3CudIHvYXbAfzh9gPjsicIwpJFAbOecABhqngB3P5/BrvigQPWNpsLPxKaAuXGnQIBUqUCbKqrA22ytQSVIsEDb9rECjta9gDHnvYRRFsAARtHEgtxeyoCoytDB5fPWQZP810GgudiAHjHbgMRq6wBu6vGAwZTywKzO+AS/+/g=");
        assertThat(cardinality).isEqualTo(49L);
    }

    @Test
    public void testBigCardinality()
    {
        TrinoHLLCardinality c = new TrinoHLLCardinality();
        Long cardinality = c.evaluate("AwwBIyUjQ2RYIicVVSVSdVJ1RpJEZ2UzeYQ1VTIiREJTVFUmQkUyOSZBd2NEUXQjMjMzJVRTRSdUhEJaIyNJJGVEIzMiQ0RnMWdCRDOTVxdWM4VkJZQzIzU1NTMzdTM1VMVlSFJVNXW2RINSEzUzITZUVnJHdBJCRDQzhoJESUR4KTNERyQiSFdVZDNDJlIzUnVlQkVERzdCVUYyRTZiNFRWQ0RzMWQzRiN0dDMkUyOTVDdSUpVUM2o1Q1QlYkNTJGI0RUVSRWNVlEWERFE1MVVSO2I0ImUiNDNFNkU3M2M1ZYREMUJUNkJjJXQyNlJ0L2R4EydUdSMyUjKURTNoR0M1I0MzVDNhA1aTNGNoU2MiI3UzIWJGRCZFZEVjVkU0RyVHUjRBOHM6VEVCMjMyNTMjNDdUQxKEZUNDIlZGRmFHNEVkJUY0NIdGVDQ1M4M0VEMjM0RjQySTIzMTMmVUhHMzJkUkcjYjNWU2RSpCIiOSojQjMTQnNZJkVkQ0UjRDg2U0JCNEQxU2IjQzJRVUQ0VWQ0KXNIZjV0VUVENjJTNzVVNFFlJiM4NEJFNlMmQzUxVBVEJUJCM2MlMTZCJVM1NjUjQzNVRFNkRkZGJlViUjdHJEc1RjVCViQ0RRIzU1IzVHU1VTNEKVNnJEQjNFMzczM0VLQUUzMiRERzY0JDQ1U2M1SVQ1M1UytTI2UkMnYzNzZ0FDUzQ2OEM3YkNSNlQzFJJjI0NDFDU0UkQkIyUSczVEN0ZjV0NDUxciOVKGVWU1RWNiJEE2UzM0RTRSU4lVOiZkklUxWDJTpDRFMlR2QzREMUQ5o1NSg1FoNSVFRlTyEzRUUcUyVkViRDOUc0QkRDMzNSMzNENTMjM0MyQySyZTNGNJMlhTVmN1JWRUNGNiNDZDgzdEQkRJM0ZFMjZ2cmNVMlViViJEIzQTZSFimVMTNpM0JUM0NVRHREMzM1UzVEKiVCJURHQnUmOFg6JDOEk0NYRHI1VWNis0NUJTRCRDRDRTM1R0UkM1MyIoRWIzJDQoNShic0VTJkRTJFQjNCVXhEZkk2JTNjNUNmIkVERTRDMyRDV3QkRUZlQ1dzMmRjRiNjUyQSRVUWNzREIiJCQ4NHNTJ0U1Y3RDNURnKaRDLCM0NyQiRiWBcjaHVHViQlcyMUIlMiIWY2Q1Q2NUZVNDIjk0pVI1QiJERFMVVxQ1F0kzWjJ1I0MyNjQjZCJFZlQzQiIzRC02InRJI3Q0UTgVJVIzNWYzU0ImOVFVdDRTNUFSZEI0Q1YyQhUjQ0OUNSJVRERFVSVDNYREdGRXIjVIUlNSVmOTJVZFNyRVRDJHZUJLRFVyM0NDMUSkY1U3QylIODQyOXNFVGU5ZilUJEJTJVNFNFI2SFNDQyNjNmhEQqtiNFMzdTRDZSc3lRVERWYyc1MlJTNoVCJFWFRDRkOHODSUM1I1hUVCJEY0Y0kzMkNFWEQiPCNTdTRCUjUyQ1Y0YkOINHQkZIQkQkNCpkhDJHhERIMnNEhlVjpjOEQ2QiMjNkEyc1VEJERSdWNWJEMjODJChCVEM2RkJ1YVU0ISRjVEKUJjGTxFRlMjNjMzQUISFaNFFHImhUZUOSMzVVN3V2YiFDZSdURFNlEzFiMyVSNTI2RbMmUjVENlZ0NCUyJmNTUkNIZ1RDojJTMTlJQ0IqM4NFVWMiYkNCRERkJmSNhWFTUkRiQjJ0SEVBRDRiMnQxZ4EjNmcmJHNCRiIyNjpIUVVTVnNERIgURzNSWCVEVCNTGFM1VjJFQ0hXczYkJVMyRjNWMjREcjNEdmMXdEYzElJTlFRmQSdGI0REU2ZYE1MzRDOVREU1cjNDIzQxVjFDRGQjY2RzVSVENFNFI0Q1ZSGHM0IkQ4Q1N2NTVEUzYTVzQlUzQzdlJ4UjMxUpNFQhMzNzQhYyZGNDNTJEM1NDR0hERlYsRUIzWEM1RZEkNHYyNlZGRGRFJDVmWyYzNlMyVjFaI1NTNBRDVXNEdCZFNlNEh1UUFUUzVmTUUjRkQmZzMnhUMhNERFIzQ3ZURDVVNDNUNDRTIzQ2RVJERDYzJSN0ZGeiRBRHVDQ2ZEeURWNTUzVjoyRkJSV1JFYzUTUyUiMlUjUlJUVENlJzVEMzUjNUQ0RGQlNTIiUlRUsyVnNBZjmDZkEyN1QWYkJENVEiRFQ0JERFNCQ1M0SjR0MzNURDYzVhJDNSMTQkNlVnQ2V1ZyBidVJnF3NFRnVjY6VCaVUjUiNGIzRTNlRDRTIjVSRERmM2QyXFNDdlVDQxMzRVkzFDJEU0YzZDMlEihSRHJDVVQiU3mjN0JSVhTyVFRmRiIxNXR4MkRHMlIzNFMihCUjImNJVEozURRyQyciSERUYzZlczJTQkR3WzREJIVCN0QiNlVVYyVWIjQ2pDIyM3RmYpRSRVUyNTUnYicVhCI0QzMzZCRzIkU0JFVUNTRDV0ODUzE1FzJGIkRGRDRIRDRUOFMyNUhCojMzJTFWNFNmYxNTRDRUJDRDY0MzJnNjUSQjNBdiVUk0JCNDVlQ1VTJEMyMnNVU1hURTVWhFJiNUQyQjNlQ0ojNTYmNCpmQzS0JzM0RCN1NWJhMyVFZDQzUVUnljJzVTYzMzNzdTUkMjRWRFQzWTdTM1UkVEdXNGOGRSUzRLSEdCQzVGZTRGQzNJU4lCdSRZKVhTQ5MyllEzQjIVJaZTVFRkNFRlM3VERTVEdUNSVElUU4YlNSVDMUNiMjMVRUZ2J0kAAA");
        assertThat(cardinality).isEqualTo(58312L);
    }
}