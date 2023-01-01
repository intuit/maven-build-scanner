package com.intuit.maven.extensions.build.scanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
  @NonNull String groupId, artifactId;
  @NonNull @EqualsAndHashCode.Exclude String version;

  @JsonIgnore
  public String getId() {
    return groupId + ":" + artifactId;
  }
}
