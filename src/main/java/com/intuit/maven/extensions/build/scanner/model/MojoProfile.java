package com.intuit.maven.extensions.build.scanner.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class MojoProfile {

  @NonNull private final Mojo mojo;
  @NonNull private final String executionId;
  @NonNull private final String goal;
  @NonNull private Status status;
  private final int threadIndex;
  private long startTime, endTime;

  public void setStartTime(long startTime) {
    this.startTime = startTime;
    this.endTime = startTime;
  }

  public long getDuration() {
    return endTime - startTime;
  }
}
