package com.demo.labelmaker.sticker;


public class FlipBothDirectionsEvent extends AbstractFlipEvent {
    @Override 
    protected int getFlipDirection() {
        return 3;
    }
}
