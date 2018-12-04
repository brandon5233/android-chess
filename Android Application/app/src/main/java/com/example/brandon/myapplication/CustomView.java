package com.example.brandon.myapplication;

/**
 * Created by Brandon on 15-11-2017.
 */

// imports
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import static com.example.brandon.myapplication.MainActivity.flagcounter;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


// class definition
public class CustomView extends View {

    boolean flag = false;

    private Paint red, green, blue, yellow, black,white, gray,one, two, three, four, m;
    private float touchx; // x position of each touch
    private float touchy; // y position of each touch
    boolean bombed = FALSE,color=FALSE,can_move=FALSE;

   MainActivity mains = new MainActivity();

    int width, x, y;
    int flag_counter = 0;
    int height;
    int count = 0;
    int cell_i, cell_j,temp_i,temp_j;
    int number_counter = 0;
    int win;
    float block;

    String[][] a = new String[9][9];
    String turn="white";

    String mode = "select";




    int[][] flag_array = new int[11][11];

    int[][] temp_selected_tiles =  new int[9][9];
    String[][] temp_a = new String[9][9];
    int prev_i=0,prev_j=0;

    float w, h;

    public CustomView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    public CustomView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    private void init() {

        // method used to initialize the initial state of the game.
        bombed = false;

        for(int i=1;i<=8;i++){Arrays.fill(a[i],"");}
        red = new Paint(Paint.ANTI_ALIAS_FLAG);
        green = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellow = new Paint(Paint.ANTI_ALIAS_FLAG);
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        gray = new Paint(Paint.ANTI_ALIAS_FLAG);
        one = new Paint(Paint.ANTI_ALIAS_FLAG);
        two = new Paint(Paint.ANTI_ALIAS_FLAG);
        three = new Paint(Paint.ANTI_ALIAS_FLAG);
        four = new Paint(Paint.ANTI_ALIAS_FLAG);
        m = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);

        red.setColor(0xFFFF0000);
        green.setColor(0xFF00FF00);
        blue.setColor(0xFF0000FF);
        yellow.setColor(0xFFFFFF00);
        black.setColor(0xFF663300);
        white.setColor(0xFFFFF68F);
        gray.setColor(0xFF7C7C7C);
        one.setColor(0xFF0000FF);
        two.setColor(0xFF00FF00);
        three.setColor(0xFFFFFF00);
        four.setColor(0xFFFF0000);
        m.setColor(0xFF000000);

        m.setTextAlign(Paint.Align.CENTER);
        one.setTextAlign(Paint.Align.CENTER);
        two.setTextAlign(Paint.Align.CENTER);
        three.setTextAlign(Paint.Align.CENTER);
        four.setTextAlign(Paint.Align.CENTER);


        number_counter = 0;
        for(int i=1;i<=8;i++)a[2][i]="white_pawn";
        for(int i=1;i<=8;i++)a[7][i]="black_pawn";
        a[1][1]=a[1][8]="white_elephant";
        a[8][1]=a[8][8]="black_elephant";
        a[1][2]=a[1][7]="white_horse";
        a[8][2]=a[8][7]="black_horse";
        a[1][3]=a[1][6]="white_camel";
        a[8][3]=a[8][6]="black_camel";
        a[1][5]="white_queen";
        a[1][4]="white_king";
        a[8][4]="black_king";
        a[8][5]="black_queen";

        //a[3][1]="abc";

        //a[1][5] = "white_king";
        //a[1][4] = "white_queen";
        //a[8]



    }

    //Measures screen width and height and sets canvas to square
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        width = Math.min(widthSize, heightSize);
        height = width;

        setMeasuredDimension(width, height);
    }

    //Draws onto the canvas on every change in the game
    public void onDraw(Canvas canvas) {

        System.out.println("ENTERED onDraw()");
        super.onDraw(canvas);
        canvas.save();
        canvas.drawRect(0, 0, width, height, white);

        block = (float) ((width) / 8);
        float space = (float) ((0.1 * width))*0;

        w = block / 2;
        h = w;

        float w1 = (w*11)/12;
        float h1 = w1;


        m.setTextSize((float) 0.75 * block);
        one.setTextSize((float) 0.75 * block);
        two.setTextSize((float) 0.75 * block);
        three.setTextSize((float) 0.75 * block);
        four.setTextSize((float) 0.75 * block);
        white.setTextSize((float) 0.75 * block);

        //traverses across the canvas drawing blocks row by row depending on what conditions they satisfy
        //eg. black if uncovered, yellow if flagged
        canvas.translate(w, h);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {

              /* if (open[i][j] == 1 && !mains.reset && flag_array[i][j] != 1) {
                    canvas.drawRect(-w, -h, w, h, gray);
                    if      (a[i][j] == 1) canvas.drawText(String.valueOf(a[i][j]), 0, h / 2, one);
                    else if (a[i][j] == 2) canvas.drawText(String.valueOf(a[i][j]), 0, h / 2, two);
                    else if (a[i][j] == 3) canvas.drawText(String.valueOf(a[i][j]), 0, h / 2, three);
                    else if (a[i][j] >= 4 && a[i][j] != 500) canvas.drawText(String.valueOf(a[i][j]), 0, h / 2, four);
                    else if (a[i][j] == 500) { canvas.drawRect(-w, -h, w, h, red);canvas.drawText("M", 0, h / 2, m);
                    }
                }
                else if (flag_array[i][j] == 1) {canvas.drawRect(-w, -h, w, h, yellow);}

                else {
                    canvas.drawRect(-w, -h, w, h, black);
                }
                canvas.translate((block + space), 0);
            }
            canvas.restore();
            canvas.translate(0, block + space);
            canvas.save();
            canvas.translate(w, w);
            */



                  if(color){canvas.drawRect(-w, -h, w, h, black);}
                  else canvas.drawRect(-w, -h, w, h, white);

                if(temp_selected_tiles[i][j]==1){canvas.drawRect(-w1,-h1,w1,h1,green);//selected_tiles[i][j]=0;
                     }

                if(a[i][j].equals("white_pawn")){canvas.drawText("P",0,h/2,one);}
                if(a[i][j].equals("white_elephant")){canvas.drawText("E",0,h/2,one);}
                if(a[i][j].equals("white_camel")){canvas.drawText("C",0,h/2,one);}
                if(a[i][j].equals("white_horse")){canvas.drawText("H",0,h/2,one);}
                if(a[i][j].equals("black_pawn")){canvas.drawText("p",0,h/2,one);}
                if(a[i][j].equals("black_elephant")){canvas.drawText("e",0,h/2,one);}
                if(a[i][j].equals("black_camel")){canvas.drawText("c",0,h/2,one);}
                if(a[i][j].equals("black_horse")){canvas.drawText("h",0,h/2,one);}
                if(a[i][j].equals("white_king")){canvas.drawText("K",0,h/2,one);}
                if(a[i][j].equals("white_queen")){canvas.drawText("Q",0,h/2,one);}
                if(a[i][j].equals("black_king")){canvas.drawText("k",0,h/2,one);}
                if(a[i][j].equals("black_queen")){canvas.drawText("q",0,h/2,one);}

                canvas.translate((block + space), 0);
                color=!color;
            }
            color=!color;
            canvas.restore();
            canvas.translate(0, block);
            canvas.save();
            canvas.translate(w, w);
        }

        mains.reset = false;
    }

//Method that handles user input.
    //updates corresponding array depending on if flagged mode is on or not
    //doesnt respond to user input if game is won or lost
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("ENTERED onTouch()");
        mains = new MainActivity();

        //Minesweeper
        if (bombed) {
            Toast.makeText(getContext(), "Hit RESET for a new game!", Toast.LENGTH_SHORT).show();
        } else {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                touchx = event.getX();
                touchy = event.getY();

                cell_i = (int) Math.ceil(touchy / block);
                cell_j = (int) Math.ceil(touchx / block);
                System.out.println("touched block = " + cell_i + "," + cell_j);
                //selected_tiles[cell_i][cell_j]=1;

                System.out.println("\n\n mode = "+ mode + "\n");
                if (mode.equals("select")) {
                    for (int i = 1; i <= 8; i++) {
                        for (int j = 1; j <= 8; j++) {
                            temp_selected_tiles[i][j] = 0;
                        }
                    }
                    prev_i = cell_i;
                    prev_j = cell_j;
                    if (a[cell_i][cell_j].contains(turn)) {

                        System.out.println("CALLING MOVEMENT From on touch");
                        temp_selected_tiles = movement(cell_i,cell_j,a);
                        System.out.println("done calling movement");
                        System.out.println("\n\n after movement mode = "+ mode + "\n");

                        check_if_check();
                        //invalidate();
                        System.out.println("Selection DONE after calling CHECK IF CHECK");

                    }
                }else if (mode.equals("move")) {
                    System.out.println("trying to move : ");
                        if (temp_selected_tiles[cell_i][cell_j] == 1 & (cell_i != prev_i | cell_j != prev_j)) {
                            a[cell_i][cell_j] = a[prev_i][prev_j];
                            a[prev_i][prev_j] = "";
                            turn = (turn.equals("white")) ? "black" : "white";
                            //flagcounter.setText(String.format("%s %d",getResources().getText(R.string.textview2),flag_counter));
                            flagcounter.setText(String.format("%s's turn", turn));

                        }
                        for (int i = 1; i <= 8; i++) {
                            for (int j = 1; j <= 8; j++) {
                                temp_selected_tiles[i][j] = 0;
                            }
                        }
                    System.out.println("\n setting select in move : ");
                    mode = "select";
                    }
                invalidate();
                }

            }
            return true;
        }

    //method resets the game
    //resets all flags, counters and arrays
    //calls init() to re initialize the game
    public int[][] movement(int row,int col,String[][] position)
    {
        System.out.println("ENTERED MOVEMENT()");
        int[][] selected_tiles =  new int[9][9];
        for(int i=1;i<9;i++)
        {
            for(int j=1;j<9;j++){
                selected_tiles[i][j]=0;
            }
        }
        switch (position[row][col]) {
            case "white_pawn":
                selected_tiles[row][col] = 1;

                if (row != 8) {

                    if (row == 2) {
                        // selected_tiles[row][col] = 1;
                        for (int i = 1; i <= 2; i++) {
                            if (position[row + i][col].equals("")) {
                                selected_tiles[row + i][col] = 1;
                                // mode = "move";
                                can_move = TRUE;
                            } else break;

                        }
                    } else if (((position[row + 1][col]).equals(""))) {
                        // selected_tiles[row][col]=1;
                        selected_tiles[row + 1][col] = 1;
                        //  mode="move";
                        can_move = TRUE;
                    }
                    if (col > 1 & col < 8) {
                        if ((position[row + 1][col - 1]).contains("black")) {
                            //selected_tiles[row][col] = 1;
                            selected_tiles[row + 1][col - 1] = 1;
                            // mode="move";
                            can_move = TRUE;
                        }
                        if ((position[row + 1][col + 1]).contains("black")) {
                            selected_tiles[row][col] = 1;
                            selected_tiles[row + 1][col + 1] = 1;
                            mode = "move";
                            can_move = TRUE;
                        }
                    } else if (col == 1) {
                        if ((position[row + 1][col + 1]).contains("black")) {
                            // selected_tiles[row][col] = 1;
                            selected_tiles[row + 1][col + 1] = 1;
                            //  mode="move";
                            can_move = TRUE;
                        }
                    } else if (col == 8) {
                        if ((position[row + 1][col - 1]).contains("black")) {
                            //selected_tiles[row][col] = 1;
                            selected_tiles[row + 1][col - 1] = 1;
                            //  mode="move";
                            can_move = TRUE;
                        }

                    }
                }
                if (can_move) {
                    mode = "move";
                    can_move = FALSE;
                } else {
                    mode = "select";
                }
                break;
            case "black_pawn":
                selected_tiles[row][col] = 1;
                if (row == 7) {

                    for (int i = 1; i <= 2; i++) {
                        if (position[row - i][col].equals("")) {
                            selected_tiles[row - i][col] = 1;
                            can_move = TRUE;
                            //selected_tiles[row][col]=1;
                            //mode="move";
                        } else break;
                    }

                }
                if (row != 1) {
                    if (((position[row - 1][col]).equals(""))) {
                        //selected_tiles[row][col]=1;
                        selected_tiles[row - 1][col] = 1;
                        can_move = TRUE;
                        // mode="move";
                    }
                    if (col > 1 & col < 8) {
                        if ((position[row - 1][col - 1]).contains("white")) {
                            // selected_tiles[row][col] = 1;
                            selected_tiles[row - 1][col - 1] = 1;
                            can_move = TRUE;
                            // mode="move";
                        }
                        if ((position[row - 1][col + 1]).contains("white")) {
                            // selected_tiles[row][col] = 1;
                            selected_tiles[row - 1][col + 1] = 1;
                            can_move = TRUE;
                            // mode="move";
                        }
                    } else if (col == 1) {
                        if ((position[row - 1][col + 1]).contains("white")) {
                            //selected_tiles[row][col] = 1;
                            selected_tiles[row - 1][col + 1] = 1;
                            can_move = TRUE;
                            // mode="move";
                        }
                    } else if (col == 8) {
                        if ((position[row - 1][col - 1]).contains("white")) {
                            //selected_tiles[row][col] = 1;
                            selected_tiles[row - 1][col - 1] = 1;
                            can_move = TRUE;
                            // mode="move";
                        }

                    }


                }
                if (can_move) {
                    mode = "move";
                    can_move = FALSE;
                } else {
                    mode = "select";
                }
                break;

            case "white_elephant":
            case "black_elephant":
                selected_tiles[row][col] = 1;
                for (int i = row; i <= 8; i++) {
                    if (position[i][col].equals("")) {
                        selected_tiles[i][col] = 1;
                        mode = "move";
                        can_move = TRUE;
                    } else if ((position[row][col].contains("white") & position[i][col].contains("black")) | (position[row][col].contains("black") & position[i][col].contains("white"))) {
                        selected_tiles[i][col] = 1;
                        mode = "move";
                        can_move = TRUE;
                        break;
                    } else if (i != row) break;
                }
                for (int j = col; j <= 8; j++) {
                    if (position[row][j].equals("")) {
                        selected_tiles[row][j] = 1;
                        mode = "move";
                        can_move = TRUE;
                    } else if ((position[row][col].contains("white") & position[row][j].contains("black")) | position[row][col].contains("black") & position[row][j].contains("white")) {
                        selected_tiles[row][j] = 1;
                        mode = "move";
                        can_move = TRUE;
                        break;
                    } else if (col != j) break;
                }

                for (int i = row; i >= 1; i--) {
                    if (position[i][col].equals("")) {
                        selected_tiles[i][col] = 1;
                        mode = "move";
                        can_move = TRUE;
                    } else if ((position[row][col].contains("white") & position[i][col].contains("black")) | position[row][col].contains("black") & position[i][col].contains("white")) {
                        selected_tiles[i][col] = 1;
                        mode = "move";
                        can_move = TRUE;
                        break;
                    } else if (i != row) break;
                }
                for (int j = col; j >= 1; j--) {
                    if (position[row][j].equals("")) {
                        selected_tiles[row][j] = 1;
                        mode = "move";
                        can_move = TRUE;
                    } else if ((position[row][col].contains("white") & position[row][j].contains("black")) | position[row][col].contains("black") & position[row][j].contains("white")) {
                        selected_tiles[row][j] = 1;
                        mode = "move";
                        can_move = TRUE;
                        break;
                    } else if (j != col) break;
                }

                if (can_move) {
                    selected_tiles[row][col] = 1;
                    can_move = FALSE;
                }
                break;
            case "knight":
                break;
            case "white_camel":
            case "black_camel":
                selected_tiles[row][col] = 1;
                int i = row;
                boolean stop_left = true, stop_right = true;
                int jminus = col, jplus = col;
                while (i < 8) {
                    jminus = jminus - 1;
                    jplus = jplus + 1;
                    if (jminus >= 1 & stop_left) {
                        if (position[i + 1][jminus].equals("")) {
                            selected_tiles[i + 1][jminus] = 1;
                            mode = "move";
                        } else if ((position[row][col].contains("white") & position[i + 1][jminus].contains("black")) | position[row][col].contains("black") & position[i + 1][jminus].contains("white")) {
                            selected_tiles[i + 1][jminus] = 1;
                            mode = "move";
                            stop_left = FALSE;
                        } else {
                            System.out.println("else");
                            stop_left = FALSE;
                        }
                    }

                    if (jplus <= 8 & stop_right) {
                        if (position[i + 1][jplus].equals("")) {
                            selected_tiles[i + 1][jplus] = 1;
                            mode = "move";
                        } else if ((position[row][col].contains("white") & position[i + 1][jplus].contains("black")) | position[row][col].contains("black") & position[i + 1][jplus].contains("white")) {
                            selected_tiles[i + 1][jplus] = 1;
                            mode = "move";
                            stop_right = FALSE;
                        } else {
                            stop_right = FALSE;
                        }
                    }
                    i++;
                }

                jminus = col;
                jplus = col;
                stop_left = TRUE;
                stop_right = TRUE;
                i = row;
                while (i > 1) {
                    jminus = jminus - 1;
                    jplus = jplus + 1;
                    if (jminus >= 1 & stop_left) {
                        if (position[i - 1][jminus].equals("")) {
                            selected_tiles[i - 1][jminus] = 1;
                            mode = "move";
                        } else if ((position[row][col].contains("white") & position[i - 1][jminus].contains("black")) | position[row][col].contains("black") & position[i - 1][jminus].contains("white")) {
                            selected_tiles[i - 1][jminus] = 1;
                            mode = "move";
                            stop_left = FALSE;
                        } else {
                            System.out.println("else");
                            stop_left = FALSE;
                        }
                    }

                    if (jplus <= 8 & stop_right) {
                        if (position[i - 1][jplus].equals("")) {
                            selected_tiles[i - 1][jplus] = 1;
                            mode = "move";
                        } else if ((position[row][col].contains("white") & position[i - 1][jplus].contains("black")) | position[row][col].contains("black") & position[i - 1][jplus].contains("white")) {
                            selected_tiles[i - 1][jplus] = 1;
                            mode = "move";
                            stop_right = FALSE;
                        } else {
                            stop_right = FALSE;
                        }
                    }
                    i--;
                }


                break;
            case "white_king":
            case "black_king":
                selected_tiles[row][col] = 1;
                if (col != 1 && col != 8 && row != 1 && row != 8) {

                    if ((position[row - 1][col - 1].equals("")) | (position[row][col].contains("white") & position[row - 1][col - 1].contains("black")) | (position[row][col].contains("black") & position[row - 1][col - 1].contains("white"))) {
                        selected_tiles[row - 1][col - 1] = 1;
                        mode = "move";
                    }

                    if ((position[row - 1][col].equals("")) | (position[row][col].contains("white") & position[row - 1][col].contains("black")) | (position[row][col].contains("black") & position[row - 1][col].contains("white"))) {
                        selected_tiles[row - 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row - 1][col + 1].equals("")) | (position[row][col].contains("white") & position[row - 1][col + 1].contains("black")) | (position[row][col].contains("black") & position[row - 1][col + 1].contains("white"))) {
                        selected_tiles[row - 1][col + 1] = 1;
                        mode = "move";

                    }
                    if ((position[row][col - 1].equals("")) | (position[row][col].contains("white") & position[row][col - 1].contains("black")) | (position[row][col].contains("black") & position[row][col - 1].contains("white"))) {
                        selected_tiles[row][col - 1] = 1;
                        mode = "move";
                    }

                    if ((position[row][col + 1].equals("")) | (position[row][col].contains("white") & position[row][col + 1].contains("black")) | (position[row][col].contains("black") & position[row][col + 1].contains("white"))) {
                        selected_tiles[row][col + 1] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col - 1].equals("")) | (position[row][col].contains("white") & position[row + 1][col - 1].contains("black")) | (position[row][col].contains("black") & position[row + 1][col - 1].contains("white"))) {
                        selected_tiles[row + 1][col - 1] = 1;
                        mode = "move";
                    }
                    if ((position[row + 1][col].equals("")) | (position[row][col].contains("white") & position[row + 1][col].contains("black")) | (position[row][col].contains("black") & position[row + 1][col].contains("white"))) {
                        selected_tiles[row + 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col + 1].equals("")) | (position[row][col].contains("white") & position[row + 1][col + 1].contains("black")) | (position[row][col].contains("black") & position[row + 1][col + 1].contains("white"))) {
                        selected_tiles[row + 1][col + 1] = 1;
                        mode = "move";
                    }
                }

                if (row == 1 && col == 1) {

                    if ((position[row][col + 1].equals("")) | (position[row][col].contains("white") & position[row][col + 1].contains("black")) | (position[row][col].contains("black") & position[row][col + 1].contains("white"))) {
                        selected_tiles[row][col + 1] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col].equals("")) | (position[row][col].contains("white") & position[row + 1][col].contains("black")) | (position[row][col].contains("black") & position[row + 1][col].contains("white"))) {
                        selected_tiles[row + 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col + 1].equals("")) | (position[row][col].contains("white") & position[row + 1][col + 1].contains("black")) | (position[row][col].contains("black") & position[row + 1][col + 1].contains("white"))) {
                        selected_tiles[row + 1][col + 1] = 1;
                        mode = "move";
                    }
                }

                if (row == 1 && col == 8) {

                    if ((position[row][col - 1].equals("")) | (position[row][col].contains("white") & position[row][col - 1].contains("black")) | (position[row][col].contains("black") & position[row][col - 1].contains("white"))) {
                        selected_tiles[row][col - 1] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col].equals("")) | (position[row][col].contains("white") & position[row + 1][col].contains("black")) | (position[row][col].contains("black") & position[row + 1][col].contains("white"))) {
                        selected_tiles[row + 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col - 1].equals("")) | (position[row][col].contains("white") & position[row + 1][col - 1].contains("black")) | (position[row][col].contains("black") & position[row + 1][col - 1].contains("white"))) {
                        selected_tiles[row + 1][col - 1] = 1;
                        mode = "move";
                    }
                }

                if (row == 1 && col > 1 && col < 8) {
                    if ((position[row][col - 1].equals("")) | (position[row][col].contains("white") & position[row][col - 1].contains("black")) | (position[row][col].contains("black") & position[row][col - 1].contains("white"))) {
                        selected_tiles[row][col - 1] = 1;
                        mode = "move";
                    }

                    if ((position[row][col + 1].equals("")) | (position[row][col].contains("white") & position[row][col + 1].contains("black")) | (position[row][col].contains("black") & position[row][col + 1].contains("white"))) {
                        selected_tiles[row][col + 1] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col - 1].equals("")) | (position[row][col].contains("white") & position[row + 1][col - 1].contains("black")) | (position[row][col].contains("black") & position[row + 1][col - 1].contains("white"))) {
                        selected_tiles[row + 1][col - 1] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col].equals("")) | (position[row][col].contains("white") & position[row + 1][col].contains("black")) | (position[row][col].contains("black") & position[row + 1][col].contains("white"))) {
                        selected_tiles[row + 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col + 1].equals("")) | (position[row][col].contains("white") & position[row + 1][col + 1].contains("black")) | (position[row][col].contains("black") & position[row + 1][col + 1].contains("white"))) {
                        selected_tiles[row + 1][col + 1] = 1;
                        mode = "move";
                    }

                }

                if (row == 8 && col == 1) {

                    if ((position[row][col + 1].equals("")) | (position[row][col].contains("white") & position[row][col + 1].contains("black")) | (position[row][col].contains("black") & position[row][col + 1].contains("white"))) {
                        selected_tiles[row][col + 1] = 1;
                        mode = "move";
                    }

                    if ((position[row - 1][col].equals("")) | (position[row][col].contains("white") & position[row - 1][col].contains("black")) | (position[row][col].contains("black") & position[row - 1][col].contains("white"))) {
                        selected_tiles[row - 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row - 1][col + 1].equals("")) | (position[row][col].contains("white") & position[row - 1][col + 1].contains("black")) | (position[row][col].contains("black") & position[row - 1][col + 1].contains("white"))) {
                        selected_tiles[row - 1][col + 1] = 1;
                        mode = "move";

                    }
                }

                if (row == 8 && col == 8) {
                    if ((position[row - 1][col - 1].equals("")) | (position[row][col].contains("white") & position[row - 1][col - 1].contains("black")) | (position[row][col].contains("black") & position[row - 1][col - 1].contains("white"))) {
                        selected_tiles[row - 1][col - 1] = 1;
                        mode = "move";
                    }

                    if ((position[row - 1][col].equals("")) | (position[row][col].contains("white") & position[row - 1][col].contains("black")) | (position[row][col].contains("black") & position[row - 1][col].contains("white"))) {
                        selected_tiles[row - 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row][col - 1].equals("")) | (position[row][col].contains("white") & position[row][col - 1].contains("black")) | (position[row][col].contains("black") & position[row][col - 1].contains("white"))) {
                        selected_tiles[row][col - 1] = 1;
                        mode = "move";
                    }
                }


                if (row > 1 && row < 8 && col == 1) {


                    if ((position[row - 1][col].equals("")) | (position[row][col].contains("white") & position[row - 1][col].contains("black")) | (position[row][col].contains("black") & position[row - 1][col].contains("white"))) {
                        selected_tiles[row - 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row - 1][col + 1].equals("")) | (position[row][col].contains("white") & position[row - 1][col + 1].contains("black")) | (position[row][col].contains("black") & position[row - 1][col + 1].contains("white"))) {
                        selected_tiles[row - 1][col + 1] = 1;
                        mode = "move";

                    }

                    if ((position[row][col + 1].equals("")) | (position[row][col].contains("white") & position[row][col + 1].contains("black")) | (position[row][col].contains("black") & position[row][col + 1].contains("white"))) {
                        selected_tiles[row][col + 1] = 1;
                        mode = "move";
                    }


                    if ((position[row + 1][col].equals("")) | (position[row][col].contains("white") & position[row + 1][col].contains("black")) | (position[row][col].contains("black") & position[row + 1][col].contains("white"))) {
                        selected_tiles[row + 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row + 1][col + 1].equals("")) | (position[row][col].contains("white") & position[row + 1][col + 1].contains("black")) | (position[row][col].contains("black") & position[row + 1][col + 1].contains("white"))) {
                        selected_tiles[row + 1][col + 1] = 1;
                        mode = "move";
                    }

                }
                if (row > 1 && row < 8 && col == 8) {

                    if ((position[row - 1][col - 1].equals("")) | (position[row][col].contains("white") & position[row - 1][col - 1].contains("black")) | (position[row][col].contains("black") & position[row - 1][col - 1].contains("white"))) {
                        selected_tiles[row - 1][col - 1] = 1;
                        mode = "move";
                    }

                    if ((position[row - 1][col].equals("")) | (position[row][col].contains("white") & position[row - 1][col].contains("black")) | (position[row][col].contains("black") & position[row - 1][col].contains("white"))) {
                        selected_tiles[row - 1][col] = 1;
                        mode = "move";
                    }


                    if ((position[row][col - 1].equals("")) | (position[row][col].contains("white") & position[row][col - 1].contains("black")) | (position[row][col].contains("black") & position[row][col - 1].contains("white"))) {
                        selected_tiles[row][col - 1] = 1;
                        mode = "move";
                    }


                    if ((position[row + 1][col - 1].equals("")) | (position[row][col].contains("white") & position[row + 1][col - 1].contains("black")) | (position[row][col].contains("black") & position[row + 1][col - 1].contains("white"))) {
                        selected_tiles[row + 1][col - 1] = 1;
                        mode = "move";
                    }
                    if ((position[row + 1][col].equals("")) | (position[row][col].contains("white") & position[row + 1][col].contains("black")) | (position[row][col].contains("black") & position[row + 1][col].contains("white"))) {
                        selected_tiles[row + 1][col] = 1;
                        mode = "move";
                    }

                }
                if (row == 8 && col > 1 && col < 8) {
                    if ((position[row - 1][col - 1].equals("")) | (position[row][col].contains("white") & position[row - 1][col - 1].contains("black")) | (position[row][col].contains("black") & position[row - 1][col - 1].contains("white"))) {
                        selected_tiles[row - 1][col - 1] = 1;
                        mode = "move";
                    }

                    if ((position[row - 1][col].equals("")) | (position[row][col].contains("white") & position[row - 1][col].contains("black")) | (position[row][col].contains("black") & position[row - 1][col].contains("white"))) {
                        selected_tiles[row - 1][col] = 1;
                        mode = "move";
                    }

                    if ((position[row - 1][col + 1].equals("")) | (position[row][col].contains("white") & position[row - 1][col + 1].contains("black")) | (position[row][col].contains("black") & position[row - 1][col + 1].contains("white"))) {
                        selected_tiles[row - 1][col + 1] = 1;
                        mode = "move";

                    }
                    if ((position[row][col - 1].equals("")) | (position[row][col].contains("white") & position[row][col - 1].contains("black")) | (position[row][col].contains("black") & position[row][col - 1].contains("white"))) {
                        selected_tiles[row][col - 1] = 1;
                        mode = "move";
                    }

                    if ((position[row][col + 1].equals("")) | (position[row][col].contains("white") & position[row][col + 1].contains("black")) | (position[row][col].contains("black") & position[row][col + 1].contains("white"))) {
                        selected_tiles[row][col + 1] = 1;
                        mode = "move";
                    }
                }


                break;
            case "white_queen":
            case "black_queen":
                selected_tiles[row][col] = 1;
                for (i = row; i <= 8; i++) {
                    if (position[i][col].equals("")) {
                        selected_tiles[i][col] = 1;
                        mode = "move";
                        can_move = TRUE;
                    } else if ((position[row][col].contains("white") & position[i][col].contains("black")) | (position[row][col].contains("black") & position[i][col].contains("white"))) {
                        selected_tiles[i][col] = 1;
                        mode = "move";
                        can_move = TRUE;
                        break;
                    } else if (i != row) break;
                }
                for (int j = col; j <= 8; j++) {
                    if (position[row][j].equals("")) {
                        selected_tiles[row][j] = 1;
                        mode = "move";
                        can_move = TRUE;
                    } else if ((position[row][col].contains("white") & position[row][j].contains("black")) | position[row][col].contains("black") & position[row][j].contains("white")) {
                        selected_tiles[row][j] = 1;
                        mode = "move";
                        can_move = TRUE;
                        break;
                    } else if (col != j) break;
                }

                for (i = row; i >= 1; i--) {
                    if (position[i][col].equals("")) {
                        selected_tiles[i][col] = 1;
                        mode = "move";
                        can_move = TRUE;
                    } else if ((position[row][col].contains("white") & position[i][col].contains("black")) | position[row][col].contains("black") & position[i][col].contains("white")) {
                        selected_tiles[i][col] = 1;
                        mode = "move";
                        can_move = TRUE;
                        break;
                    } else if (i != row) break;
                }
                for (int j = col; j >= 1; j--) {
                    if (position[row][j].equals("")) {
                        selected_tiles[row][j] = 1;
                        mode = "move";
                        can_move = TRUE;
                    } else if ((position[row][col].contains("white") & position[row][j].contains("black")) | position[row][col].contains("black") & position[row][j].contains("white")) {
                        selected_tiles[row][j] = 1;
                        mode = "move";
                        can_move = TRUE;
                        break;
                    } else if (j != col) break;
                }

                selected_tiles[row][col] = 1;
                i = row;
                stop_left = true;
                stop_right = true;
                jminus = col;
                jplus = col;
                while (i < 8) {
                    jminus = jminus - 1;
                    jplus = jplus + 1;
                    if (jminus >= 1 & stop_left) {
                        if (position[i + 1][jminus].equals("")) {
                            selected_tiles[i + 1][jminus] = 1;
                            mode = "move";
                        } else if ((position[row][col].contains("white") & position[i + 1][jminus].contains("black")) | position[row][col].contains("black") & position[i + 1][jminus].contains("white")) {
                            selected_tiles[i + 1][jminus] = 1;
                            mode = "move";
                            stop_left = FALSE;
                        } else {
                            //System.out.println("else");
                            stop_left = FALSE;
                        }
                    }

                    if (jplus <= 8 & stop_right) {
                        if (position[i + 1][jplus].equals("")) {
                            selected_tiles[i + 1][jplus] = 1;
                            mode = "move";
                        } else if ((position[row][col].contains("white") & position[i + 1][jplus].contains("black")) | position[row][col].contains("black") & position[i + 1][jplus].contains("white")) {
                            selected_tiles[i + 1][jplus] = 1;
                            mode = "move";
                            stop_right = FALSE;
                        } else {
                            stop_right = FALSE;
                        }
                    }
                    i++;
                }

                jminus = col;
                jplus = col;
                stop_left = TRUE;
                stop_right = TRUE;
                i = row;
                while (i > 1) {
                    jminus = jminus - 1;
                    jplus = jplus + 1;
                    if (jminus >= 1 & stop_left) {
                        if (position[i - 1][jminus].equals("")) {
                            selected_tiles[i - 1][jminus] = 1;
                            mode = "move";
                        } else if ((position[row][col].contains("white") & position[i - 1][jminus].contains("black")) | position[row][col].contains("black") & position[i - 1][jminus].contains("white")) {
                            selected_tiles[i - 1][jminus] = 1;
                            mode = "move";
                            stop_left = FALSE;
                        } else {
                            // System.out.println("else");
                            stop_left = FALSE;
                        }
                    }

                    if (jplus <= 8 & stop_right) {
                        if (position[i - 1][jplus].equals("")) {
                            selected_tiles[i - 1][jplus] = 1;
                            mode = "move";
                        } else if ((position[row][col].contains("white") & position[i - 1][jplus].contains("black")) | position[row][col].contains("black") & position[i - 1][jplus].contains("white")) {
                            selected_tiles[i - 1][jplus] = 1;
                            mode = "move";
                            stop_right = FALSE;
                        } else {
                            stop_right = FALSE;
                        }
                    }
                    i--;
                }
                if (can_move) {
                    selected_tiles[row][col] = 1;
                    can_move = FALSE;
                }

                break;
        }
        return selected_tiles;
    }

    public void check_if_check()
    {
        int[][]  abc = new int[9][9];
        System.out.println("ENTERED CHECK_IF_CHECK()");

        /*******************************************************************************************************************/
        System.out.println("\n\n");
        System.out.println("TEMP_SELECTED = \n");
        for(int i=1;i<=8;i++)
        {
            for(int j=1;j<=8;j++)
            {
                System.out.print(temp_selected_tiles[i][j]);
            }
            System.out.println();
        }


        /*******************************************************************************************************************/


        int temp_i=0;
        int temp_j=0;
        for(int i=1;i<9;i++)
        {
            for(int j=1;j<9;j++)
            {
                temp_a[i][j]=a[i][j];
            }
        }



      loop:  for(int i=1;i<9;i++)
        {
            for(int j=1;j<9;j++)
            {
                if(i==cell_i&j==cell_j)
                {

                }
                else if(temp_selected_tiles[i][j]==1)
                {
                   // if(!("_king".contains(temp_a[i][j])))
                        temp_a[i][j]=a[cell_i][cell_j];
                        temp_a[cell_i][cell_j]="";

                        //Finding the position of the white_king
                        System.out.println("*******************************************************");
                        System.out.println(a[cell_i][cell_j] + " placed at " + i + "," + j);

                    for(int k=1;k<=8;k++)
                    {
                        for(int l=1;l<=8;l++)
                        {
                            if(temp_a[k][l].equals(turn+"_king"))
                            {
                                temp_i=k;
                                temp_j=l;
                            }
                        }
                    }
                    System.out.println(String.format("KING IS AT %d , %d",temp_i,temp_j));

                    //find the nearest neighbours of the king
                    for(int u=temp_i-1;u>=1;u--) //UP
                    {
                        if((temp_a[temp_i][temp_j].contains("black") & temp_a[u][temp_j].contains("white"))|(temp_a[temp_i][temp_j].contains("white") & temp_a[u][temp_j].contains("black")))
                        {
                            abc=movement(u,temp_j,temp_a);      //save the position

                            for(int e=1;e<=8;e++)
                            {
                                for(int f=1;f<=8;f++)
                                {
                                   // System.out.print(abc[e][f] + " ");
                                    if((abc[e][f]==1) & (temp_a[e][f].contains(turn+"_king")))
                                    {
                                        temp_selected_tiles[i][j]=0;
                                        System.out.println("CANNOT MOVE TO POS "+ i+","+j);
                                        break;

                                    }
                                }
                              //  System.out.println("");
                            }
                            break;
                        }
                        else if((temp_a[temp_i][temp_j].contains("black") & temp_a[u][temp_j].contains("black"))|(temp_a[temp_i][temp_j].contains("white") & temp_a[u][temp_j].contains("white"))) {
                            break;
                        }

                    }

                    for(int d=temp_i+1;d<=8;d++) //DOWN
                    {
                        if((temp_a[temp_i][temp_j].contains("black") & temp_a[d][temp_j].contains("white"))|(temp_a[temp_i][temp_j].contains("white") & temp_a[d][temp_j].contains("black")))
                        {
                            abc=movement(d,temp_j,temp_a);
                            for(int e=1;e<=8;e++)
                            {
                                for(int f=1;f<=8;f++)
                                {
                                    //System.out.print(abc[e][f] + " ");
                                    if((abc[e][f]==1) & (temp_a[e][f].contains(turn+"_king")))
                                    {
                                        temp_selected_tiles[i][j]=0;
                                        System.out.println("CANNOT MOVE TO POS "+ i+","+j);
                                        break;

                                    }
                                }
                               // System.out.println("");
                            }
                            //save the position
                            break;
                        }
                        else if((temp_a[temp_i][temp_j].contains("black") & temp_a[d][temp_j].contains("black"))|(temp_a[temp_i][temp_j].contains("white") & temp_a[d][temp_j].contains("white")))
                        {
                            break;
                        }
                    }
                    for(int s_l=temp_j-1;s_l>=1;s_l--) //SIDE LEFT
                    {
                        if((temp_a[temp_i][temp_j].contains("black") & temp_a[temp_i][s_l].contains("white"))|(temp_a[temp_i][temp_j].contains("white") & temp_a[temp_i][s_l].contains("black")))
                        {
                            abc=movement(temp_i,s_l,temp_a);
                            for(int e=1;e<=8;e++)
                            {
                                for(int f=1;f<=8;f++)
                                {
                                    //System.out.print(abc[e][f] + " ");
                                    if((abc[e][f]==1) & (temp_a[e][f].contains(turn+"_king")))
                                    {
                                        temp_selected_tiles[i][j]=0;
                                        System.out.println("CANNOT MOVE TO POS "+ i+","+j);
                                        break;

                                    }
                                }
                              //  System.out.println("");
                            }
                            //save the position
                            break;
                        }
                        else if((temp_a[temp_i][temp_j].contains("black") & temp_a[temp_i][s_l].contains("black"))|(temp_a[temp_i][temp_j].contains("white") & temp_a[temp_i][s_l].contains("white")))
                        {
                            break;
                        }
                    }

                    for(int s_r=temp_j+1;s_r<=8;s_r++) //SIDE RIGHT
                    {
                        if((temp_a[temp_i][temp_j].contains("black") & temp_a[temp_i][s_r].contains("white"))|(temp_a[temp_i][temp_j].contains("white") & temp_a[temp_i][s_r].contains("black")))
                        {
                            abc=movement(temp_i,s_r,temp_a);
                            for(int e=1;e<=8;e++)
                            {
                                for(int f=1;f<=8;f++)
                                {
                                    //System.out.print(abc[e][f] + " ");
                                    if((abc[e][f]==1) & (temp_a[e][f].contains(turn+"_king")))
                                    {
                                        temp_selected_tiles[i][j]=0;
                                      //  System.out.println("CANNOT MOVE TO POS "+ i+","+j);
                                        break;

                                    }
                                }
                                System.out.println("");
                            }
                            //save the position
                            break;
                        }
                        else if((temp_a[temp_i][temp_j].contains("black") & temp_a[temp_i][s_r].contains("black"))|(temp_a[temp_i][temp_j].contains("white") & temp_a[temp_i][s_r].contains("white")))
                        {
                            break;
                        }
                    }


                    temp_a[i][j]=a[i][j];
                    temp_a[cell_i][cell_j]=a[cell_i][cell_j];



                }

            }
            //END
                }


            }



    public void reset() {
        Toast.makeText(getContext(), "Resetting", Toast.LENGTH_SHORT).show();







        for(int i=1;i<=8;i++)
        {
            for(int j=1;j<=8;j++)
            {
                temp_selected_tiles[i][j]=0;
            }
        }
        turn = "white";
        flagcounter.setText(String.format("%s's turn",turn));

        bombed = false;
        flag = false;
        flag_counter = 0;
      //  flagcounter.setText(String.format("%s %d",getResources().getText(R.string.textview2),flag_counter));
        count = 0;
        win = 0;
        init();
        invalidate();
    }
}

