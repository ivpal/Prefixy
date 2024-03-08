package com.github.ivpal.prefixy.api;

import java.util.List;

public interface SuggestionStore {
    List<String> list(String prefix);
}
