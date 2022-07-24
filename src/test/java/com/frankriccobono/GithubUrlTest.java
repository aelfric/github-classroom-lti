package com.frankriccobono;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class GithubUrlTest {

  @ParameterizedTest
  @CsvSource(
      value = {
          "https://github.com/SIT-EE552-WS/02-fibonacci-numbers-fasfar/pull/1,https://github.com/SIT-EE552-WS/02-fibonacci-numbers-fasfar/pull/1",
          "https://github.com/SIT-EE552-WS/02-fibonacci-numbers-Kiran20146,https://github.com/SIT-EE552-WS/02-fibonacci-numbers-Kiran20146/pull/1",
          "https://github.com/SIT-EE552-WS/02-fibonacci-numbers-svc12570.git/pull/1,https://github.com/SIT-EE552-WS/02-fibonacci-numbers-svc12570/pull/1",
          "https://github.com/SIT-EE552-WS/02-fibonacci-numbers-BriannaPGarland/blob/main/Fibonacci.java,https://github.com/SIT-EE552-WS/02-fibonacci-numbers-BriannaPGarland/pull/1",
          "https://github.com/SIT-EE552-WS/02-fibonacci-numbers-fasfar/blob/f63a003747b56b12d271cf043c51710299e4a03c/Fibonacci.java,https://github.com/SIT-EE552-WS/02-fibonacci-numbers-fasfar/pull/1"
      }
  )
  void testParse(String url, String feedbackUrl) {

    final GithubUrl githubUrl = GithubUrl.parseUrl(url);

    assertThat(githubUrl.toFeedbackPr(), is(feedbackUrl));
    ;
  }

}