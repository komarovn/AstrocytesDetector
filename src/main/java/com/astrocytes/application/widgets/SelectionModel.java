package com.astrocytes.application.widgets;

import com.astrocytes.application.widgets.primitives.drawable.Paintable;

import java.util.ArrayList;
import java.util.List;

public class SelectionModel {
    private List<Paintable> selection = new ArrayList<Paintable>();
    private Paintable hovered;

    public void addSelection(Paintable selection) {
        this.selection.add(selection);
    }

    public void clearSelection() {
        this.selection.clear();
    }

    public List<Paintable> getSelection() {
        return selection;
    }

    public void hover(Paintable obj) {
        this.hovered = obj;
    }

    public boolean isSelected(Paintable obj) {
        return selection.contains(obj);
    }

    public boolean isHovered(Paintable obj) {
        return hovered == obj;
    }
}
