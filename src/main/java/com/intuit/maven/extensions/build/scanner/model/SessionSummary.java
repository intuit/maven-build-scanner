package com.intuit.maven.extensions.build.scanner.model;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class SessionSummary {
  @NonNull private final String id;
  @NonNull private final Project project;
  private final long startTime, duration;
  @NonNull private final String branch, username;
  @NonNull private final List<String> goals;
  private final String hostname;
  @NonNull private final Status status;
}
