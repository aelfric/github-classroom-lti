package com.frankriccobono;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Classroom will automatically create a pull request for instructor feedback.  This will
 * construct that link from student's repo URL
 *
 * @param repository
 * @param host
 */
public record GithubUrl(String repository, String host) {
  public String toFeedbackPr() {
    return "https://" + host  + repository + "/pull/1";
  }

  static GithubUrl parseUrl(String url) {
    final URI uri = URI.create(url);

    String repository = Arrays.stream(
            uri.getPath().split("/")
        )
        .map(seg -> {
          if (seg.endsWith(".git")) {
            return seg.substring(0, seg.length() - ".git".length());
          } else {
            return seg;
          }
        })
        .limit(3)
        .collect(Collectors.joining("/"));


    return new GithubUrl(repository, uri.getHost());

  }
}
