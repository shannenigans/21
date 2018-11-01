package com.blackjack.woods.prototype;


import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Element;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

public class Processor implements Detector.Processor<TextBlock> {
    private SparseArray<TextBlock> items;

    public Processor() {

    }

    public TextBlock getAT(int i) {
        return items.get(i);
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        items = detections.getDetectedItems();
    }

    @Override
    public void release() {

    }

}
