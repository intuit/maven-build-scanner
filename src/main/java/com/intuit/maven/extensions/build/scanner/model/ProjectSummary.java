package com.intuit.maven.extensions.build.scanner.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class ProjectSummary {
  @NonNull String groupId, artifactId;
  SessionSummary latestSessionSummary;

  public String getId() {
    return groupId + ":" + artifactId;
  }
}
