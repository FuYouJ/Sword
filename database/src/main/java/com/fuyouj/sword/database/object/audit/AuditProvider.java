package com.fuyouj.sword.database.object.audit;

import java.time.LocalDateTime;

/**
 * 实现该接口必须包含一个无参构造函数
 */
public interface AuditProvider {
    String getAuditor();

    LocalDateTime getNow();
}
