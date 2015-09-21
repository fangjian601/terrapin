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
package com.pinterest.terrapin.zookeeper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pinterest.terrapin.TerrapinUtil;
import com.pinterest.terrapin.thrift.generated.Options;
import com.pinterest.terrapin.thrift.generated.PartitionerType;

import org.apache.commons.lang.ObjectUtils;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * The information as stored for a file set inside zookeeper. We store these
 * as json strings. This provides the bridge between logical names such as "recommendation_data"
 * etc. to actual files on HDFS. File sets refer to a collection of versions of a set of
 * (h)files. Each version of the file set is an HDFS directory. The FileSetInfo
 * contains information such as the current serving HDFS directory, the number of
 * partitions in the current serving HDFS directory and the set of HDFS directories
 * to be retained through garbage collection etc. This information is used by the
 * controller to determine which HDFS directories/helix resources need to be cleaned up
 * or retained. Its also used by the client to quickly find the active serving HDFS
 * resource for a file set and find the partitioner type/number of partitions etc.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileSetInfo {
  private static final Logger LOG = LoggerFactory.getLogger(FileSetInfo.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ServingInfo {
    @JsonProperty("hdfsPath")
    public String hdfsPath;
    @JsonProperty("helixResource")
    public String helixResource;
    @JsonProperty("numPartitions")
    public int numPartitions;
    @JsonProperty("partitionerType")
    public PartitionerType partitionerType;

    @JsonCreator
    public ServingInfo(@JsonProperty("hdfsPath") String hdfsPath,
                       @JsonProperty("helixResource") String helixResource,
                       @JsonProperty("numPartitions") int numPartitions,
                       @JsonProperty("partitionerType") PartitionerType partitionerType) {
      this.hdfsPath = Preconditions.checkNotNull(hdfsPath);
      this.helixResource = Preconditions.checkNotNull(helixResource);
      this.numPartitions = numPartitions;
      this.partitionerType = partitionerType;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ServingInfo)) {
        return false;
      }
      ServingInfo s = (ServingInfo)o;
      return ObjectUtils.equals(hdfsPath, s.hdfsPath) &&
             ObjectUtils.equals(helixResource, s.helixResource) &&
             s.numPartitions == this.numPartitions &&
             s.partitionerType == this.partitionerType;
    }
  }

  public String fileSetName;
  public int numVersionsToKeep;
  // The current active HDFS path which is serving.
  public ServingInfo servingInfo;

  // List of HDFS paths which have made it into serving. This
  // value depends on @numVersionsToKeep. It lists the earlier versions
  // which were being served.
  public List<ServingInfo> oldServingInfoList;

  // Sometimes, transient objects may be created
  public boolean valid;

  // For Garbage Collection
  public boolean deleted;

  // For storing user defined Data
  public String userDefinedData;

  public FileSetInfo() {
    this.oldServingInfoList = Lists.newArrayList();
    this.valid = false;
    this.deleted = false;
  }

  public FileSetInfo(String fileSetName,
                     String hdfsDir,
                     int numPartitions,
                     List<ServingInfo> oldServingInfoList,
                     Options options) {
    this(fileSetName, hdfsDir, numPartitions, oldServingInfoList, options, null);
  }

  public FileSetInfo(String fileSetName,
                     String hdfsDir,
                     int numPartitions,
                     List<ServingInfo> oldServingInfoList,
                     Options options,
                     String userDefinedData) {
    this.fileSetName = fileSetName;
    this.numVersionsToKeep = options.getNumVersionsToKeep();
    this.oldServingInfoList = oldServingInfoList;
    this.servingInfo = new ServingInfo(hdfsDir,
        TerrapinUtil.hdfsDirToHelixResource(hdfsDir),
        numPartitions,
        options.getPartitioner());
    this.valid = true;
    this.deleted = false;
    this.userDefinedData = userDefinedData;
  }

  public byte[] toJson() throws Exception {
    return OBJECT_MAPPER.writeValueAsBytes(this);
  }

  /**
   * To pretty printing JSON format.
   * @return JSON string
   * @throws IOException if dumping process raises any exceptions
   */
  public String toPrettyPrintingJson() throws IOException {
    return OBJECT_MAPPER.defaultPrettyPrintingWriter().writeValueAsString(this);
  }

  public static FileSetInfo fromJson(byte[] json) throws Exception {
    return OBJECT_MAPPER.readValue(json, FileSetInfo.class);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof FileSetInfo)) {
      return false;
    }
    FileSetInfo f = (FileSetInfo)o;
    return ObjectUtils.equals(fileSetName, f.fileSetName) &&
           f.numVersionsToKeep == this.numVersionsToKeep &&
           ObjectUtils.equals(servingInfo, f.servingInfo) &&
           ObjectUtils.equals(oldServingInfoList, f.oldServingInfoList) &&
           f.deleted == this.deleted &&
           ObjectUtils.equals(userDefinedData, f.userDefinedData);
  }
}
