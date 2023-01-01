package com.intuit.maven.extensions.build.scanner.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intuit.maven.extensions.build.scanner.model.Project;
import com.intuit.maven.extensions.build.scanner.model.ProjectSummary;
import com.intuit.maven.extensions.build.scanner.model.SessionProfile;
import com.intuit.maven.extensions.build.scanner.model.SessionSummary;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import lombok.SneakyThrows;

public class DiskDataStorage {
  private static final String RESULTS = "target" + File.separator + "scans";
  private static final String SESSION_SUMMARIES = RESULTS + File.separator + "session-summaries";
  private static final String PROJECT_SUMMARIES = RESULTS + File.separator + "project-summaries";
  private static final String SESSION_PROFILES = RESULTS + File.separator + "session-profiles";
  private static final ObjectMapper MAPPER =
      new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

  void open() {
    new File(SESSION_PROFILES).mkdirs();
    new File(PROJECT_SUMMARIES).mkdirs();
    new File(SESSION_SUMMARIES).mkdirs();
  }

  @SneakyThrows
  void updateProjectSummary(Project project, SessionProfile sessionProfile) {
    String path =
        PROJECT_SUMMARIES
            + File.separator
            + project.getGroupId()
            + "-"
            + project.getArtifactId()
            + ".json";
    MAPPER.writeValue(
        new File(path),
        new ProjectSummary(
            project.getGroupId(), project.getArtifactId(), getSessionSummary(sessionProfile)));
  }

  private SessionSummary getSessionSummary(SessionProfile sessionProfile) {
    return new SessionSummary(
        sessionProfile.getId(),
        sessionProfile.getProject(),
        sessionProfile.getStartTime(),
        sessionProfile.getDuration(),
        sessionProfile.getBranch(),
        sessionProfile.getUsername(),
        sessionProfile.getGoals(),
        sessionProfile.getHostname(),
        sessionProfile.getStatus());
  }

  @SneakyThrows
  void updateSessionSummary(SessionProfile sessionProfile) {
    String path = SESSION_SUMMARIES + File.separator + sessionProfile.getId() + ".json";
    MAPPER.writeValue(new File(path), getSessionSummary(sessionProfile));
  }

  @SneakyThrows
  void updateSessionProfile(SessionProfile sessionProfile) {
    String path = SESSION_PROFILES + File.separator + sessionProfile.getId() + ".json";
    MAPPER.writeValue(new File(path), sessionProfile);
  }

  @SneakyThrows
  public List<ProjectSummary> listProjectSummaries() {
    List<ProjectSummary> items = new LinkedList<>();
    for (File file : new File(PROJECT_SUMMARIES).listFiles()) {
      items.add(MAPPER.readValue(file, ProjectSummary.class));
    }
    return items;
  }

  @SneakyThrows
  public List<SessionSummary> listSessionSummaries(String groupId, String artifactId) {
    List<SessionSummary> items = new LinkedList<>();
    for (File file : new File(SESSION_SUMMARIES).listFiles()) {
      SessionSummary item = MAPPER.readValue(file, SessionSummary.class);
      if (item.getProject().getGroupId().equals(groupId)
          && item.getProject().getArtifactId().equals(artifactId)) {
        items.add(item);
      }
    }
    return items;
  }

  @SneakyThrows
  public SessionProfile getSessionProfile(String id) {
    return MAPPER.readValue(
        new File(SESSION_PROFILES + File.separator + id + ".json"), SessionProfile.class);
  }
}
