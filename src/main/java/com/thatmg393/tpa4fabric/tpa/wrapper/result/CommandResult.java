package com.thatmg393.tpa4fabric.tpa.wrapper.result;

import java.util.Optional;

public enum CommandResult {
    SUCCESS,

    TPA_SELF,
    ON_COOLDOWN,

    HAS_EXISTING,
    NO_REQUEST,
    EMPTY_REQUESTS,

    NOT_ALLOWED,

    IGNORE,

    NO_PREVIOUS_COORDS;

    private Object extraData = null;

    private CommandResult() {

    }

    public void withExtraData(Object any) {
        this.extraData = any;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getExtraData() {
        return extraData == null ? Optional.empty() : Optional.of((T) extraData);
    }
}
