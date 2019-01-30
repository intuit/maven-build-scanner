package com.intuit.maven.extensions.build.scanner.infra;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Indexes.ascending;
import static com.mongodb.client.model.Indexes.compoundIndex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intuit.maven.extensions.build.scanner.model.Project;
import com.intuit.maven.extensions.build.scanner.model.ProjectSummary;
import com.intuit.maven.extensions.build.scanner.model.SessionProfile;
import com.intuit.maven.extensions.build.scanner.model.SessionSummary;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOptions;
import java.util.Optional;
import org.bson.Document;

public class MongoDataStorage implements DataStorage {
  private static final ReplaceOptions UPSERT = new ReplaceOptions().upsert(true);
  private final ObjectMapper objectMapper =
      new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
  private final MongoClient mongoClient =
      MongoClients.create(
          Optional.ofNullable(System.getenv("MAVEN_BUILD_SCANNER_DB"))
              .orElse("mongodb://localhost/build_scans?serverSelectionTimeoutMS=1000"));
  private final MongoDatabase database = mongoClient.getDatabase("build_scans");
  private final MongoCollection<Document> projectSummariesCollection =
      database.getCollection("project_summaries");
  private final MongoCollection<Document> sessionProfilesCollection =
      database.getCollection("session_profiles");
  private final MongoCollection<Document> sessionSummariesCollection =
      database.getCollection("session_summaries");
  private final SessionProfile sessionProfile;
  private final Project project;

  public MongoDataStorage(SessionProfile sessionProfile) {
    this.sessionProfile = sessionProfile;
    this.project = sessionProfile.getProject();
  }

  @Override
  public void checkPoint() {
    sessionProfilesCollection.replaceOne(
        eq("id", sessionProfile.getId()), document(sessionProfile), UPSERT);
  }

  private Document document(Object value) {
    try {
      return Document.parse(objectMapper.writeValueAsString(value));
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void open() {
    sessionProfilesCollection.createIndex(
        compoundIndex(
            ascending("project.groupId"), ascending("project.artifactId"), ascending("id")),
        new IndexOptions().unique(true));
    checkPoint();

    sessionSummariesCollection.createIndex(
        compoundIndex(
            ascending("project.groupId"), ascending("project.artifactId"), ascending("id")),
        new IndexOptions().unique(true));
    updateProjectSummaries();

    projectSummariesCollection.createIndex(
        compoundIndex(ascending("groupId"), ascending("artifactId")),
        new IndexOptions().unique(true));
    updateSessionProfileSummaries();
  }

  private void updateProjectSummaries() {
    projectSummariesCollection.replaceOne(
        and(eq("groupId", project.getGroupId()), eq("artifactId", project.getArtifactId())),
        document(getProjectSummary()),
        UPSERT);
  }

  private ProjectSummary getProjectSummary() {
    return new ProjectSummary(project.getGroupId(), project.getArtifactId(), getSessionSummary());
  }

  private SessionSummary getSessionSummary() {
    return SessionSummary.builder()
        .id(sessionProfile.getId())
        .project(project)
        .hostname(sessionProfile.getHostname())
        .username(sessionProfile.getUsername())
        .startTime(sessionProfile.getStartTime())
        .duration(sessionProfile.getDuration())
        .goals(sessionProfile.getGoals())
        .branch(sessionProfile.getBranch())
        .status(sessionProfile.getStatus())
        .build();
  }

  @Override
  public void close() {
    checkPoint();
    updateSessionProfileSummaries();
    updateProjectSummaries();
    mongoClient.close();
  }

  private void updateSessionProfileSummaries() {
    sessionSummariesCollection.replaceOne(
        eq("id", sessionProfile.getId()), document(getSessionSummary()), UPSERT);
  }
}
