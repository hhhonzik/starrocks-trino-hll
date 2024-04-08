# Support for Athena / Trino / Presto Hyperloglog on Starrocks via UDF

## Installation on Starrocks

```sql
CREATE GLOBAL FUNCTION CARDINALITY(STRING)
RETURNS BIGINT
properties (
    "symbol" = "org.hhhonzik.starrocks.TrinoHLLCardinality", 
    "type" = "StarrocksJar",
    "file" = "https://github.com/hhhonzik/starrocks-trino-hll/releases/download/v0.0.8/starrocks_trino_hll-build-jar-with-dependencies.jar"
);
```
```sql
CREATE GLOBAL AGGREGATE FUNCTION MERGE(STRING)
RETURNS STRING
properties (
    "annalytic" = "true", 
    "symbol" = "org.hhhonzik.starrocks.TrinoHLLMerge", 
    "type" = "StarrocksJar",
    "file" = "https://github.com/hhhonzik/starrocks-trino-hll/releases/download/v0.0.8/starrocks_trino_hll-build-jar-with-dependencies.jar"
);
```

## USAGE

Java UDF cannot recieve binary / varbinary data, so we just need to send it encoded as base64 string.

```sql
SELECT CARDINALITY(from_binary(YOUR_HLL_COLUMN, 'encode64'))
SELECT MERGE(from_binary(YOUR_HLL_COLUMN, 'encode64'))

--- you can combine them

select cardinality(merge(from_binary(my_hll_column, 'encode64'))) as unique_count from `glue_catalog`...
```
