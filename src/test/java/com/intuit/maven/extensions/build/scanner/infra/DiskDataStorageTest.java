package com.intuit.maven.extensions.build.scanner.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.maven.extensions.build.scanner.model.Project;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DiskDataStorageTest {
  private static final Project PROJECT = new Project("com.example", "example", "1.0");

  @Rule public final TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void readAllSkipsEmptyFileLeftByInterruptedBuild() throws IOException {
    writeJson("valid.json", PROJECT);
    folder.newFile("empty.json");

    final List<Project> projects = DiskDataStorage.readAll(folder.getRoot(), Project.class);

    assertEquals(Collections.singletonList(PROJECT), projects);
  }

  @Test
  public void readAllReturnsEmptyListForMissingDirectory() {
    final File missingDirectory = new File(folder.getRoot(), "missing");

    final List<Project> projects = DiskDataStorage.readAll(missingDirectory, Project.class);

    assertTrue(projects.isEmpty());
  }

  private void writeJson(final String fileName, final Object value) throws IOException {
    new ObjectMapper().writeValue(folder.newFile(fileName), value);
  }
}
