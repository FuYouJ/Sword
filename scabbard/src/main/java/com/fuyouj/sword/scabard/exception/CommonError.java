package com.fuyouj.sword.scabard.exception;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonError {

    // 错误详细
    private List<Object> detail;

    public CommonError(final List<Object> detail) {
        this.detail = detail;
    }
}
