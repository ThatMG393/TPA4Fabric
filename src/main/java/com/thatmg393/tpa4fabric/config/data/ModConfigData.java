package com.thatmg393.usefulhuds.config.data;

public class ModConfigData {
    public class FPSSection {
        public boolean visible = true;
        public int textColor = 0xFFFFFF;

        public int offsetX = 20;
        public int offsetY = 20;
        public float scale = 1.0f;

        public class AdvancedSection {
            public boolean showAdvanceInfo = false;
            public int histroyMax = 60;
        }

        public final AdvancedSection ADVANCED = new AdvancedSection();
    }

    // STD (Sprint toggle display)
    public class STDSection {
        public boolean visible = false;
        public int textColor = 0xFFFFFF;

        public int offsetX = 20;
        public int offsetY = 30;
        public float scale = 1.0f;

        public class AdvancedSection { }

        public final AdvancedSection ADVANCED = new AdvancedSection();
    }

    public class CoordsSection {
        public boolean visible = true;
    }

    public final FPSSection FPS = new FPSSection();
    public final STDSection STD = new STDSection();
    public final CoordsSection COORDS = new CoordsSection();
}