package com.intuit.maven.extensions.build.scanner;

import java.util.Collections;
import java.util.List;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;

class TestExecutionEvent implements ExecutionEvent {
  
  private final ExecutionEvent.Type type;
  private final MavenProject project;
  private final MojoExecution mojoExecution;
  private final MavenSession mavenSession =
      new MavenSession(
          null, null, new DefaultMavenExecutionRequest(), new DefaultMavenExecutionResult());

  private final List<MavenProject> projects;

  {
    mavenSession.setProjectDependencyGraph(
        new ProjectDependencyGraph() {
          @Override
          public List<MavenProject> getAllProjects() {
            return null;
          }

          @Override
          public List<MavenProject> getSortedProjects() {
            return projects;
          }

          @Override
          public List<MavenProject> getDownstreamProjects(
              MavenProject project, boolean transitive) {
            return null;
          }

          @Override
          public List<MavenProject> getUpstreamProjects(MavenProject project, boolean transitive) {
            return null;
          }
        });
  }

  TestExecutionEvent(Type type, MavenProject project) {
    this(type, project, null);
  }

  TestExecutionEvent(Type type, MavenProject project, MojoExecution mojoExecution) {
    this(type, project, mojoExecution, Collections.singletonList(project));
  }

  TestExecutionEvent(
      Type type, MavenProject project, MojoExecution mojoExecution, List<MavenProject> projects) {
    this.type = type;
    this.project = project;
    this.mojoExecution = mojoExecution;
    this.projects = projects;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public MavenSession getSession() {
    return mavenSession;
  }

  @Override
  public MavenProject getProject() {
    return project;
  }

  @Override
  public MojoExecution getMojoExecution() {
    return mojoExecution;
  }

  @Override
  public Exception getException() {
    return null;
  }
}
