package com.intuit.maven.extensions.build.scanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MojoProfile {

  @NonNull private Mojo mojo;
  @NonNull private String executionId;
  @NonNull private String goal;
  @NonNull private Status status;
  private int threadIndex;
  private long startTime, endTime, duration;

  public void setStartTime(long startTime) {
    this.startTime = startTime;
    this.endTime = startTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
    this.duration = endTime - startTime;
  }
}
