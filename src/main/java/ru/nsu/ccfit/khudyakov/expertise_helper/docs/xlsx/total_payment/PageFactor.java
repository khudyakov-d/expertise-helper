package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PageFactor {
    PAGE_FACTOR_20(1d),
    PAGE_FACTOR_40(1.1d),
    PAGE_FACTOR_60(1.2d),
    PAGE_FACTOR_80(1.3d),
    PAGE_FACTOR_MAX(1.4d);

    private final double value;

    public static PageFactor getFromCount(int pageCount) {
            if (pageCount <= 20) {
            return PAGE_FACTOR_20;
        } else if (pageCount <= 40) {
            return PAGE_FACTOR_40;
        } else if (pageCount <= 60) {
            return PAGE_FACTOR_60;
        } else if (pageCount <= 80) {
            return PAGE_FACTOR_80;
        } else {
            return PAGE_FACTOR_MAX;
        }
    }

}
