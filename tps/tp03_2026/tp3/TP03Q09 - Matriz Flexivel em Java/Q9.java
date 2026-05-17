import java.util.*;
import java.io.*;

public class Q9 {

    // Each row is a linked list of integers
    static class Cell {
        int val;
        Cell next;
        Cell(int val) { this.val = val; }
    }

    static class MatrizFlex {
        int n, m;
        Cell[] rows;

        MatrizFlex(int n, int m) {
            this.n = n;
            this.m = m;
            rows = new Cell[n];
        }

        void set(int i, int j, int val) {
            Cell cur = rows[i];
            for (int k = 0; k < j && cur != null; k++) cur = cur.next;
            if (cur != null) cur.val = val;
        }

        int get(int i, int j) {
            Cell cur = rows[i];
            for (int k = 0; k < j && cur != null; k++) cur = cur.next;
            return cur == null ? 0 : cur.val;
        }

        static MatrizFlex read(Scanner sc, int n, int m) {
            MatrizFlex mat = new MatrizFlex(n, m);
            for (int i = 0; i < n; i++) {
                Cell head = null, tail = null;
                for (int j = 0; j < m; j++) {
                    Cell c = new Cell(sc.nextInt());
                    if (head == null) { head = tail = c; }
                    else { tail.next = c; tail = c; }
                }
                mat.rows[i] = head;
            }
            return mat;
        }

        void printDiag() {
            int d = Math.min(n, m);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < d; i++) {
                if (i > 0) sb.append(' ');
                sb.append(get(i, i));
            }
            System.out.println(sb);
        }

        void printAntiDiag() {
            int d = Math.min(n, m);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < d; i++) {
                if (i > 0) sb.append(' ');
                sb.append(get(i, m - 1 - i));
            }
            System.out.println(sb);
        }

        static MatrizFlex add(MatrizFlex a, MatrizFlex b) {
            MatrizFlex res = new MatrizFlex(a.n, a.m);
            for (int i = 0; i < a.n; i++) {
                Cell head = null, tail = null;
                for (int j = 0; j < a.m; j++) {
                    Cell c = new Cell(a.get(i, j) + b.get(i, j));
                    if (head == null) { head = tail = c; }
                    else { tail.next = c; tail = c; }
                }
                res.rows[i] = head;
            }
            return res;
        }

        // A(n x m) * B(n x m): assumes a.m == b.n, result is a.n x b.m
        static MatrizFlex mul(MatrizFlex a, MatrizFlex b) {
            int rn = a.n, rm = b.m, inner = a.m;
            MatrizFlex res = new MatrizFlex(rn, rm);
            for (int i = 0; i < rn; i++) {
                Cell head = null, tail = null;
                for (int j = 0; j < rm; j++) {
                    int sum = 0;
                    for (int k = 0; k < inner; k++) sum += a.get(i, k) * b.get(k, j);
                    Cell c = new Cell(sum);
                    if (head == null) { head = tail = c; }
                    else { tail.next = c; tail = c; }
                }
                res.rows[i] = head;
            }
            return res;
        }

        void print() {
            for (int i = 0; i < n; i++) {
                StringBuilder sb = new StringBuilder();
                Cell cur = rows[i];
                boolean first = true;
                while (cur != null) {
                    if (!first) sb.append(' ');
                    sb.append(cur.val);
                    first = false;
                    cur = cur.next;
                }
                System.out.println(sb);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int t = 0; t < T; t++) {
            int n = sc.nextInt(), m = sc.nextInt();
            MatrizFlex A = MatrizFlex.read(sc, n, m);
            MatrizFlex B = MatrizFlex.read(sc, n, m);
            A.printDiag();
            B.printAntiDiag();
            MatrizFlex.add(A, B).print();
            MatrizFlex.mul(A, B).print();
        }
    }
}
