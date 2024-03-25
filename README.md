# WIP support for Hyperloglog in Starrocks via Java UDF


## Installation on Starrocks

```sql
CREATE GLOBAL FUNCTION TRINO_HLL_CARDINALITY(STRING)
RETURNS LARGEINT
properties (
    "symbol" = "org.hhhonzik.starrocks.TrinoHLLCardinality", 
    "type" = "StarrocksJar",
    "file" = "https://github.com/hhhonzik/starrocks-trino-hll/raw/main/target/starrocks_trino_hll-1.0-SNAPSHOT-jar-with-dependencies.jar"
);
```
```sql
CREATE GLOBAL AGGREGATE FUNCTION TRINO_HLL_MERGE(STRING)
RETURNS STRING
properties (
    "symbol" = "org.hhhonzik.starrocks.TrinoHLLMerge", 
    "type" = "StarrocksJar",
    "file" = "https://github.com/hhhonzik/starrocks-trino-hll/raw/main/target/starrocks_trino_hll-1.0-SNAPSHOT-jar-with-dependencies.jar"
);
```


## USAGE

```sql
SELECT TRINO_HLL_CARDINALITY(from_binary(YOUR_HLL_COLUMN, 'encode64'))
SELECT TRINO_HLL_MERGE(from_binary(YOUR_HLL_COLUMN, 'encode64'))
```
