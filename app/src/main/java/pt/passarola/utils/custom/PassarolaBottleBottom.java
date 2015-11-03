package pt.passarola.utils.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ruigoncalo on 25/10/15.
 */
public class PassarolaBottleBottom extends View {

    private static final int width = 200;
    private static final int height = 100;
    private Paint paint;

    private Path path;
    private Point a, b, c, d, e, f;
    private RectF rectRight, rectLeft;

    public PassarolaBottleBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        path = new Path();
        a = new Point(0, 0);
        b = new Point(width, 0);
        c = new Point(width, height/2);
        d = new Point(width-50, height);
        e = new Point(50, height);
        f =new Point(0, height/2);
        rectRight = new RectF(c.x - 50, c.y, d.x + 50, d.y);
        rectLeft = new RectF(f.x, f.y, e.x, e.y);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = width;
        int desiredHeight = height;

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == View.MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == View.MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.arcTo(rectRight, 0, 90);
        path.lineTo(d.x, d.y);
        path.lineTo(e.x, e.y);
        path.arcTo(rectLeft, 90, 90);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, paint);
    }
}
