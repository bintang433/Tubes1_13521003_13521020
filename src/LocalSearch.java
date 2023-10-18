import javafx.scene.control.Button;

import java.util.ArrayList;

public class LocalSearch extends Bot {

    public LocalSearch(Button[][] buttons, int roundsLeft, String self) {
        super(buttons, roundsLeft, self);
    }

    @Override
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
                if(this.getButtons()[i][j].getText().equals(""))
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

    public int objectiveFunction(int x, int y) {
        ArrayList<ArrayList<Integer>> buttonCoordinates = this.modifiedButtons(x, y, this.getOpp());
        int M = 0;
        for (int i = 1; i <= buttonCoordinates.get(0).get(0); i++) {
            int coordinateM = enemyPossibleMove(buttonCoordinates.get(i).get(0), buttonCoordinates.get(i).get(1));
            if (coordinateM > M) {
                M = coordinateM;
            }
        }
        return (buttonCoordinates.get(0).get(0) - M*2 - 1);
    }

    public int enemyPossibleMove(int x, int y)
    {
        int M = 1;
        if (x != 0)
        {
            if (this.getButtons()[x-1][y].getText().equals(""))
            {
                M += (modifiedButtons(x-1, y, this.getSelf()).get(0).get(0) - 1);
                System.out.printf("aaaa %d\n", M);
            }
        }
        if (x != 7)
        {
            if (this.getButtons()[x+1][y].getText().equals(""))
            {
                M += (modifiedButtons(x+1, y, this.getSelf()).get(0).get(0) - 1);
                System.out.printf("bbbb %d\n", M);
            }
        }
        if (y != 0)
        {
            if (this.getButtons()[x][y-1].getText().equals(""))
            {
                M += (modifiedButtons(x, y-1, this.getSelf()).get(0).get(0) - 1);
                System.out.printf("cccc %d\n", M);
            }
        }
        if (y != 7)
        {
            if (this.getButtons()[x][y+1].getText().equals(""))
            {
                M += (modifiedButtons(x, y+1, this.getSelf()).get(0).get(0) - 1);
                System.out.printf("dddd %d\n", M);
            }
        }

        return M;
    }

    

}