/**
 * Created by yzs on 2017/4/9.+1s
 */
////////
import java.util.*;
import java.io.*;

////////
public class Q4D0123456789 extends Tetris {
    // enter your student id here
    public String id = new String("Q4D0123456789");

    public boolean currentboard[][];
    public boolean currentpiece[][];
    int currentpiece_x;
    int currentpiece_y;

    public void cpyBoard(boolean source[][], boolean dst[][]){
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                dst[y][x] = source[y][x];
    }

    public void cpyPiece(boolean source[][], boolean dst[][]){
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
                dst[y][x] = source[y][x];
    }


    public int colHeight(int col){
        int c = 0;
        for(int i = 0; i < h; i++)
            if(currentboard[i][col])
                c++;
        return c;
    }

    public int aggregateHeight(){
        int c = 0;
        for(int i = 0; i < w; i++)
            c += colHeight(i);
        return c;
    }

    public int completeLines(){
        int c = 0;
        for(int i = 0; i < h; i++)
        {
            boolean flag = true;
            for(int j = 0; j < w; j++)
                if(!currentboard[i][j]){
                    flag = false;
                    break;
                }
            if(flag)
                c++;
        }
        return c;
    }

    public int holesCount(){
        int c = 0;
        for(int i = 0; i < h - 1; i++)
            for(int j = 0; j < w; j++)
                if(!currentboard[i][j] && currentboard[i + 1][j])
                    c++;
        return c;
    }

    public int bumpinessCount(){
        int c = 0;
        for(int i = 0; i < w - 1; i++)
            c += Math.abs(colHeight(i + 1) - colHeight(i));
        return c;
    }

    public double eval(){
        return -0.510066 * aggregateHeight() + 0.760666 * completeLines() - 0.35663 * holesCount() - 0.184483 * bumpinessCount();
    }


    public boolean movePiece(PieceOperator op) {
        // remove piece from board
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (currentpiece[y][x])
                    currentboard[currentpiece_y-y][currentpiece_x+x] = false;
            }
        }
        // generate a new piece
        int new_piece_x = currentpiece_x;
        int new_piece_y = currentpiece_y;

        boolean new_piece[][] = new boolean[4][4];
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                new_piece[y][x] = currentpiece[y][x];
            }
        }
        // piece operation
        switch (op) {
            case ShiftLeft:  new_piece_x--; break;
            case ShiftRight: new_piece_x++; break;
            case Drop:       new_piece_y--; break;
            case Rotate:
                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        new_piece[y][x] = currentpiece[x][3-y];
                    }
                }
                break;
        }
        // check if new_piece is deployable
        boolean deployable = true;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (!new_piece[y][x]) continue;
                if (new_piece_x+x < 0 || new_piece_x+x >= w
                        || new_piece_y-y < 0 || new_piece_y-y >= h
                        || currentboard[new_piece_y-y][new_piece_x+x]) {
                    deployable = false;
                    break;
                }
            }
        }
        if (deployable) {
            // replace piece with new_piece
            currentpiece_x = new_piece_x;
            currentpiece_y = new_piece_y;
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    currentpiece[y][x] = new_piece[y][x];
                }
            }
        }
        // deploy piece
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (currentpiece[y][x])
                    currentboard[currentpiece_y-y][currentpiece_x+x] = true;
            }
        }

        return deployable;
    }


    class Solution{
        double score;
        int lx;
        int cnt_r;
        public Solution(){
            score = 0.0;
            lx = 0;
            cnt_r = 0;
        }
    }

    public final void displaycurrentBoard(){
        // clear screen, OS dependent

        // print board
        System.out.printf("\n");
        System.out.printf("-------debug-------");
        for (int y = h-1; y >= 0; y--) {
            System.out.printf("%s", y < h-4 ? "<!":"  ");
            for (int x = 0; x < w; x++) {
                System.out.printf("%c", currentboard[y][x] ? 'H':' ');
            }
            System.out.printf("%s", y < h-4 ? "!>":"  ");
            System.out.printf("\n");
        }
        System.out.printf("<!");
        for (int x = 0; x < w; x++) {
            System.out.printf("=");
        }
        System.out.printf("!>");
        System.out.printf("\n");
        // print score
        System.out.printf("-------debug-------");
        System.out.printf("\n");
        // flush
        System.out.flush();
    }

    // ####
    public PieceOperator robotPlay() {

        final boolean board[][] = getBoard();
        final boolean piece[][] = getPiece();
        final int piece_x = getPieceX();
        final int piece_y = getPieceY();


        currentboard = getBoard();
        currentpiece = getPiece();
        currentpiece_x = getPieceX();
        currentpiece_y = getPieceY();

        Solution sol[] = new Solution[50];
        int cnt = 0;

        for(int rotation = 0; rotation < 4; rotation++){
            cpyBoard(board, currentboard);
            cpyPiece(piece, currentpiece);
            currentpiece_x = piece_x;
            currentpiece_y = piece_y;

            // rotate n times
            int t = 0;
            while(t < rotation) {
                movePiece(PieceOperator.Rotate);
                t++;
            }

            while(movePiece(PieceOperator.ShiftLeft));
            int cx = currentpiece_x;
            int cy = currentpiece_y;
            boolean cboard[][] = new boolean[h][w];


            do {
                cpyBoard(currentboard, cboard);
                cx = currentpiece_x;
                cy = currentpiece_y;
                while(movePiece(PieceOperator.Drop));
                sol[cnt]=new Solution();
                sol[cnt].score = eval();
                sol[cnt].lx = currentpiece_x;
                sol[cnt].cnt_r = rotation;
                cnt++;
                //displaycurrentBoard();
                cpyBoard(cboard, currentboard);
                currentpiece_x = cx;
                currentpiece_y = cy;

            }while(movePiece(PieceOperator.ShiftRight));
        }

        int maxi = 0;
        double max_score = -999999999.99;

        for(int i = 0; i < cnt; i++)
            if(sol[i].score > max_score)
            {
                max_score = sol[i].score;
                maxi = i;
            }
        //System.out.println("\nmaxi = "+maxi + "\n");
        //System.out.println("\nlx = "+sol[maxi].lx + "\n");
        //System.out.println("\ncnt_r = "+sol[maxi].cnt_r + "\n");
        if(sol[maxi].cnt_r != 0)
            return PieceOperator.Rotate;
        if(piece_x < sol[maxi].lx)
            return PieceOperator.ShiftRight;
        if(piece_x > sol[maxi].lx)
            return PieceOperator.ShiftLeft;
        return PieceOperator.Drop;
    }
}

