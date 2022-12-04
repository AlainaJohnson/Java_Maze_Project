

import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;
    int rows;
    int cols;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        this.startRow = startRow;
        this.endRow = endRow;
        maze = new Cell[rows][cols];

        this.rows = rows;
        this.cols = cols;

        //for each cell object
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                maze[i][j] = new Cell();
            }
        }




    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze(int rows, int cols, int startRow, int endRow){
        Random rand = new Random();
        //instantiates a new MyMaze object
        MyMaze myMaze = new MyMaze(rows, cols, startRow, endRow);



        //creates stack
        Stack1Gen<int[]> stack = new Stack1Gen<int[]>();

        //adds start index to stack
        stack.push(new int[]{startRow,0});

        //updates isVisited
        myMaze.maze[startRow][0].setVisited(true);

//        int row = startRow;
//        int col = 0;

        while(!stack.isEmpty()){
            //Object top = stack.top();
            int row = stack.top()[0];
            int col = stack.top()[1];
//            Cell[] neighborArr = new Cell[4]; //max length is 4
            int[][] neighborArr = new int[4][2];
            int count = 0;

            //if it is in bounds and not visited, add it to nested neighbor array
            if(row+1<rows && !myMaze.maze[row+1][col].getVisited()){
                neighborArr[count] = new int[] {row + 1, col};
                count++;
            }
            if(row-1>=0 && !myMaze.maze[row-1][col].getVisited()){
                neighborArr[count] = new int[] {row - 1, col};
                count++;
            }
            if(col-1>=0 && !myMaze.maze[row][col-1].getVisited()){
                neighborArr[count] = new int[] {row, col - 1};
                count++;
            }

            if(col+1<cols && !myMaze.maze[row][col+1].getVisited()){
                neighborArr[count] = new int[] {row, col + 1};
                count++;
            }

            //if there are no neighbors that meet the requirements, pop
            if(count==0){
                stack.pop();
                //dead end
                //break;
            }
            //if there are valid neighbors
            else {

                int randNum = rand.nextInt(count);

                //randomly choose one of the valid neighbors
                int[] chosen = neighborArr[randNum];

                int newRow = chosen[0];
                int newCol = chosen[1];

                //updating row and col values based on chosen path
                if (row < newRow) {
                    myMaze.maze[row][newCol].setBottom(false);
                } else if (row>newRow) {
                    myMaze.maze[newRow][newCol].setBottom(false);
                }
                if (newCol < col) {
                    myMaze.maze[newRow][newCol].setRight(false);
                } else if (newCol>col) {
                    myMaze.maze[newRow][col].setRight(false);
                }

                stack.push(new int[]{newRow, newCol});
                myMaze.maze[newRow][newCol].setVisited(true);
            }
        }

        for(int i = 0; i<rows; i++){
            for(int j = 0; j<cols; j++){
                myMaze.maze[i][j].setVisited(false);
            }
        }

        return myMaze;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze(boolean path) {
        //iterates through each row
        int newDimR = rows * 2 + 1;
        int newDimC = cols * 2 + 1;
        for (int i = 0; i<newDimR; i++){
            for(int j =0; j<newDimC; j++){
                //start or end
                if((i==startRow*2+1 && j==0) || (i==endRow*2+1 && j==(cols*2+1)-1)){

                    System.out.print(" ");
                }
                else if(i % 2 ==0){
                    if(j % 2 == 0){

                        System.out.print("|");
                    }
                    else{
                        if(i>0 && maze[(i-1)/2][(j-1)/2].getBottom() == false){

                            System.out.print("   ");
                        }
                        else{
                            System.out.print("---");
                        }
                    }
                }
                else {
                    if (j % 2 == 0) {
                        if (j > 0 && maze[(i - 1) / 2][(j - 1) / 2].getRight() == false) {
                            System.out.print(" ");

                        } else {
                            System.out.print("|");
                        }
                    }
                    //checking if its start
                    else {
                        if (maze[i / 2][j / 2].getVisited()) {
                            System.out.print(" * ");
                        } else {
                            System.out.print("   ");
                        }

                    }
                }

            }
            System.out.println();
        }


    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        //make + initialize queue
        Q1Gen<int[]> queue = new Q1Gen<int[]>();
        queue.add(new int[] {startRow, 0});

        //while the queue is not empty
        while(queue.length() !=0){
            int[] temp = queue.remove();
            int row = temp[0];
            int col = temp[1];
            maze[temp[0]][temp[1]].setVisited(true);

            //if the current point is at the finish line
            if(row==endRow && col==cols-1){
                //if it has been solved, end it
                break;
            }
            else{
                //if there is no bottom and the cell below has not been visited
                if(row+1<rows && !maze[row][col].getBottom() && !maze[row+1][col].getVisited()){
                    queue.add(new int[] {row+1 , col});
                }
                //above current cell
                if(row>0 && !maze[row-1][col].getBottom() && !maze[row-1][col].getVisited()){
                    queue.add(new int[] {row-1, col});
                }
                //to the right of the current cell
                if(col+1<cols && !maze[row][col].getRight() && !maze[row][col+1].getVisited()){
                    queue.add(new int[] {row, col +1});
                }
                if(col-1>=0 && !maze[row][col-1].getRight() && !maze[row][col-1].getVisited()){
                    queue.add(new int[] {row, col-1});
                }
            }

        } //queue will be empty at end of the loop


        printMaze(true);

    }

    public static void main(String[] args){

        //rows and cols input from user
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter number of rows: ");
        int rows = scan.nextInt();
        //if inputted row number is out of range
        if(rows>20){
            rows = 20;
        }
        else if(rows<5){
            rows = 5;
        }

        System.out.println("Enter number of columns: ");
        int cols = scan.nextInt();
        //if inputted colum number is out of range
        if(cols>20){
            cols = 20;
        }
        else if(cols<5){
            cols = 5;
        }


        //random values for startRow and endRow
        Random rand = new Random();
        int startRow = rand.nextInt(rows);
        int endRow = rand.nextInt(rows);



        //making the maze with values from above
        MyMaze maze = makeMaze(rows, cols, startRow, endRow);


        maze.solveMaze();




    }
}
