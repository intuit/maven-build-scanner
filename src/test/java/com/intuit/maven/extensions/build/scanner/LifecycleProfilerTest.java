package com.intuit.maven.extensions.build.scanner;

import static org.apache.maven.execution.ExecutionEvent.Type.MojoFailed;
import static org.apache.maven.execution.ExecutionEvent.Type.MojoStarted;
import static org.apache.maven.execution.ExecutionEvent.Type.MojoSucceeded;
import static org.apache.maven.execution.ExecutionEvent.Type.ProjectFailed;
import static org.apache.maven.execution.ExecutionEvent.Type.ProjectStarted;
import static org.apache.maven.execution.ExecutionEvent.Type.ProjectSucceeded;
import static org.apache.maven.execution.ExecutionEvent.Type.SessionEnded;
import static org.apache.maven.execution.ExecutionEvent.Type.SessionStarted;

import com.intuit.maven.extensions.build.scanner.infra.DataStorage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class LifecycleProfilerTest {

  private final LifecycleProfiler sessionProfileRenderer =
      new LifecycleProfiler(
          sessionProfile ->
              new DataStorage() {
                @Override
                public void open() {}

                @Override
                public void checkPoint() {}

                @Override
                public void close() {}
              });

  private static MojoExecution mojoExecution(String test) {
    Plugin p = new Plugin();
    p.setGroupId("test");
    p.setArtifactId(test);
    p.setVersion("1");
    MojoExecution m = new MojoExecution(p, "test", "test");
    m.setLifecyclePhase("test");
    return m;
  }

  private static MavenProject project(String artifactId) {
    MavenProject p = new MavenProject();
    p.setGroupId("test");
    p.setArtifactId(artifactId);
    p.setVersion("1");
    p.setFile(new File("./pom.xml"));
    return p;
  }

  @Test
  public void testSessionProfile() throws Exception {
    MavenProject projectA = project("test-a");
    MavenProject projectB = project("test-b");
    MojoExecution mojoA = mojoExecution("test-a");
    MojoExecution mojoB = mojoExecution("test-b");
    List<MavenProject> projects = Arrays.asList(projectA, projectB);
    for (ExecutionEvent event :
        new ExecutionEvent[] {
          new TestExecutionEvent(SessionStarted, projectA, null, projects),
          new TestExecutionEvent(ProjectStarted, projectA),
          new TestExecutionEvent(MojoStarted, projectA, mojoA),
          new TestExecutionEvent(MojoSucceeded, projectA, mojoA),
          new TestExecutionEvent(ProjectSucceeded, projectA),
          new TestExecutionEvent(ProjectStarted, projectB),
          new TestExecutionEvent(MojoStarted, projectB, mojoB),
          new TestExecutionEvent(MojoFailed, projectB, mojoB),
          new TestExecutionEvent(ProjectFailed, projectB),
          new TestExecutionEvent(SessionEnded, projectA)
        }) {
      sessionProfileRenderer.onEvent(event);
      Thread.sleep(2);
    }
  }
}
