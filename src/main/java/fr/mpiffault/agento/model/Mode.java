package fr.mpiffault.agento.model;

import java.util.Arrays;
import java.util.List;

public enum Mode {
    RANDOM, FOLLOW_NEAREST;

    public static List<Mode> getModeList() {
        return Arrays.asList(RANDOM, FOLLOW_NEAREST);
    }

    public static Mode nextMode(Mode currentMode) {
        return getModeList().get((getModeList().indexOf(currentMode) + 1) % (getModeList().size()));
    }
}
