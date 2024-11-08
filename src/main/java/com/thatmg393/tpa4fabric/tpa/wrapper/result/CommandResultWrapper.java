package com.thatmg393.tpa4fabric.tpa.wrapper.result;

import java.util.Optional;

public record CommandResultWrapper<T>(CommandResult result, Optional<T> extraData) {
    public static CommandResultWrapper<Object> of(CommandResult result) {
        return new CommandResultWrapper<>(result, Optional.empty());
    }
    
    public static <T> CommandResultWrapper<T> of(CommandResult result, T extraData) {
        return new CommandResultWrapper<T>(result, Optional.of(extraData));
    }
}
