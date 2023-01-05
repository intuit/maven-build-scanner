package com.intuit.maven.extensions.build.scanner;

import static java.lang.System.currentTimeMillis;

import com.intuit.maven.extensions.build.scanner.infra.DataWriter;
import com.intuit.maven.extensions.build.scanner.infra.DiskDataWriter;
import com.intuit.maven.extensions.build.scanner.model.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Singleton
public class LifecycleProfiler extends AbstractEventSpy {
  private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleProfiler.class.getName());
  private final AtomicInteger threadIndexGenerator = new AtomicInteger();
  private final ThreadLocal<Integer> threadIndex =
      ThreadLocal.withInitial(threadIndexGenerator::incrementAndGet);
  private final Function<SessionProfile, DataWriter> dataStorageFactory;
  private DataWriter dataWriter;
  private SessionProfile sessionProfile;
  private long lastCheckPoint = currentTimeMillis();

  public LifecycleProfiler() {
    this(DiskDataWriter::new);
  }

  LifecycleProfiler(Function<SessionProfile, DataWriter> dataStorageFactory) {
    this.dataStorageFactory = dataStorageFactory;
  }

  private static Project project(MavenProject mavenProject) {
    return new Project(
        mavenProject.getGroupId(), mavenProject.getArtifactId(), mavenProject.getVersion());
  }

  @Override
  public void init(Context context) {}

  private static String hostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "unknown";
    }
  }

  @Override
  public synchronized void onEvent(Object event) {

    if (event instanceof ExecutionEvent) {
      ExecutionEvent executionEvent = (ExecutionEvent) event;
      MavenProject mavenProject = executionEvent.getProject();

      switch (executionEvent.getType()) {
        case SessionStarted:
          {
            MavenSession session = executionEvent.getSession();
            // TODO its rather nice having the report based on the first project run, rather than
            // the top-level project
            // as that indicates quite a different build occurring
            Project project = project(mavenProject);
            String tag = getBranch(mavenProject);

            sessionProfile =
                new SessionProfile(
                    UUID.randomUUID().toString(),
                    project,
                    command(session.getRequest()),
                    hostname(),
                    System.getProperty("user.name"),
                    session.getGoals(),
                    tag,
                    new LinkedList<>(),
                    0,
                    0,
                    0,
                    Status.PENDING);
            sessionProfile.setStartTime(currentTimeMillis());

            LOGGER.info(
                "Creating Maven build scanner session profile "
                    + sessionProfile.getProject().getId()
                    + "#"
                    + sessionProfile.getId());

            ProjectDependencyGraph projectDependencyGraph = session.getProjectDependencyGraph();
            List<MavenProject> sortedProjects = projectDependencyGraph.getSortedProjects();
            sortedProjects.forEach(
                dependency -> {
                  Project childProject =
                      new Project(
                          dependency.getGroupId(),
                          dependency.getArtifactId(),
                          dependency.getVersion());
                  sessionProfile.addProjectProfile(
                      new ProjectProfile(childProject, new ArrayList<>(), Status.PENDING, 0, 0, 0));
                });

            dataWriter = dataStorageFactory.apply(sessionProfile);
            dataWriter.open();
            break;
          }
        case SessionEnded:
          {
            MavenSession session = executionEvent.getSession();
            sessionProfile.setEndTime(currentTimeMillis());

            if (session.getResult().hasExceptions()) {
              sessionProfile.setStatus(Status.FAILED);
            } else {
              sessionProfile.setStatus(Status.SUCCEEDED);
            }

            dataWriter.close();

            String mavenHome = session.getSystemProperties().getProperty("maven.home");

            LOGGER.info("To view your build scan, start-up the server:");
            LOGGER.info(
                "java -jar {}",
                mavenHome
                    + File.separator
                    + "lib"
                    + File.separator
                    + "ext"
                    + File.separator
                    + "maven-build-scanner-jar-with-dependencies.jar");
            LOGGER.info(
                "Then open http://localhost:3000/?projectId={}&sessionId={}",
                sessionProfile.getProject().getId(),
                sessionProfile.getId());
            break;
          }
        case ProjectStarted:
          {
            sessionProfile
                .getProjectProfile(project(mavenProject))
                .setStartTime(currentTimeMillis());
            break;
          }
        case ProjectSucceeded:
        case ProjectFailed:
          {
            ProjectProfile projectProfile = sessionProfile.getProjectProfile(project(mavenProject));
            projectProfile.setStatus(
                Status.valueOf(executionEvent.getType().name().toUpperCase().substring(7)));
            projectProfile.setEndTime(currentTimeMillis());
            sessionProfile.setEndTime(currentTimeMillis());
            maybeCheckPoint();
            break;
          }
        case MojoStarted:
          {
            Project project = project(mavenProject);
            MojoExecution mojoExecution = executionEvent.getMojoExecution();

            ProjectProfile projectProfile = sessionProfile.getProjectProfile(project);
            MojoProfile mojoProfile =
                new MojoProfile(
                    mojo(mojoExecution),
                    mojoExecution.getExecutionId(),
                    mojoExecution.getGoal(),
                    Status.PENDING,
                    threadIndex.get(),
                    0,
                    0,
                    0);
            mojoProfile.setStartTime(currentTimeMillis());

            projectProfile.addMojoProfile(mojoProfile);
            break;
          }
        case MojoSucceeded:
        case MojoFailed:
          {
            Project project = project(mavenProject);
            MojoExecution mojoExecution = executionEvent.getMojoExecution();
            ProjectProfile projectProfile = sessionProfile.getProjectProfile(project);

            MojoProfile mojoProfile =
                projectProfile.getMojoProfile(
                    mojo(mojoExecution), mojoExecution.getExecutionId(), mojoExecution.getGoal());
            mojoProfile.setEndTime(currentTimeMillis());
            mojoProfile.setStatus(
                Status.valueOf(executionEvent.getType().name().toUpperCase().substring(4)));
          }
          break;
      }
    }
  }

  private void maybeCheckPoint() {
    if (currentTimeMillis() - lastCheckPoint > 30_0000) {
      LOGGER.info("Requesting check-point");
      dataWriter.checkPoint();
      lastCheckPoint = currentTimeMillis();
    }
  }

  private String getBranch(MavenProject mavenProject) {

    try {
      File basedir = mavenProject.getBasedir();
      File gitHead;
      do {
        gitHead = new File(basedir, ".git/HEAD");
        basedir = basedir.getParentFile();
      } while (basedir != null && !gitHead.exists());

      if (!gitHead.exists()) {
        return "&lt;git not found&gt;";
      }

      //noinspection OptionalGetWithoutIsPresent
      return Files.readAllLines(gitHead.toPath()).stream()
          .map(line -> line.replaceFirst(".*/", ""))
          .findFirst()
          .get();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private String command(MavenExecutionRequest request) {
    List<String> out = new ArrayList<>();

    out.add("mvn");

    out.add("-s " + request.getUserSettingsFile());

    out.add("-T " + request.getDegreeOfConcurrency());

    request.getActiveProfiles().stream().map(profile -> "-P" + profile).forEach(out::add);

    request.getUserProperties().entrySet().stream()
        .map(entry -> "-D" + entry.getKey() + "=" + entry.getValue())
        .forEach(out::add);

    out.addAll(request.getGoals());

    return String.join(" ", out);
  }

  private Mojo mojo(MojoExecution mojoExecution) {
    return new Mojo(
        mojoExecution.getGroupId(), mojoExecution.getArtifactId(), mojoExecution.getVersion());
  }
}
