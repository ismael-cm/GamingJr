package com.example.gamingjr.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MapLayout extends View {

    private Paint paint = new Paint();
    private LinearLayout buttonLayout;

    public MapLayout(Context context) {
        super(context);
    }

    public MapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (buttonLayout != null) {
            // Obtener posiciones de los botones
            int childCount = buttonLayout.getChildCount();
            Button[] buttons = new Button[childCount];
            for (int i = 0; i < childCount; i++) {
                View childView = buttonLayout.getChildAt(i);
                if (childView instanceof Button) {
                    buttons[i] = (Button) childView;
                }
            }

            // Dibujar líneas entre botones
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] != null) {
                    for (int j = i + 1; j < buttons.length; j++) {
                        if (buttons[j] != null) {
                            canvas.drawLine(
                                    buttons[i].getX() + buttons[i].getWidth() / 2,
                                    buttons[i].getY() + buttons[i].getHeight() / 2,
                                    buttons[j].getX() + buttons[j].getWidth() / 2,
                                    buttons[j].getY() + buttons[j].getHeight() / 2,
                                    paint);
                        }
                    }
                }
            }
        }
    }

    public void setButtonLayout(LinearLayout buttonLayout) {
        this.buttonLayout = buttonLayout;
    }
}
