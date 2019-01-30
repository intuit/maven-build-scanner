package com.intuit.maven.extensions.build.scanner.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
public class Project {
  @NonNull private final String groupId, artifactId;
  @NonNull @EqualsAndHashCode.Exclude private final String version;

  public String getId() {
    return groupId + ":" + artifactId;
  }
}
