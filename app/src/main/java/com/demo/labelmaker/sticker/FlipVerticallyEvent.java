package com.demo.labelmaker.sticker;


public class FlipVerticallyEvent extends AbstractFlipEvent {
    @Override 
    protected int getFlipDirection() {
        return 2;
    }
}
