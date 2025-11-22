package com.example.gifts_kr;

import java.util.Map;

public class Juwerly extends Gifts {
    @Override
    public String getName() { return "Ювелирные украшения"; }

    @Override
    public Map<String, Integer> getGifts() {
        return Map.of(
                "Серьги", 2500,
                "Подвеска", 2000
        );
    }
}
