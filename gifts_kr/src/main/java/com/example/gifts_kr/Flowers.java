package com.example.gifts_kr;

import java.util.Map;

public class Flowers extends Gifts  {
        @Override
        public String getName() { return "Цветочный магазин"; }

        @Override
        public Map<String, Integer> getGifts() {
            return Map.of(
                    "Букет из роз", 800,
                    "Букет из лилий", 1200,
                    "Экзотический букет", 2500
            );
        }
    }
