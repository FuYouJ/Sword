package com.fuyouj.sword.scabard;

import lombok.Getter;

public enum Week {
    Monday(2);

    @Getter
    private int number;

    Week(final int number) {
        this.number = number;
    }
}
