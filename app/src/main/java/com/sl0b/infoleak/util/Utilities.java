package com.sl0b.infoleak.util;

import android.graphics.Rect;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

public class Utilities {

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static Rect getLocationOnScreen(View view) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        view.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + view.getWidth();
        mRect.bottom = location[1] + view.getHeight();

        return mRect;
    }
}
