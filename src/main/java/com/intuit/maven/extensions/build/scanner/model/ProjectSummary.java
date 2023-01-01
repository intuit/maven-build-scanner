package com.intuit.maven.extensions.build.scanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSummary {
  @NonNull String groupId, artifactId;
  SessionSummary latestSessionSummary;
}
