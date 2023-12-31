import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.Objects;

//import java.util.Collections;
public class MiniMax extends Bot {
    public MiniMax(Button[][] buttons, int roundsLeft, String self) {
        super(buttons, roundsLeft, self);
    }

    @Override
    public int[] move() {
        String[][] copyButtons = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!this.getButtons()[i][j].getText().isEmpty()) {
                    copyButtons[i][j] = this.getButtons()[i][j].getText();
                }
                else
                copyButtons[i][j] = "";
            }
        }
        ArrayList<Integer> bestMove = findBestMove(copyButtons);
        return new int[]{bestMove.get(0), bestMove.get(1)};
    }

    public int minimax(String[][] copyButtons, int curDepth, boolean maximize, int alpha, int beta) {
        int curVal = utility(copyButtons);
        System.out.println(curVal);
        System.out.printf("roundsLeft = %d, curDepth = %d\n", getRoundsLeft(), curDepth);
        if (curDepth == Math.min((getRoundsLeft() - 1), 2))
            return curVal;

        if (maximize) {
            int maxVal = Integer.MIN_VALUE;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (copyButtons[i][j].equals("")) {
                        ArrayList<ArrayList<Integer>> changedButtons = modifiedButtons(i, j, this.getOpp());
                        for (int k = 1; k < changedButtons.get(0).get(0); k++) {
                            copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = this.getSelf();
                        }
                        maxVal = Math.max(maxVal, minimax(copyButtons, curDepth + 1, !maximize, alpha, beta));

                        for (int k = 1; k < changedButtons.get(0).get(0); k++) {
                            copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = this.getOpp();
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
        } else {
            int minVal = Integer.MAX_VALUE;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (copyButtons[i][j].equals("")) {
                        ArrayList<ArrayList<Integer>> changedButtons = modifiedButtons(i, j, this.getSelf());
                        for (int k = 1; k < changedButtons.get(0).get(0); k++) {
                            copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = this.getOpp();
                        }
                        minVal = Math.min(minVal, minimax(copyButtons, curDepth + 1, !maximize, alpha, beta));
                        for (int k = 1; k < changedButtons.get(0).get(0); k++) {
                            copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = this.getSelf();
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

    public int utility(String[][] buttonState) {
        int X = 0;
        int O = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (buttonState[i][j].equals("X")) {
                    X++;
                } else if (buttonState[i][j].equals("O")) {
                    O++;
                }
            }
        }
        System.out.printf("O = %d, X = %d\n", O, X);

        return this.getSelf() == "O" ? (O - X) : (X-O);
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
                    ArrayList<ArrayList<Integer>> changedButtons = modifiedButtons(i, j, this.getOpp());
                    for(int k = 1; k < changedButtons.get(0).get(0); k++)
                    {
                        copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = this.getSelf();
                    }
                    int tempValue = minimax(copyButtons, 0, true, -999, 999);
                    for(int k = 1; k < changedButtons.get(0).get(0); k++)
                    {
                        copyButtons[changedButtons.get(k).get(0)][changedButtons.get(k).get(1)] = this.getOpp();
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

}