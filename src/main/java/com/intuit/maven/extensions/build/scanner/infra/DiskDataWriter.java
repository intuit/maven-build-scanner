package com.intuit.maven.extensions.build.scanner.infra;

import com.intuit.maven.extensions.build.scanner.model.Project;
import com.intuit.maven.extensions.build.scanner.model.SessionProfile;

public class DiskDataWriter implements DataWriter {
  private final SessionProfile sessionProfile;
  private final Project project;
  private final DiskDataStorage aux = new DiskDataStorage();

  public DiskDataWriter(SessionProfile sessionProfile) {
    this.sessionProfile = sessionProfile;
    this.project = sessionProfile.getProject();
  }

  @Override
  public void open() {
    aux.open();
    aux.updateSessionSummary(sessionProfile);
  }

  @Override
  public void checkPoint() {
    aux.updateSessionProfile(sessionProfile);
  }

  @Override
  public void close() {
    aux.updateProjectSummary(project, sessionProfile);
    aux.updateSessionSummary(sessionProfile);
    aux.updateSessionProfile(sessionProfile);
  }
}
