package com.intuit.maven.extensions.build.scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intuit.maven.extensions.build.scanner.infra.DiskDataStorage;
import com.intuit.maven.extensions.build.scanner.model.ProjectSummary;
import com.intuit.maven.extensions.build.scanner.model.SessionProfile;
import com.intuit.maven.extensions.build.scanner.model.SessionSummary;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class Server {

  private static final ObjectMapper MAPPER =
      new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

  public static void main(String[] args) throws IOException {
    DiskDataStorage diskDataStorage = new DiskDataStorage();
    HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
    server.createContext(
        "/",
        t -> {
          try (InputStream in = Server.class.getClassLoader().getResourceAsStream("index.html")) {
            t.sendResponseHeaders(200, 0);
            IOUtils.copy(in, t.getResponseBody());
          } catch (Throwable e) {
            t.sendResponseHeaders(500, e.toString().length());
            t.getResponseBody().write(e.toString().getBytes());
          } finally {
            t.close();
          }
        });
    server.createContext(
        "/api/v1/session-summaries",
        t -> {
          String projectId = t.getRequestURI().getPath().split("/")[4];
          String groupId = projectId.split(":")[0];
          String artifactId = projectId.split(":")[1];
          ;
          try {
            List<SessionSummary> items = diskDataStorage.listSessionSummaries(groupId, artifactId);
            t.sendResponseHeaders(200, 0);
            MAPPER.writeValue(t.getResponseBody(), items);
          } catch (Throwable e) {
            t.sendResponseHeaders(500, e.toString().length());
            t.getResponseBody().write(e.toString().getBytes());
          } finally {
            t.close();
          }
        });
    server.createContext(
        "/api/v1/session-profiles",
        t -> {
          String id = t.getRequestURI().getPath().split("/")[4];

          try {
            SessionProfile item = diskDataStorage.getSessionProfile(id);
            t.sendResponseHeaders(200, 0);
            MAPPER.writeValue(t.getResponseBody(), item);
          } catch (Throwable e) {
            t.sendResponseHeaders(500, e.toString().length());
            t.getResponseBody().write(e.toString().getBytes());
          } finally {
            t.close();
          }
        });
    server.createContext(
        "/api/v1/project-summaries",
        t -> {
          try {
            List<ProjectSummary> items = diskDataStorage.listProjectSummaries();
            t.sendResponseHeaders(200, 0);
            MAPPER.writeValue(t.getResponseBody(), items);
          } catch (Throwable e) {
            t.sendResponseHeaders(500, e.toString().length());
            t.getResponseBody().write(e.toString().getBytes());
          } finally {
            t.close();
          }
        });

    System.out.println("Open http://localhost:3000 to view build scan results");
    server.start();
  }
}
