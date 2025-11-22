package com.example.gifts_kr;

import java.util.Map;

public class Chocolate extends Gifts {
    @Override
    public String getName() { return "Кондитерская"; }

    @Override
    public Map<String, Integer> getGifts() {
        return Map.of(
                "Шоколад", 300,
                "Пирожные заварные", 1000,
                "Торт муссовый шоколад&карамель", 1800
        );
    }
}
