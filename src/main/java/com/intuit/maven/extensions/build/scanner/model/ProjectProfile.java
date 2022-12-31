package com.intuit.maven.extensions.build.scanner.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NonNull;

@Data
public class ProjectProfile {
  @NonNull private final Project project;
  private final List<MojoProfile> mojoProfiles = new ArrayList<>();
  @NonNull private Status status;
  private long startTime, endTime;

  public void addMojoProfile(MojoProfile mojoProfile) {
    mojoProfiles.add(mojoProfile);
  }

  public MojoProfile getMojoProfile(Mojo mojo, String executionId, String goal) {
    return mojoProfiles.stream()
        .filter(
            mojoProfile ->
                mojoProfile.getMojo().equals(mojo)
                    && mojoProfile.getExecutionId().equals(executionId)
                    && mojoProfile.getGoal().equals(goal))
        .findFirst()
        .get();
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
    this.endTime = startTime;
  }

  public long getDuration() {
    return endTime - startTime;
  }
}
