package com.intuit.maven.extensions.build.scanner.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionProfile {
  @NonNull private String id;
  @NonNull private Project project;
  @NonNull private String command, hostname, username;
  @NonNull private List<String> goals;
  @NonNull private String branch;
  @NonNull private List<ProjectProfile> projectProfiles;
  private long startTime, endTime, duration;
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

  public void setEndTime(long endTime) {
    this.endTime = endTime;
    this.duration = endTime - startTime;
  }
}
