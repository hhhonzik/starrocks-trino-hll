# Support for Athena / Trino / Presto Hyperloglog on Starrocks via UDF

## Installation on Starrocks

```sql
CREATE GLOBAL FUNCTION TRINO_HLL_CARDINALITY(STRING)
RETURNS LARGEINT
properties (
    "symbol" = "org.hhhonzik.starrocks.TrinoHLLCardinality", 
    "type" = "StarrocksJar",
    "file" = "https://github.com/hhhonzik/starrocks-trino-hll/releases/download/v0.0.1/starrocks_trino_hll-0.0.1-jar-with-dependencies.jar"
);
```
```sql
CREATE GLOBAL AGGREGATE FUNCTION TRINO_HLL_MERGE(STRING)
RETURNS STRING
properties (
    "symbol" = "org.hhhonzik.starrocks.TrinoHLLMerge", 
    "type" = "StarrocksJar",
    "file" = "https://github.com/hhhonzik/starrocks-trino-hll/releases/download/v0.0.1/starrocks_trino_hll-0.0.1-jar-with-dependencies.jar"
);
```

## USAGE

```sql
SELECT TRINO_HLL_CARDINALITY(from_binary(YOUR_HLL_COLUMN, 'encode64'))
SELECT TRINO_HLL_MERGE(from_binary(YOUR_HLL_COLUMN, 'encode64'))

--- you can combine them

select trino_hll_cardinality(trino_hll_merge(from_binary(my_hll_column, 'encode64'))) as unique_count from `glue_catalog`...
```
