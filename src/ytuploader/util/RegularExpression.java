package ytuploader.util;

import java.util.regex.Matcher;

public final class RegularExpression {
  public RegularExpression() {
    throw new AssertionError();
  }

  public static String replaceCallback(String input, Matcher matcher) {
    for (int i = 0; i <= matcher.groupCount(); i++) {
      input = input.replaceAll("(?<!\\\\)\\$" + i, matcher.group(i));
    }

    return input;
  }
}
