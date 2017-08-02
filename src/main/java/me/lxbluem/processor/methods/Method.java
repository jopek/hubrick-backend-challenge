package me.lxbluem.processor.methods;

import java.util.List;

public interface Method<TYPE, RESULT> {
  RESULT get(List<TYPE> elements);
}
