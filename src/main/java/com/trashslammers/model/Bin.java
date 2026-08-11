package com.trashslammers.model;

import com.trashslammers.model.TrashItem.WasteType;

public class Bin {
    private final WasteType type;

    public Bin(WasteType type) {
        this.type = type;
    }

    public WasteType getType() {
        return type;
    }

    public boolean accepts(TrashItem item) {
        return item.getCorrectBin() == type;
    }
}