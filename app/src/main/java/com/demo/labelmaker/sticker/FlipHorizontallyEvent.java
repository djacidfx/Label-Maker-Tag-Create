package com.demo.labelmaker.sticker;


public class FlipHorizontallyEvent extends AbstractFlipEvent {
    @Override 
    protected int getFlipDirection() {
        return 1;
    }
}
