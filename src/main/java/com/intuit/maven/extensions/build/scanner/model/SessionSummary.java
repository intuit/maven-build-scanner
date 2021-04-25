package com.intuit.maven.extensions.build.scanner.model;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class SessionSummary {
  @NonNull String id;
  @NonNull Project project;
  long startTime, duration;
  @NonNull String branch, username;
  @NonNull List<String> goals;
  String hostname;
  @NonNull Status status;
}
