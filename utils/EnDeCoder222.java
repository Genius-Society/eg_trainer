package utils;

public class EnDeCoder222 {

    private int[] dePerm(int permutation) {
        int a;
        int b;
        int c;
        int q;
        int[] state = new int[8];
        q = permutation;
        for (a = 1; a <= 7; a++) {
            b = q % a;
            q = (q - b) / a;
            for (c = a - 1; c >= b; c--)
                state[c + 1] = state[c];
            state[b] = 7 - a;
        }
        state[7] = 7;
        return state;
    }

    private int perm(int[] state) {
        int q = 0;
        int a;
        int b;
        int c;
        for (a = 0; a < 7; a++) {
            b = 0;
            for (c = 0; c < 7; c++) {
                if (state[c] == a)
                    break;
                if (state[c] > a)
                    b++;
            }
            q = q * (7 - a) + b;
        }
        return q;
    }

    private int orient(int[] state) {
        int n = 0;
        for (int i = 0; i < 5; i++) {
            n += state[i];
            n *= 3;
        }
        n += state[5];
        return n;
    }

    private int[] deOrient(int orientation) {
        int[] orient = new int[8];
        orient[7] = 0;
        int total = 0;
        for (int i = 5; i >= 0; i--) {
            orient[i] = orientation % 3;
            total += orient[i];
            orientation /= 3;
        }
        orient[6] = (3 - total % 3) % 3;
        return orient;
    }

    public int encode(int[][] state) {
        int permutation = perm(state[0]);
        int orientation = orient(state[1]);
        return permutation * 729 + orientation;
    }

    public int[][] decode(int code) {
        int orientation = code % 729;
        int permutation = code / 729;
        int state[][] = new int[2][8];
        state[0] = dePerm(permutation);
        state[1] = deOrient(orientation);
        return state;
    }

    public static void main(String args[]) {
        EnDeCoder222 coder = new EnDeCoder222();
        int[][] state = { { 6, 5, 4, 3, 2, 1, 0, 7 },
                { 2, 2, 2, 2, 2, 2, 2, 0 } };
        long start = System.currentTimeMillis();
        System.out.println(coder.encode(state));
        System.out.println(System.currentTimeMillis() - start);
        int[][] s = coder.decode(3669785);
        for (int i = 0; i < 8; i++) {
            System.out.print(s[0][i] + " ");
        }
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print(s[1][i] + " ");
        }
        System.out.println();
    }
}
