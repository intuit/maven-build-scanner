package com.intuit.maven.extensions.build.scanner.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class ProjectSummary {
  @NonNull private final String groupId, artifactId;
  private final SessionSummary latestSessionSummary;

  public String getId() {
    return groupId + ":" + artifactId;
  }
}
