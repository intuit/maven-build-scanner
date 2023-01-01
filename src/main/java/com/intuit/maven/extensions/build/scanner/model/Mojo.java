package com.intuit.maven.extensions.build.scanner.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mojo {
  @NonNull String groupId, artifactId;
  @NonNull @EqualsAndHashCode.Exclude String version;
}
