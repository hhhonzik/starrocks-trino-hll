# WIP support for Hyperloglog in Starrocks via Java UDF




## add UDF functions

```
CREATE FUNCTION TRINO_HLL_CARDINALITY(varbinary)
RETURNS LARGEINT
properties (
    "symbol" = "org.hhhonzik.starrocks.TrinoHLLCardinality", 
    "type" = "StarrocksJar",
    "file" = "https://github.com/hhhonzik/starrocks-trino-hll/raw/main/target/starrocks_trino_hll-1.0-SNAPSHOT-jar-with-dependencies.jar"
);
```
