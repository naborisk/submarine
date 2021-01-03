import java.util.*;

public class Main {

	public static final char[] LETTERS = {'-', 'A', 'B', 'C', 'D', 'E', '-'};
	public static final String[] DIRECTION = {"ERROR", "North", "East", "South", "West"};
	public static final boolean DEBUG = false;
	public static final int THRESHOLD = 8;

	public static void print(int[][] a){
		int i,j;
		System.out.println("-----------------------------------");
		for(i=0; i<7; i++){
			for(j=0; j<7; j++){
				if(a[i][j] == -1){
					if(j == 0 || j == a.length-1) System.out.print("  " + LETTERS[i] +"  ");
					else System.out.print("  " + j +"  ");
				}else if(a[i][j] > 0){
					System.out.print("  " + a[i][j] + "  ");
				}else{
					System.out.print("     ");
				}
			}
			System.out.print("\n-----------------------------------\n");
		}
		
	}
	
	public static void moveEquation(int moveEq[][], int totalHP, int numSub, int nextMove[][]){
		for(int i=1; i<6; i++){
			for(int j=1; j<6; j++){
				moveEq[i][j] = (totalHP + numSub + nextMove[i][j]) / 2;
			}
		}
	}
	
	public static void moveEquationAr(int moveEq[][], int m, int n, int totalHP, int numSub, int nextMove[][]){
		
		int[][] temArr = {
				{-1, -1, -1, -1, -1, -1, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1, -1, -1, -1, -1, -1, -1},
		};
		
		if(temArr[m+1][n+1] > -1) temArr[m+1][n+1] = 1;  
		if(temArr[m+1][n] > -1) temArr[m+1][n] = 1;
		if(temArr[m+1][n-1] > -1) temArr[m+1][n-1] = 1;
		if(temArr[m][n+1] > -1) temArr[m][n+1] = 1;
		if(temArr[m][n-1] > -1) temArr[m][n-1] = 1;
		if(temArr[m-1][n+1] > -1) temArr[m-1][n+1] = 1;
		if(temArr[m-1][n] > -1) temArr[m-1][n] = 1;
		if(temArr[m-1][n-1] > -1) temArr[m-1][n-1] = 1;
		
		
		for(int i=1; i<6; i++){
			for(int j=1; j<6; j++){
				if(m == i && n == j) moveEq[i][j] = 0;
				else if(temArr[i][j] == 1){
					moveEq[i][j] = totalHP + numSub + nextMove[i][j];
				}else{
					moveEq[i][j] = (totalHP + numSub + nextMove[i][j]) / 2;
				}
			}
		}
	}
	
	public static void _max(int a[][], int m, int n) { 
		int max = a[0][0];
		for(int i=1; i<6; i++){ 
			for(int j=1; j<6; j++){ 
				if(a[i][j] > max){
					max = a[i][j]; 
					m = i;
					n = j;
				}
			}
		}
	}
	
	public static int total(int a[][]){
		int sum = 0;
		int i,j;
		
		for(i=1; i<6; i++){
			for(j=1; j<6; j++){
				sum += a[i][j];
			}
		}
		return sum;
	}
	
	public static int _num(int a[][]){
		int num = 0;
		int i,j;
		
		for(i=1; i<6; i++){
			for(j=1; j<6; j++){
				if(a[i][j] > 0){
					num++;
				};
			}
		}
		
		return num;
	}

	// debug printing
	public static void printArr(String name, int[][] arr) {
		System.out.println(name);
		printArr(arr);
	}

	public static void printArr(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}

	// Make the decision based on moveEq
	static void decide(int[][] moveEq, int[][] board, int[][] nextMove) {
		int[][] coordinates = new int[4][2]; // coordinates of each ship
		int x = 0; // iterator for each coordinates to save

		for (int i = 1; i < board.length - 1; i++) {
			for (int j = 1; j < board[0].length - 1; j++) {
				// Ship found
				if(board[i][j] > 0) {
					coordinates[x][0] = i;
					coordinates[x][1] = j;
					x++;
				}
				// break when all ships are found
				if(x == 4) break;
			}
		}

		// moveEq value collection & ship
		int maxRow = 0, maxCol = 0;
		int maxMoveEq = 0;
		int ship = 0;
		int targetShip = 0;
		boolean canAttack = true; // will be false when target slot is 2 slots apart or there is our ship

		for (int[] s : coordinates) {
			int row = s[0];
			int col = s[1];

			if(row == 0 && col == 0) break; // if ship doesn't exist then exit

			// for surrounding spaces
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if( moveEq[i][j] != -1 && !(i == row && j == col)) {
						if(DEBUG) System.out.print(moveEq[i][j] + " ");

						// find max and update coordinates
						if(moveEq[i][j] > maxMoveEq) {
							maxRow = i;
							maxCol = j;
							maxMoveEq = moveEq[i][j];
							targetShip = ship;
							if(DEBUG) System.out.println("\nMaxMoveEq is set to " + maxMoveEq + "at " + LETTERS[maxRow] + "" + maxCol);
							if(DEBUG) System.out.println("Of ship #" + targetShip);
						}
					}
				}
			}

			// for row+2, row-2, col+2, col-2
			if(row + 2 < board.length && moveEq[row+2][col] > maxMoveEq) {
				maxRow = row+2;
				maxCol = col;
				maxMoveEq = moveEq[row+2][col];
				targetShip = ship;
				canAttack = false;
				if(DEBUG) System.out.println("\nMaxMoveEq is set to " + maxMoveEq + "at " + LETTERS[maxRow] + "" + maxCol);
				if(DEBUG) System.out.println("Of ship #" + targetShip);
			}

			if(row - 2 > 0 && moveEq[row-2][col] > maxMoveEq) {
				maxRow = row-2;
				maxCol = col;
				maxMoveEq = moveEq[row-2][col];
				targetShip = ship;
				canAttack = false;
				if(DEBUG) System.out.println("\nMaxMoveEq is set to " + maxMoveEq + "at " + LETTERS[maxRow] + "" + maxCol);
				if(DEBUG) System.out.println("Of ship #" + targetShip);
			}

			if(col + 2 < board.length && moveEq[row][col+2] > maxMoveEq) {
				maxRow = row;
				maxCol = col+2;
				maxMoveEq = moveEq[row][col+2];
				targetShip = ship;
				canAttack = false;
				if(DEBUG) System.out.println("\nMaxMoveEq is set to " + maxMoveEq + "at " + LETTERS[maxRow] + "" + maxCol);
				if(DEBUG) System.out.println("Of ship #" + targetShip);
			}

			if(col - 2 > 0 && moveEq[row][col-2] > maxMoveEq) {
				maxRow = row;
				maxCol = col-2;
				maxMoveEq = moveEq[row][col-2];
				targetShip = ship;
				canAttack = false;
				if(DEBUG) System.out.println("\nMaxMoveEq is set to " + maxMoveEq + "at " + LETTERS[maxRow] + "" + maxCol);
				if(DEBUG) System.out.println("Of ship #" + targetShip);
			}

			if(DEBUG) System.out.println("Ship is at: " + LETTERS[row] + "" + col);
			ship++;

			// set moveEquation for the ship to 0
			moveEq[row][col] = 0;
		}

		//TODO: check for dead ship

		if(board[maxRow][maxCol] > 0) canAttack = false;

		boolean attacked = false;
		if(maxMoveEq > THRESHOLD && canAttack) {
			// attack that location
			System.out.println("『[" + LETTERS[maxRow] + "-" + maxCol + "] に魚雷発射！』");
			attacked = true;
		} else {

			int shipRow = coordinates[targetShip][0];
			int shipCol = coordinates[targetShip][1];

			int dist = 0;
			String dir = "東西南北";

			// Swap the ship with MaxMoveEq's Location

			if((dist = maxRow - shipRow) > 0) {
				// South
				dir = "南";
			} else if ((dist = maxRow - shipRow) < 0) {
				// North
				dir	= "北";
			} else if ((dist = maxCol - shipCol) > 0) {
				// East
				dir = "東";
			} else if ((dist = maxCol - shipCol) < 0) {
				// West
				dir = "西";
			}

			// board[maxCol][maxRow] -> target

			if(board[maxCol][maxRow] == 0) {
				board[maxRow][maxCol] = board[shipRow][shipCol];
				board[shipRow][shipCol] = 0;

				// swap moveEq as well when moving ship
				int temp = moveEq[shipRow][shipCol];
				moveEq[shipRow][shipCol] = moveEq[maxCol][maxCol];
				moveEq[maxCol][maxCol] = temp;

				System.out.println("『潜水艦を" + dir + "に" + Math.abs(dist) + "マス移動！ 』");
			} else {
				for (int i = maxRow-1; i < maxRow+2; i++) {
					for (int j = maxCol-1; j < maxCol+2; j++) {
						if(board[i][j] == 0){
							System.out.println("『[" + LETTERS[i] + "-" + j + "] に魚雷発射！』");
							i = maxRow+2;
							attacked = true;
							break;
						}
					}
				}
			}
		}

		if(attacked) {
			// TODO: do something attack blah blah
			// Attacking enemy

			// Convert input to position
			int i = maxRow;
			int j = maxCol;

			//System.out.println("DEBUG: i: " + i + " j: " + j);

			Scanner sc = new Scanner(System.in);
			String res;

			do {
				System.out.println("反応は何だ？");
				System.out.println("1. 命中");
				System.out.println("2. 波高し");
				System.out.println("3. ハズレ");
				System.out.println("4. 命中 (撃沈)");
				System.out.print("> ");
				res = sc.nextLine();
			} while(!res.matches("[1234]"));



			switch (res) {
				case "1": // 命中
					// Set target to 20 when direct hit (attacking enemy)
					nextMove[i][j] = 20;
					break;
				case "2": //波高し
					nextMove[i][j] = 0; // Set target to 0 when near hit and increase surroundings (attacking enemy)
					// --- nextMove Implementation for attacking
					nextMove[i][j] = 0; // Set self to 0

					// Increments
					// corner -> 8, top & bot -> 5, normal -> 3

					// Corner
					cornerCheck(nextMove, i, j);
					break;
				case "3": //ハズレ
					// Set target + surroundings to 0 when miss
					for (int k = i-1; k <= i+1; k++) {
						for (int l = j-1; l <= j+1; l++) {
							nextMove[k][l] = 0;
						}
					}
					break;
				case "4":
					nextMove[i][j] = 0;
					break;
				default:
					break;
			}

			System.out.println("Attacked Enemy");
			if(DEBUG) printArr("nextMove", nextMove);

			int totalHp = total(board);
			int numSub = _num(board);
			moveEquation(moveEq, totalHp, numSub, nextMove);
			if(DEBUG) printArr("nextMove", moveEq);
			//sc.close();
		}


		if(DEBUG) System.out.println("Max Move Eq: " + maxMoveEq + " Location: " + LETTERS[maxRow] + "" + maxCol);

		//return board;
	}

	static int[][] spawnShip() {
		int[][] board = {
				{-1, -1, -1, -1, -1, -1 ,-1},
				{-1, 0, 0, 0, 0, 0, -1},
				{-1, 0, 0, 0, 0, 0, -1},
				{-1, 0, 0, 0, 0, 0, -1},
				{-1, 0, 0, 0, 0, 0, -1},
				{-1, 0, 0, 0, 0, 0, -1},
				{-1, -1, -1, -1, -1, -1 ,-1}
		};

		Random r = new Random();

		// r.nextInt(max - min) + min
		//** Max number is exclusive while min is inclusive **

		board[r.nextInt(3-1) + 1][r.nextInt(3-1) + 1] = 3;
		board[r.nextInt(3-1) + 1][r.nextInt(6-3) + 3] = 3;
		board[r.nextInt(6-3) + 3][r.nextInt(3-1) + 1] = 3;
		board[r.nextInt(6-3) + 3][r.nextInt(6-3) + 3] = 3;

		return board;
	}

	public static void main(String[] args) {
		String[][] panelName = {
				{"N", "N", "N", "N", "N", "N" ,"N"},
				{"N", "A1", "A2", "A3", "A4", "A5", "N"},
				{"N", "B1", "B2", "B3", "B4", "B5", "N"},
				{"N", "C1", "C2", "C3", "C4", "C5", "N"},
				{"N", "D1", "D2", "D3", "D4", "D5", "N"},
				{"N", "E1", "E2", "E3", "E4", "E5", "N"},
				{"N", "N", "N", "N", "N", "N" ,"N"}
			};

		int[][] nextMove = {
				{-1, -1, -1, -1, -1, -1, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1, -1, -1, -1, -1, -1, -1},
		};
		
		int[][] moveEq = {
				{-1, -1, -1, -1, -1, -1, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1,  0,  0,  0,  0,  0, -1},
				{-1, -1, -1, -1, -1, -1, -1},
		};

		
		int totalHp;
		int numSub;
		String order[] = {"print", "move" ,"attack"};
		String direction[] = {"North", "South", "West", "East"};

		String orderInput = null;
		String directionInput = null;
		String moveLengthInput = null;

		int moveLength;
		String To = null;
		Scanner sc = new Scanner(System.in);

		int[][] allies = spawnShip();
		//int[][] enemies = spawnShip();

		while(true) {
			print(allies);
			System.out.println("Input the following action");
			System.out.println("1. Attack");
			System.out.println("2. Move");
			System.out.println("3. Print");
			System.out.print("> ");

			String input = sc.nextLine();

			switch(input.toLowerCase()) {
				case "attack":
				case "1":
					// --- target selection implementation
					String target = "PLACEHOLDER";

					do {
						System.out.println("Who got attacked?");
						System.out.println("1. Ally");
						System.out.println("2. Enemy");
						System.out.print("> ");

						target = sc.nextLine();
					}while(!target.equalsIgnoreCase("enemy") && !target.equalsIgnoreCase("ally") && !target.equals("1") && !target.equals("2"));


					//System.out.println("Attacking to: " + target);

					String position = "PLACEHOLDER";
					while(position.length() == 1 || position.length() != 2 && !(position.charAt(0) >= 'A' && position.charAt(0) <= 'E' && position.charAt(1) >= 1 && position.charAt(1) <= 5) ) {

						System.out.println("At Position? (A1 - E5)");
						System.out.print("> ");
						position  = sc.nextLine();
					}

					To = position;

					System.out.println("Attacking " + target + " At " + position);
					// --- ends target selection implementation

					switch(target.toLowerCase()) {
						case "ally":
						case "1":
							// **Attacking our team
							for(int i=1; i<6; i++){
								for(int j=1; j<6; j++){

									if(To.equals(panelName[i][j])){

										// --- nextMove Implementation for attacking
										nextMove[i][j] = 0; // Set self to 0

										// Increments
										// corner -> 8, top & bot -> 5, normal -> 3

										// Corner
										cornerCheck(nextMove, i, j);

										if(DEBUG) printArr("nextMove", nextMove);
										// --- ends nextMove Implementation for attacking

										if(allies[i][j] > 0){
											allies[i][j]--;
											System.out.println("命中！");

											totalHp = total(allies);
											numSub = _num(allies);
											moveEquationAr(moveEq, i, j, totalHp, numSub, nextMove);

											if(allies[i][j] == 0){
												System.out.println("命中！撃沈！");
											}
										}else if(allies[i+1][j+1] > 0 || allies[i+1][j] > 0 || allies[i+1][j-1] > 0 || allies[i][j+1] > 0 ||
												allies[i][j-1] > 0 || allies[i-1][j+1] > 0 || allies[i-1][j] > 0 || allies[i-1][j-1] > 0){ // > 0 means there exist an enemy
											System.out.println("波高し！");
											totalHp = total(allies);
											numSub = _num(allies);
											moveEquation(moveEq, totalHp, numSub, nextMove);
										}else{
											System.out.println("ハズレ！");
											totalHp = total(allies);
											numSub = _num(allies);
											moveEquation(moveEq, totalHp, numSub, nextMove);
										}

										if(DEBUG) printArr("moveEq", moveEq);

									}
								}
							}

							// decide after getting attacked
							decide(moveEq, allies, nextMove);
							break;
						case "enemy":
						case "2":
							// Attacking enemy

							// Convert input to position
							int i = position.charAt(0) - 'A' + 1;
							int j = position.charAt(1) - '1' + 1;

							//System.out.println("DEBUG: i: " + i + " j: " + j);

							String res;

							do {
								System.out.println("反応は何だ？");
								System.out.println("1. 命中");
								System.out.println("2. 波高し");
								System.out.println("3. ハズレ");
								System.out.println("4. 命中 (撃沈)");
								System.out.print("> ");
								res = sc.nextLine();
							} while(!res.matches("[1234]"));



							switch (res) {
								case "1": // 命中
									// Set target to 20 when direct hit (attacking enemy)
									nextMove[i][j] = 20;
									break;
								case "2": //波高し
									nextMove[i][j] = 0; // Set target to 0 when near hit and increase surroundings (attacking enemy)
									// --- nextMove Implementation for attacking
									nextMove[i][j] = 0; // Set self to 0

									// Increments
									// corner -> 8, top & bot -> 5, normal -> 3

									// Corner
									cornerCheck(nextMove, i, j);
									break;
								case "3": //ハズレ
									// Set target + surroundings to 0 when miss
									for (int k = i-1; k <= i+1; k++) {
										for (int l = j-1; l <= j+1; l++) {
											nextMove[k][l] = 0;
										}
									}
									break;
								case "4":
									nextMove[i][j] = 0;
									break;
								default:
									break;
							}

							System.out.println("Attacked Enemy");
							if(DEBUG) printArr("nextMove", nextMove);

							totalHp = total(allies);
							numSub = _num(allies);
							moveEquation(moveEq, totalHp, numSub, nextMove);
							if(DEBUG) printArr("nextMove", moveEq);
							break;
					}
					break;
				case "move":
				case "2":
					System.out.println("Move to which direction?");
					System.out.println("1. 北 North");
					System.out.println("2. 東 East");
					System.out.println("3. 南 South");
					System.out.println("4. 西 West");
					System.out.print("> ");

					//System.out.print("Direction: ");
					directionInput = sc.nextLine();
					if(directionInput.length() == 1) {
						directionInput = DIRECTION[Integer.parseInt(directionInput)]; // Convert Number to North...
					}

					//System.out.println("Direction: " + directionInput);
					//System.out.print("moveLength: ");


					moveLengthInput = "PLACEHOLDER";
					while(!moveLengthInput.equals("1") && !moveLengthInput.equals("2") ) {
						System.out.println("Length? (1-2)");
						System.out.print("> ");
						moveLengthInput = sc.nextLine();
					}

					//System.out.println("moveLength = " + moveLengthInput);
					System.out.println("Enemy is moving " + directionInput + " by " + moveLengthInput + " space");

					// --- nextMove implementation for move
					int dist = Integer.parseInt(moveLengthInput);

					for (int i = 0; i < nextMove.length; i++) {
						for (int j = 0; j < nextMove[0].length; j++) {

							if (nextMove[i][j] >= 12) {

								if(nextMove[i][j] >= 20) nextMove[i][j] = 10;

								if(directionInput.equalsIgnoreCase("North")) {
									if(i-dist < 1) continue;
									nextMove[i-dist][j] /= nextMove[i-dist][j] == -1 ? 1 : 2;
								} else if(directionInput.equalsIgnoreCase("East")) {
									if(j+dist > nextMove[0].length-2) continue;
									nextMove[i][j+dist] /= nextMove[i][j+dist] == -1 ? 1 : 2;
								} else if(directionInput.equalsIgnoreCase("South")) {
									if(i+dist > nextMove[0].length-2) continue;
									nextMove[i+dist][j] /= nextMove[i+dist][j] == -1 ? 1 : 2;
								} else if(directionInput.equalsIgnoreCase("West")) {
									if(j-dist < 1) continue;
									nextMove[i][j-dist] /= nextMove[i][j-dist] == -1 ? 1 : 2;
								}
							}
						}
					}

					if(DEBUG) printArr("nextMove", nextMove);

					totalHp = total(allies);
					numSub = _num(allies);
					moveEquation(moveEq, totalHp, numSub, nextMove);

					if(DEBUG) printArr("moveEq", moveEq);

					// --- ends nextMove implementation for move

					// decide after move
					decide(moveEq, allies, nextMove);
					break;
				case "print":
				case "p":
				case "3":
					print(allies);
					break;
			}

			if(input.equalsIgnoreCase("exit")) break;
		}

		sc.close();
	}

	private static void cornerCheck(int[][] nextMove, int i, int j) {
		if(nextMove[i][j-1] == -1 && nextMove[i-1][j-1] == -1 && nextMove[i-1][j] == -1 || // top left
				nextMove[i-1][j] == -1 && nextMove[i-1][j+1] == -1 && nextMove[i][j+1] == -1 || // top right
				nextMove[i][j-1] == -1 && nextMove[i+1][j-1] == -1 && nextMove[i+1][j] == -1	|| // bot left
				nextMove[i][j+1] == -1 && nextMove[i+1][j+1] == -1 && nextMove[i+1][j] == -1    // bot right
		) {
			for (int k = i-1; k <= i+1; k++) {
				for (int l = j-1; l <= j+1; l++) {
					if(k != i || l != j) nextMove[k][l] += nextMove[k][l] == -1 ? 0 : 8;
				}
			}
		}
		// Top Bottom Left Right
		else if(nextMove[i-1][j] == -1 || nextMove[i+1][j] == -1 || nextMove[i][j-1] == -1 || nextMove[i][j+1] == -1) {
			for (int k = i-1; k <= i+1; k++) {
				for (int l = j-1; l <= j+1; l++) {
					if(k != i || l != j) nextMove[k][l] += nextMove[k][l] == -1 ? 0 : 5;
				}
			}
		}
		// All other cases
		else {
			for (int k = i-1; k <= i+1; k++) {
				for (int l = j-1; l <= j+1; l++) {
					if(k != i || l != j) nextMove[k][l] += nextMove[k][l] == -1 ? 0 : 3;
				}
			}
		}
	}

}