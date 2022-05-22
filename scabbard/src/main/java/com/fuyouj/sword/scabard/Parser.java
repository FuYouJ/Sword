package com.fuyouj.sword.scabard;

import java.time.LocalDateTime;
import java.util.Optional;

public interface Parser {
    Optional<LocalDateTime> tryParse(String dateString);
}
