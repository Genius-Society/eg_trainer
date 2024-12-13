package scrambler;

import java.util.Random;

import utils.EnDeCoder222;

public class Scrambler222 extends Scrambler {

    // length表示最短从length步开始搜索
    // UFL, URF, UBR, ULB, DLF, DFR, DRB, DBL
    private int[][] state = { { 0, 1, 2, 3, 4, 5, 6, 7 },// 位置
            { 0, 0, 0, 0, 0, 0, 0, 0 } };// 方向
    private char[] permCost = new char[5040];
    private char[] orientCost = new char[729];
    private EnDeCoder222 coder = new EnDeCoder222();
    private int moves[][] = new int[2][100];

    public Scrambler222() {
        this(0);
    }

    public Scrambler222(int minLength) {
        this.length = minLength;
        initCostTable();
    }

    private void initCostTable() {
        reset();
        for (int i = 0; i < 5040; i++) {
            permCost[i] = 255;
            if (i < 729)
                orientCost[i] = 255;
        }
        int permCount = 0;
        int orientCount = 0;
        for (char depth = 0; permCount < 5040 || orientCount < 729; depth++) {
            if (depth == 0) {
                permCost[0] = 0;
                orientCost[0] = 0;
                permCount++;
                orientCount++;
                // System.out.println("Depth " + (int) depth + " initialized "
                // + permCount + " perms " + orientCount + " orients");
                continue;
            } else {
                for (int i = 0; i < 5040; i++) {
                    if (permCost[i] == depth - 1) {
                        int perm = i;
                        int code = perm * 729;
                        int[][] codeState = decode(code);
                        int axis = -1;
                        int turns = 0;
                        for (axis = 0; axis < 3; axis++) {
                            for (turns = 1; turns < 4; turns++) {
                                for (int j = 0; j < 8; j++) {
                                    this.state[0][j] = codeState[0][j];
                                    this.state[1][j] = codeState[1][j];
                                }
                                doMove(axis, turns);
                                int newPerm = encode(state) / 729;
                                if (permCost[newPerm] == 255) {
                                    permCost[newPerm] = depth;
                                    permCount++;
                                }
                            }
                        }
                    }
                    if (i < 729) {
                        if (orientCost[i] == depth - 1) {
                            int orient = i;
                            int code = orient;
                            int[][] codeState = decode(code);
                            int axis = -1;
                            int turns = 0;
                            for (axis = 0; axis < 3; axis++) {
                                for (turns = 1; turns < 4; turns++) {
                                    for (int j = 0; j < 8; j++) {
                                        this.state[0][j] = codeState[0][j];
                                        this.state[1][j] = codeState[1][j];
                                    }
                                    doMove(axis, turns);
                                    int newOrient = encode(state) % 729;
                                    if (orientCost[newOrient] == 255) {
                                        orientCost[newOrient] = depth;
                                        orientCount++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // System.out.println("Depth " + (int) depth + " initialized "
            // + permCount + " perms " + orientCount + " orients");
        }
    }

    private void doMove(int axis, int count) {
        switch (axis) {
            case 0:
                moveU(count);
                break;
            case 1:
                moveR(count);
                break;
            case 2:
                moveF(count);
                break;
        }
    }

    private int encode(int[][] state) {
        return coder.encode(state);
    }

    private int[][] decode(int code) {
        return coder.decode(code);
    }

    private void reset() {
        for (int i = 0; i < 8; i++) {
            state[0][i] = i;
            state[1][i] = 0;
        }
        for (int i = 0; i < moves[0].length; i++) {
            moves[0][i] = -1;
            moves[1][i] = 0;
        }
    }

    private void moveU(int n) {
        for (int i = 0; i < n; i++) {
            int t = state[0][0];
            state[0][0] = state[0][1];
            state[0][1] = state[0][2];
            state[0][2] = state[0][3];
            state[0][3] = t;

            t = state[1][0];
            state[1][0] = state[1][1];
            state[1][1] = state[1][2];
            state[1][2] = state[1][3];
            state[1][3] = t;
        }
    }

    private void moveD(int n) {
        for (int i = 0; i < n; i++) {
            int t = state[0][4];
            state[0][4] = state[0][7];
            state[0][7] = state[0][6];
            state[0][6] = state[0][5];
            state[0][5] = t;

            t = state[1][4];
            state[1][4] = state[1][7];
            state[1][7] = state[1][6];
            state[1][6] = state[1][5];
            state[1][5] = t;
        }
    }

    private void moveR(int n) {
        for (int i = 0; i < n; i++) {
            int t = state[0][1];
            state[0][1] = state[0][5];
            state[0][5] = state[0][6];
            state[0][6] = state[0][2];
            state[0][2] = t;

            t = state[1][1];
            state[1][1] = (state[1][5] + 2) % 3;
            state[1][5] = (state[1][6] + 1) % 3;
            state[1][6] = (state[1][2] + 2) % 3;
            state[1][2] = (t + 1) % 3;
        }
    }

    private void moveL(int n) {
        for (int i = 0; i < n; i++) {
            int t = state[0][0];
            state[0][0] = state[0][3];
            state[0][3] = state[0][7];
            state[0][7] = state[0][4];
            state[0][4] = t;

            t = state[1][0];
            state[1][0] = (state[1][3] + 1) % 3;
            state[1][3] = (state[1][7] + 2) % 3;
            state[1][7] = (state[1][4] + 1) % 3;
            state[1][4] = (t + 2) % 3;
        }
    }

    private void moveF(int n) {
        for (int i = 0; i < n; i++) {
            int t = state[0][0];
            state[0][0] = state[0][4];
            state[0][4] = state[0][5];
            state[0][5] = state[0][1];
            state[0][1] = t;

            t = state[1][0];
            state[1][0] = (state[1][4] + 2) % 3;
            state[1][4] = (state[1][5] + 1) % 3;
            state[1][5] = (state[1][1] + 2) % 3;
            state[1][1] = (t + 1) % 3;
        }
    }

    private void moveB(int n) {
        for (int i = 0; i < n; i++) {
            int t = state[0][2];
            state[0][2] = state[0][6];
            state[0][6] = state[0][7];
            state[0][7] = state[0][3];
            state[0][3] = t;

            t = state[1][2];
            state[1][2] = (state[1][6] + 2) % 3;
            state[1][6] = (state[1][7] + 1) % 3;
            state[1][7] = (state[1][3] + 2) % 3;
            state[1][3] = (t + 1) % 3;
        }
    }

    private void moveX(int n) {
        n %= 4;
        moveR(n);
        moveL(4 - n);
    }

    private void moveY(int n) {
        n %= 4;
        moveU(n);
        moveD(4 - n);
    }

    private void moveZ(int n) {
        n %= 4;
        moveF(n);
        moveB(4 - n);
    }

    private void randomState() {
        Random rand = new Random();
        int code;
        do {
            code = rand.nextInt(5040) * 729 + rand.nextInt(729);
        } while ((code % 729) % 3 != 0);
        this.state = decode(code);
    }

    @SuppressWarnings("unused")
    private void randomEGState() {
        this.randomEGState(0, "X");
    }

    @SuppressWarnings("unused")
    private void randomEGState(int subGroup) {
        this.randomEGState(subGroup, "X");
    }

    @SuppressWarnings("unused")
    private void randomEGState(String olls) {
        this.randomEGState(0, olls);
    }

    private void randomEGState(int subGroup, String olls) {
        Random rand = new Random();
        reset();
        // 先整体转
        moveX(rand.nextInt(4));
        moveY(rand.nextInt(4));
        moveZ(rand.nextInt(4));
        // 然后随机交换底层2块
        int x = 0, y = 0;
        switch (subGroup) {
            case 1:
                break;// 不交换
            case 2:
                do {
                    x = rand.nextInt(4);
                    y = rand.nextInt(4);
                } while ((x + y) % 2 == 0 || x == y);// 交换相邻块
                break;
            case 3:
                do {
                    x = rand.nextInt(4);
                    y = rand.nextInt(4);
                } while ((x + y) % 2 == 0);// 不交换或交换相邻块
                break;
            case 4:
                do {
                    x = rand.nextInt(4);
                    y = rand.nextInt(4);
                } while ((x + y) % 2 != 0);// 交换不相邻块
                break;
            case 5:
                do {
                    x = rand.nextInt(4);
                    y = rand.nextInt(4);
                } while ((x + y) % 2 != 0 && x != y);// 不交换或交换不相邻块
                break;
            case 6:
                do {
                    x = rand.nextInt(4);
                    y = rand.nextInt(4);
                } while (x == y);// 一定交换任意两块
                break;
            default:
                x = rand.nextInt(4);
                y = rand.nextInt(4);// 不作限制
                break;
        }
        swapCubie(x + 4, y + 4);
        // 然后随机排列顶层块
        x = rand.nextInt(4);
        y = rand.nextInt(4);
        swapCubie(x, y);
        // 随机OLL
        if (olls.toUpperCase() == "X") {
            olls = "PHUTLSAN";
        }
        char oll = olls.toUpperCase().charAt(rand.nextInt(olls.length()));
        switch (oll) {
            case 'P':
                twistCorner(0, 1);
                twistCorner(1, 2);
                twistCorner(2, 2);
                twistCorner(3, 1);
                break;
            case 'H':
                twistCorner(0, 1);
                twistCorner(1, 2);
                twistCorner(2, 1);
                twistCorner(3, 2);
                break;
            case 'U':
                twistCorner(2, 1);
                twistCorner(3, 2);
                break;
            case 'T':
                twistCorner(2, 2);
                twistCorner(3, 1);
                break;
            case 'L':
                twistCorner(1, 2);
                twistCorner(3, 1);
                break;
            case 'S':
                twistCorner(1, 2);
                twistCorner(2, 2);
                twistCorner(3, 2);
                break;
            case 'A':
                twistCorner(0, 1);
                twistCorner(1, 1);
                twistCorner(2, 1);
                break;
            case 'N':
                break;
            default:
                int sum = 0;
                for (int i = 0; i < 3; i++) {
                    int v = rand.nextInt(3);
                    sum += v;
                    twistCorner(i, v);
                }
                twistCorner(3, 3 - sum % 3);
                break;
        }
        moveU(rand.nextInt(4));//在选择EG0且无OLL的时候如果没有这步会造成始终打乱不出一步态
        // 将DBL块放到D层
        while (state[0][4] != 7 && state[0][5] != 7 && state[0][6] != 7
                && state[0][7] != 7) {
            moveX(1);
        }
        // 将DBL块放回原位
        while (state[0][7] != 7) {
            moveY(1);
        }
        // 调整DBL块色向
        while (state[1][7] != 0) {
            moveX(1);
            moveY(1);
        }

    }

    private void swapCubie(int first, int second) {
        if (first < 0 || second < 0 || first >= 8 || second >= 8
                || first == second) {
            return;
        }

        // 位置交换
        int tmp = state[0][first];
        state[0][first] = state[0][second];
        state[0][second] = tmp;

        // 色向交换
        tmp = state[1][first];
        state[1][first] = state[1][second];
        state[1][second] = tmp;
    }

    private void twistCorner(int corner, int value) {
        if (value < 0) {
            return;
        }
        state[1][corner] += value;
        state[1][corner] %= 3;
    }

    private boolean solve(int stateCode, int moveCount, int lastAxis, int depth) {
        // System.out.println("Solving Depth " + depth);
        if (moveCount == 0) {
            if (stateCode == 0)
                return true;
        } else {
            if (permCost[stateCode / 729] > moveCount
                    || orientCost[stateCode % 729] > moveCount)
                return false;
            else {
                int[][] stateNow = decode(stateCode);
                int axis = -1;
                int turns = 0;
                for (axis = 0; axis < 3; axis++) {
                    if (axis != lastAxis) {
                        for (turns = 1; turns < 4; turns++) {
                            for (int i = 0; i < 8; i++) {
                                this.state[0][i] = stateNow[0][i];
                                this.state[1][i] = stateNow[1][i];
                            }
                            moves[0][depth] = axis;
                            moves[1][depth] = turns;
                            doMove(axis, turns);
                            if (solve(encode(this.state), moveCount - 1, axis,
                                    depth + 1))
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String scramble() {
        int i = 0;
        do {
            reset();
            randomState();
            int code = encode(this.state);
            for (i = length; i < moves[0].length + 1; i++) {
                for (int j = 0; j < moves[0].length; j++) {
                    moves[0][j] = -1;
                    moves[1][j] = 0;
                }
                if (solve(code, i, -1, 0)) {
                    inverseSolution();
                    break;
                }
            }
        } while (i == moves[0].length + 1);
        this.scrambleSequence = convertToNames();
        return this.scrambleSequence;
    }

    public String scrambleEG(int subGroup, String olls) {
        int i = 0;
        do {
            int code = 0;
            do {
                reset();
                randomEGState(subGroup, olls);
                code = encode(this.state);
            } while (code == 0);
            for (i = length; i < moves[0].length + 1; i++) {
                for (int j = 0; j < moves[0].length; j++) {
                    moves[0][j] = -1;
                    moves[1][j] = 0;
                }
                if (solve(code, i, -1, 0)) {
                    inverseSolution();
                    break;
                }
            }
        } while (i == moves[0].length + 1);
        this.scrambleSequence = convertToNames();
        return this.scrambleSequence;
    }

    private void inverseSolution() {
        // TODO Auto-generated method stub
        int endIndex;
        for (endIndex = moves[0].length - 1; endIndex >= 0; endIndex--) {
            if (moves[0][endIndex] >= 0)
                break;
        }
        int i;
        int j;
        for (i = 0, j = endIndex; i <= j; i++, j--) {
            int t = moves[0][i];
            moves[0][i] = moves[0][j];
            moves[0][j] = t;

            t = 4 - moves[1][i];
            moves[1][i] = 4 - moves[1][j];
            moves[1][j] = t;
        }
    }

    private String convertToNames() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < moves[0].length; i++) {
            if (moves[0][i] == -1)
                break;
            switch (moves[0][i]) {
                case 0:
                    sb.append("U");
                    break;
                case 1:
                    sb.append("R");
                    break;
                case 2:
                    sb.append("F");
            }
            switch (moves[1][i]) {
                case 1:
                    sb.append(" ");
                    break;
                case 2:
                    sb.append("2 ");
                    break;
                case 3:
                    sb.append("' ");
                    break;
            }
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Scrambler222 scr = new Scrambler222();
        long start = System.currentTimeMillis();
        // for (int code = 0; code < 100000; code++) {
        // if ((code % 729) % 3 != 0)
        // continue;
        // for (int i = 0; i < 12; i++) {
        // for (int j = 0; j < 11; j++) {
        // scr.moves[0][j] = -1;
        // scr.moves[1][j] = 0;
        // }
        // // System.out.println("Solve at Length " + i);
        // if (scr.solve(code, i, -1, 0)) {
        // // System.out.println(System.currentTimeMillis() - start);
        // break;
        // }
        // }
        // }
        System.out.println(scr.scrambleEG(2, "PA"));
        System.out.println((System.currentTimeMillis() - start));
    }
}
