package com.intuit.maven.extensions.build.scanner.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value public class Project {
  @NonNull String groupId, artifactId;
  @NonNull @EqualsAndHashCode.Exclude String version;

  public String getId() {
    return groupId + ":" + artifactId;
  }
}
