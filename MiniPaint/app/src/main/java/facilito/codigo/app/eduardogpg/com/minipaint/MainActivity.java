package facilito.codigo.app.eduardogpg.com.minipaint;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Color;

import android.widget.Button;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends AppCompatActivity {

    private DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.drawView = new DrawView(this);

        //setContentView(R.layout.main)
        this.setContentView(this.drawView);
        this.addContentView(this.drawView.row, this.drawView.params);
    }

    public class DrawView extends View{

        private final int finalColor = Color.parseColor("#9FA9DF");
        private final int widthPencil = 14;
        private final int widthEraser = 20;
        private final int widthCircle = 20;


        private Context myContex;
        private float lastPostX, lastPostY;

        private Paint circlePaint;
        private Path circlePath;

        private Paint paint;//Mantiene el color y los estilos de las figuras.
        private Path painPath; //Nos permite manejar figuras geometricas

        private Canvas canvas;//El lapiz
        private Bitmap  myBitmap;//Donde se debe de pintar
        private Paint bitmapPaint;

        public LayoutParams params;
        public LinearLayout row;//Nos permite colocar los elementos sin superponerlas

        private Button eraser;
        private Button pencil;
        private Button clean;

        public DrawView(Context context){
            super(context);
            this.myContex = context;
            this.bitmapPaint = new Paint();

            this.createCircle();
            this.createPaint();
            this.creatButtons();

            this.setBackgroundColor(Color.WHITE);
        }

        private void createCircle(){
            this.circlePaint = new Paint();
            this.circlePath = new Path();

            this.circlePaint.setDither(true);//NOs ayuda a mantener una precisión en los colores dependiedo del dispositivo a utilizar
            this.circlePaint.setAntiAlias(true);//Hace que los bordes de vean bien, sin pixeles
            this.circlePaint.setColor(this.finalColor);
            this.circlePaint.setStyle(Paint.Style.STROKE);//Nos permite que haya un circulo dentro

            this.circlePaint.setStrokeWidth(this.widthCircle);
        }

        private void createPaint(){
            this.paint = new Paint();
            this.painPath = new Path();

            this.paint.setAntiAlias(true);
            this.paint.setDither(true);//NOs ayuda a mantener una precisión en los colores dependiedo del dispositivo a utilizar
            this.paint.setColor(this.finalColor);
            this.paint.setStyle(Paint.Style.STROKE);//Hace que el circulo tengo un agujero

            this.paint.setStrokeCap(Paint.Cap.ROUND);//Nos permite cambiar la linea a como si fuera una pluma o brocha
            //this.paint.setStrokeJoin(Paint.Join.ROUND);

            this.paint.setStrokeWidth(12);
        }

        private void creatButtons(){
            this.params = new LayoutParams(LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT);

            this.row = new LinearLayout(this.myContex);
            this.row.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

            this.createEraser();
            this.createPencil();
            this.createClean();

            this.row.addView(this.pencil);
            this.row.addView(this.eraser);
            this.row.addView(this.clean);
        }

        private void createEraser(){
            this.eraser = new Button(this.myContex);
            this.eraser.setText("Borrador");
            this.eraser.setWidth(200);

            this.eraser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowMessage("Borrador");
                    setEraser();
                }
            });
        }

        private void createPencil(){
            this.pencil = new Button(this.myContex);
            this.pencil.setText("Lapiz");
            this.pencil.setWidth(200);

            this.pencil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowMessage("Lapiz");
                    setPencil();
                }
            });
        }

        private void createClean(){
            this.clean = new Button(this.myContex);
            this.clean.setText("Limpiar");
            this.clean.setWidth(200);

            this.clean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowMessage("Limpiar");
                    setClean();
                }
            });
        }

        private void ShowMessage(String message){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();//import android.widget.Toast;
        }

        private void setEraser(){
            this.paint.setStrokeWidth(this.widthEraser);
            this.paint.setColor(Color.WHITE);
        }

        private void setPencil(){
            this.paint.setStrokeWidth(this.widthPencil);
            this.paint.setColor(this.finalColor);
        }

        private void setClean(){
            this.canvas.drawColor(Color.WHITE);
            this.painPath.reset();
            this.invalidate();
            this.setPencil();
        }

        private void touch_start(float posx, float posy) {
            this.lastPostX = posx;
            this.lastPostY = posy;

            this.painPath.reset();
            this.painPath.moveTo(posx, posy); //Nos indica en que posición va a comenzar, si no comenzarimos desde la pos 0, 0
        }

        private void touch_move(float posx, float posy) {
            circlePath.reset();

            this.painPath.quadTo(this.lastPostX, this.lastPostY, (posx + this.lastPostX) / 2, (posy + this.lastPostY) / 2);
            this.lastPostX = posx;
            this.lastPostY = posy;

            this.circlePath.addCircle(posx, posy, 30, Path.Direction.CCW);
        }

        private void touch_up() {
            //this.painPath.lineTo(this.lastPostX, this.lastPostX);
            this.circlePath.reset();
            this.canvas.drawPath(this.painPath, this.paint);
            this.painPath.reset();
        }

        private void drawCustomeCircle(Canvas canvas) {
            Paint paint = new Paint();

            paint.setDither(true);//Nos ayuda a mantener una precisión en los colores dependiedo del dispositivo a utilizar
            paint.setAntiAlias(true);//Hace que los bordes de vean bien, sin pixeles
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(this.finalColor);
            canvas.drawCircle(300, 200, 100, paint);
        }

        private void drawFakeCustomeCircle(Canvas canvas) {
            Paint paint = new Paint();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(this.finalColor);
            canvas.drawCircle(300, 600, 100, paint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            //this.myBitmap es el lienzo, mientras que el canvas es donde de debe de dibujar
            this.myBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);//Each pixel is stored on 4 bytes.
            this.canvas = new Canvas(this.myBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(this.myBitmap, 0, 0, this.bitmapPaint);
            canvas.drawPath(this.painPath, this.paint);
            canvas.drawPath(this.circlePath, this.circlePaint);

            //this.drawCustomeCircle(canvas);
            //this.drawFakeCustomeCircle(canvas);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    break;
            }
            invalidate();//Si el view es visible realizará el llamado del pintado
            return true;
        }
    }
}
