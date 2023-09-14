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


public class Bot {

    private Button[][] buttons;

    public Bot(Button[][] buttons) {
        this.buttons = buttons;
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

}
