package com.fuyouj.sword.database.object.audit;

import java.time.LocalDateTime;

import com.fuyouj.sword.scabard.DateTimes2;

public class DefaultAuditProvider implements AuditProvider {
    public static final String DEFAULT_AUDITOR = "System";

    @Override
    public String getAuditor() {
        return DEFAULT_AUDITOR;
    }

    @Override
    public LocalDateTime getNow() {
        return DateTimes2.localNow();
    }
}
