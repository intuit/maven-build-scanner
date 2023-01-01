package com.intuit.maven.extensions.build.scanner.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectProfile {
  @NonNull private Project project;
  @NonNull private List<MojoProfile> mojoProfiles;
  @NonNull private Status status;
  private long startTime, endTime, duration;

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

  public void setEndTime(long endTime) {
    this.endTime = endTime;
    this.duration = endTime - startTime;
  }
}
