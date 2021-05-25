/******************************************************************************
 *  Compilation:  javac *.java
 *  Execution:    java Life x y m
 *  Dependencies: Picture.java
 *
 *  Simple demo of how you can use the Picture class for graphical output
 *
 *  In this demo an x-by-y grid of cells will be drawn in random colours, to level of magnification m
 *
 *  m represents number of interations
 *
 ******************************************************************************/

import java.awt.Color;
import java.util.Random;

public class Life
{
    private final int x; // x-by-y grid of cells
    private final int y;
    private final int m; // pixel-width of each cell
    private final Color[][] cells; // cells to be randomly coloured
    private static Picture pic; // picture to be drawn on screen
    private static Picture test;
    private static Color[][] updated;

    public Life(final int x, final int y, final int m) {
        this.x = x;
        this.y = y;
        this.m = m;
        cells = new Color[x][x];
        updated = new Color[x][x];
        pic = new Picture(x * 10, x * 10);
    }

    // fill a cell with a random colour
    public void initBuildGrid(final int i, final int j, final String y) {
        if(y.equals("L")){
            for (int offsetX = 0; offsetX < 10; offsetX++) {
                for (int offsetY = 0; offsetY < 10; offsetY++) {
    
                    this.pic.set((i * 10) + offsetX, (j * 10) + offsetY, new Color(255,255,255));
                    this.cells[i][j] = new Color(255,255,255);
                }
            }
            int[] lws = new int[]{ 201, 501, 602, 203, 603, 304, 404, 504, 604};

            for(int iq = 0; iq < 9; iq++){
                this.cells[ ((lws[iq] - (lws[iq]%100))/100) ][ (lws[iq] %100) ] = new Color(0,0,0);
            }
        }
        //RandomSetting

        if(!(y.equals("L")) && !(y.equals("P"))){
            for (int offsetX = 0; offsetX < 10; offsetX++) {
                for (int offsetY = 0; offsetY < 10; offsetY++) {
                    // set() colours an individual pixel
                    Random rand = new Random();
                    int randomValue = rand.nextInt() % 2;
    
                    Color bc = new Color(255,255,255); //white
                    if(randomValue == 1){
                        bc = new Color(0,0,0); //black
                    }
    
                    this.pic.set((i * 10) + offsetX, (j * 10) + offsetY, bc);
                    this.cells[i][j] = bc;
                }
            }
        }
        
        if(y.equals("P")){
            for (int offsetX = 0; offsetX < 10; offsetX++) {
                for (int offsetY = 0; offsetY < 10; offsetY++) {
    
                    this.pic.set((i * 10) + offsetX, (j * 10) + offsetY, new Color(255,255,255));
                    this.cells[i][j] = new Color(255,255,255);
                }
            }
            int[] lws = new int[]{ 402,502,602, 1002,1102,1202, 204,704,904,1404, 205,705,905,1405, 206,706,906,1406, 407,507,607, 1007,1107,1207,409,509,609, 1009,1109,1209,  210,710,910,1410, 211,711,911,1411, 212,712,912,1412,  414,514,614, 1014,1114,1214};

            for(int iq = 0; iq < 48; iq++){
                this.cells[ (lws[iq] %100) ][ ((lws[iq] - (lws[iq]%100))/100) ] = new Color(0,0,0);
            }
        
        }

    }

    //determines what the next state should be
    public void findNextColor(final int i, final int j, int x) { //Need to take in x for the foreverScroll feature
        int determiner = 0;
        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                if(this.cells[(i+k+x) %x][(j+l+x) %x].equals(new Color(0,0,0)) ){
                    determiner += 1;
                }
            }
        }

        if (determiner == 3) { //has three neighbours and is dead or has two neighbours and is alive
            this.updated[i][j] = new Color(0,0,0); //be alive (black)
        } else {
            if (determiner == 4) { //have four neighbours and is dead or has three neighbours and is alive
                this.updated[i][j] = this.cells[i][j];
            } else {
                this.updated[i][j] = new Color(255,255,255); //all other cases - die.
            }
        }
    }

    //updates the cell with colour calculated in updateCell
    public void copyCell() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                this.cells[i][j] = this.updated[i][j];
                for (int offsetX = 0; offsetX < 10; offsetX++) {
                    for (int offsetY = 0; offsetY < 10; offsetY++) {
                        this.pic.set((i * 10) + offsetX, (j * 10) + offsetY, this.cells[i][j]);
                    }
                } 
            }
        }   
    }

    // display (or update) the picture on screen
    public void show() {
        this.pic.show(); // without calling this the pic will not show
    }

    // must provide grid size (x & y) and level of magnification (m)
    public static void main(final String[] args) {
        final int x = Integer.parseInt(args[1]);
        final String y = args[2];
        final int m = Integer.parseInt(args[0]);

        final Life life = new Life(x, x, m);

        // fill each cell with a random colour
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                life.initBuildGrid(i, j, y);
            }
        }
        life.show();
        for (int q = 1; q < m; q++) {
            try {
                Thread.sleep(100);
            } 
                catch(InterruptedException e)
            {
            // this part is executed when an exception (in this example InterruptedException) occurs
            }
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < x; j++) {
                    life.findNextColor(i, j, x);
                }
            }
            life.copyCell();
            life.show();

        }
    }
}
