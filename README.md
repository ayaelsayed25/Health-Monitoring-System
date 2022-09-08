# Health Monitoring System
Lambda Architecture | Java, Hadoop, Spark, DuckDB 


• Cloud Machines are used to maintain the HDFS cluster. 

• Health reports arriving from health monitor datasource will get persisted as raw data in the Batch Layer using HDFS. 

• Batch views are generated using Map-reduce jobs in parquet file format. 

• Realtime views are generated using spark in parquet file format. 

• Backend collects query results by contacting both speed layer and batch views to aggregate the results and stitch them together
using Duck DB. 
