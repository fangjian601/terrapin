/**
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.pinterest.terrapin;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Constants for terrapin.
 */
public class Constants {
  public static final String ZOOKEEPER_QUORUM = "zookeeper_quorum";
  public static final String ZOOKEEPER_QUORUM_DELIMITER = ",";
  public static final String HDFS_NAMENODE = "hdfs_namenode";

  public static final String HELIX_CLUSTER = "helix_cluster";
  public static final String HELIX_CLUSTER_NAME_DEFAULT = "terrapin";

  // Constants related to the thrift server (controller or server).
  public static final String THRIFT_PORT = "thrift_port";
  public static final int DEFAULT_THRIFT_PORT = 9090;
  public static final String THRIFT_CONN_MAX_IDLE_TIME = "thrift_conn_max_idle_time";
  public static final String THRIFT_MAX_CONCURRENT_REQUESTS = "thrift_max_concurrent_requests";

  // Port for exposing ostrich based metrics.
  public static final String OSTRICH_METRICS_PORT = "ostrich_metrics_port";

  // File prefix used for writing files and identifying them.
  public static final String FILE_PREFIX = "part-";

  // Number of threads for the thread pool on which read operations execute.
  public static final String READER_THREAD_POOL_SIZE = "reader_thread_pool_size";

  // Parameters for HFile format.
  public static final byte[] HFILE_COLUMN_FAMILY = Bytes.toBytes("cf");

  // Threshold for how much minimum deviation, we need to have for a new
  // ideal state to be written out for a resource.
  public static final String REBALANCE_DEVIATION_THRESHOLD = "rebalancer_deviation_threshold";

  // Interval at which the rebalancer and garbage collection thread runs.
  public static final String REBALANCE_INTERVAL_SECONDS = "rebalance_interval_seconds";

  // Root directory where data is stored.
  public static final String HDFS_DATA_DIR = "/terrapin/data";

  // HDFS replication.
  public static final String HDFS_REPLICATION = "hdfs_replication";
  public static final int DEFAULT_HDFS_REPLICATION = 3;

  // Number of serving replicas, default is 3 to match the HDFS replication factor.
  public static final String NUM_SERVING_REPLICAS = "num_serving_replicas";

  // Configuration for client side settings used in the thrift server.
  public static final String CLIENT_CONNECT_TIMEOUT_MILLIS = "client_connect_timeout_millis";
  public static final String CLIENT_RPC_TIMEOUT_MILLIS = "client_rpc_timeout_millis";
  // The target port the servers are listening on. This is used by the client to connect
  // appropriately.
  public static final String TERRAPIN_SERVER_TARGET_PORT = "terrapin_server_target_port";

  public static final String HELIX_WEBAPP_PORT = "helix_webapp_port";

  // Cluster status web server binding address
  public static final String STATUS_SERVER_BINDING_ADDRESS = "status_server_binding_address";

  // Cluster status web server binding port
  public static final String STATUS_SERVER_BINDING_PORT = "status_server_binding_port";

  // The percentage of heap to devote to the block cache.
  public static final String HFILE_BLOCK_CACHE_HEAP_PCT = "hfile_block_cache_heap_pct";

  // Maximum number of allowed shards that we can upload under a given fileset.
  public static final int MAX_ALLOWED_SHARDS = 10000;

  // Amount of time to wait for loading of a copied data set.
  public static final int LOAD_TIMEOUT_SECONDS = 120;

  // Frequency with which the compressed view is refreshed.
  public static final int VIEW_INFO_REFRESH_INTERVAL_SECONDS_DEFAULT = 15;
  
  // Frequency with which the gauge uploader is executed.
  public static final int GAUGE_MANAGER_EXEC_INTERVAL_SECONDS_DEFAULT = 60;

  // Default max size of a single HFile shard in bytes. Configured at 4G.
  public static final long DEFAULT_MAX_SHARD_SIZE_BYTES = 4L * 1024L * 1024L * 1024L;

  // Configuration parameters for HFiles such as compression, block size etc.
  public static final String HFILE_COMPRESSION = "hfile.compression";
  public static final String HFILE_COMPRESSION_DEFAULT = "NONE";
  public static final String HFILE_BLOCKSIZE = "hfile.blocksize";
  public static final int HFILE_BLOCKSIZE_DEFAULT = 16384;

  // Whether cluster state kept in ZK is gzip compressed by Helix.
  public static final String ENABLE_ZK_COMPRESSION = "enable_zk_compression";
  public static final boolean ENABLE_ZK_COMPRESSION_DEFAULT = false;

  // Maximum number of attempts for mapreduce mapper tasks
  public static final int MAPRED_MAP_MAX_ATTEMPTS = 10;

  // The number of bytes per checksum
  public static final int CHECKSUM_BYTES = 4096;
}
