package com.intuit.maven.extensions.build.scanner.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class SessionProfile {
  @NonNull private final String id;
  @NonNull private final Project project;
  @NonNull private final String command, hostname, username;
  @NonNull private final List<String> goals;
  private final String branch;
  private final List<ProjectProfile> projectProfiles = new ArrayList<>();
  private long startTime, endTime;
  @NonNull private Status status;

  public void addProjectProfile(ProjectProfile projectProfile) {
    projectProfiles.add(projectProfile);
  }

  public ProjectProfile getProjectProfile(Project project) {
    return projectProfiles.stream()
        .filter(candidate -> candidate.getProject().equals(project))
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
