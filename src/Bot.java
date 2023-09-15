import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Bot {

    private Button[][] buttons;
    private int roundsLeft;

    public Bot(Button[][] buttons, int roundsLeft) {
        this.buttons = buttons;
        this.roundsLeft = roundsLeft;
    }

    public void roundPassed() {
        this.roundsLeft--;
    }

    public int[] move() {
        // create random move
        int x = (int) (Math.random()*8);
        int y = (int) (Math.random()*8);
        // int x = ;
        // int y;
        int objFuncValue = -999;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(buttons[i][j].getText().equals(""))
                {
                    int tempObjFuncVal = objectiveFunction(i, j);
                    if (tempObjFuncVal>objFuncValue)
                    {
                        objFuncValue = tempObjFuncVal;
                        x = i;
                        y = j;
                    }
                }
            }
        }
        return new int[]{x, y};
    }

    public int[] moveMinimax() {

        String[][] copyButtons = new String[8][8];
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                copyButtons[i][j] = this.buttons[i][j].getText();
            }
        }
        ArrayList<Integer> bestMove = findBestMove(copyButtons);

        return new int[]{bestMove.get(0), bestMove.get(1)};
    }

    public ArrayList<ArrayList<Integer>> modifiedButtons(int x, int y, String opp)
    {
        int N = 0;
        ArrayList<ArrayList<Integer>> buttonCoordinates = new ArrayList<>();
        if (x != 0)
        {
            if (buttons[x-1][y].getText().equals(opp))
            {
                N++;
                ArrayList<Integer> tempArr = new ArrayList<>();
                tempArr.add(x-1);
                tempArr.add(y);
                buttonCoordinates.add(tempArr);
            }   
        }
        if (x != 7)
        {
            if (buttons[x+1][y].getText().equals(opp))
            {
                N++;
                ArrayList<Integer> tempArr = new ArrayList<>();
                tempArr.add(x+1);
                tempArr.add(y);
                buttonCoordinates.add(tempArr);
            }         
        }
        if (y != 0)
        {
            if (buttons[x][y-1].getText().equals(opp))
            {
                N++;
                ArrayList<Integer> tempArr = new ArrayList<>();
                tempArr.add(x);
                tempArr.add(y-1);
                buttonCoordinates.add(tempArr);
            }
        }
        if (y != 7)
        {
            if (buttons[x][y+1].getText().equals(opp))
            {
                N++;
                ArrayList<Integer> tempArr = new ArrayList<>();
                tempArr.add(x);
                tempArr.add(y+1);
                buttonCoordinates.add(tempArr);
            }
        }
        ArrayList<Integer> buttonCount = new ArrayList<>();
        ArrayList<Integer> selectedButton = new ArrayList<>();
        selectedButton.add(x);
        selectedButton.add(y);
        buttonCoordinates.add(selectedButton);
        buttonCount.add(N+1);
        buttonCoordinates.add(0, buttonCount);
        return buttonCoordinates;
    }

    public int enemyPossibleMove(int x, int y)
    {
        int M = 1;
        if (x != 0)
        {
            if (buttons[x-1][y].getText().equals(""))
            {
                M += (modifiedButtons(x-1, y, "O").get(0).get(0) - 1);
                System.out.printf("aaaa %d\n", M);
            }   
        }
        if (x != 7)
        {
            if (buttons[x+1][y].getText().equals(""))
            {
                M += (modifiedButtons(x+1, y, "O").get(0).get(0) - 1);
                System.out.printf("bbbb %d\n", M);
            }         
        }
        if (y != 0)
        {
            if (buttons[x][y-1].getText().equals(""))
            {
                M += (modifiedButtons(x, y-1, "O").get(0).get(0) - 1);
                System.out.printf("cccc %d\n", M);
            }
        }
        if (y != 7)
        {
            if (buttons[x][y+1].getText().equals(""))
            {
                M += (modifiedButtons(x, y+1, "O").get(0).get(0) - 1);
                System.out.printf("dddd %d\n", M);
            }
        }

        return M;
    }

    public int objectiveFunction(int x, int y)
    {
        ArrayList<ArrayList<Integer>> buttonCoordinates = modifiedButtons(x, y, "X");
        int M = 0;
        for(int i = 1; i <= buttonCoordinates.get(0).get(0); i++)
        {
            int coordinateM = enemyPossibleMove(buttonCoordinates.get(i).get(0), buttonCoordinates.get(i).get(1));
            if (coordinateM > M)
            {
                M = coordinateM;
            }
        }
        System.out.printf("%d, %d", buttonCoordinates.get(0).get(0), M);
        System.out.println();
        return (buttonCoordinates.get(0).get(0) - M*2 - 1);
    }
    public int minimax(String[][] copyButtons, int curDepth, boolean maximize, int alpha, int beta)
    {
        int curVal = utility(copyButtons);
        System.out.println(curVal);
        System.out.printf("roundsLeft = %d, curDepth = %d\n", roundsLeft, curDepth);
        if(curDepth == (roundsLeft - 1))
        return curVal;

        if(maximize)
        {
            int maxVal = -999;
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    if (copyButtons[i][j].equals(""))
                    {
                        ArrayList<ArrayList<Integer>> changedButtons = modifiedButtons(i, j, "X");
                        for(int k = 1; k < changedButtons.get(0).get(0); k++)
                        {
                            copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = "O";
                        }
                        maxVal = Math.max(maxVal, minimax(copyButtons, curDepth+1, !maximize, alpha, beta));
                        for(int k = 1; k < changedButtons.get(0).get(0); k++)
                        {
                            copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = "X";
                        }
                        copyButtons[i][j] = "";

                        alpha = Math.max(alpha, maxVal);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }

            return maxVal;
        }
        else
        {
            int minVal = 999;
            for(int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    // System.out.println("pipi");
                    if(copyButtons[i][j].equals(""))
                    {
                        ArrayList<ArrayList<Integer>> changedButtons = modifiedButtons(i, j, "O");
                        for(int k = 1; k < changedButtons.get(0).get(0); k++)
                        {
                            copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = "X";
                        }
                        minVal = Math.min(minVal, minimax(copyButtons, curDepth+1, !maximize, alpha, beta));
                        for(int k = 1; k < changedButtons.get(0).get(0); k++)
                        {
                            copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = "O";
                        }
                        copyButtons[i][j] = "";

                        alpha = Math.min(alpha, minVal);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }

            return minVal;
        }
    }

    public ArrayList<Integer> findBestMove(String[][] copyButtons)
    {   
        int bestValue = -999;
        int x = -1;
        int y = -1;

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if (copyButtons[i][j].equals(""))
                {
                    // copyButtons[i][j] = "O";
                    ArrayList<ArrayList<Integer>> changedButtons = modifiedButtons(i, j, "X");
                    for(int k = 1; k < changedButtons.get(0).get(0); k++)
                    {
                        copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = "O";
                    }
                    int tempValue = minimax(copyButtons, 0, false, -999, 999);
                    for(int k = 1; k < changedButtons.get(0).get(0); k++)
                    {
                        copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = "X";
                    }
                    copyButtons[i][j] = "";

                    if (tempValue>bestValue)
                    {
                        x = i;
                        y = j;
                        bestValue = tempValue;
                    }
                }
            }
        }

        ArrayList<Integer> bestMove = new ArrayList<>();
        bestMove.add(x);
        bestMove.add(y);
        return bestMove;
    }

    public int utility(String[][] buttonState)
    {
        int X = 0;
        int O = 0;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if (buttonState[i][j].equals("X"))
                {
                    X++;
                }
                else if(buttonState[i][j].equals("O"))
                {
                    O++;
                }
            }
        }
        System.out.printf("O = %d, X = %d\n", O, X);
        return O-X;
    }

}
