import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF gridFull;
    private boolean[][] opened;
    private int n;
    private int virtualTopSite;
    private int virtualBottomSite;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
        virtualTopSite = 0;
        virtualBottomSite = n * n + 1;
        grid = new WeightedQuickUnionUF(n * n + 2); // plus top and buttom
        gridFull = new WeightedQuickUnionUF(n * n + 2); // plus top
        opened = new boolean[n][n];
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        try {
            if (!isOpen(row, col)) {
                opened[row - 1][col - 1] = true;
                if (row == 1) {
                    grid.union(getGridSite(row, col), virtualTopSite);
                    gridFull.union(getGridSite(row, col), virtualTopSite);
                }
                if (row == n) {
                    grid.union(getGridSite(row, col), virtualBottomSite);
                }
                connectToOpenedSites(row, col);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void connectToOpenedSites(int row, int col) {
        if (row > 1 && isOpen(row - 1, col)) {
            grid.union(getGridSite(row, col), getGridSite(row - 1, col));
            gridFull.union(getGridSite(row, col), getGridSite(row - 1, col));
        }
        if (row < n && isOpen(row + 1, col)) {
            grid.union(getGridSite(row, col), getGridSite(row + 1, col));
            gridFull.union(getGridSite(row, col), getGridSite(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            grid.union(getGridSite(row, col), getGridSite(row, col - 1));
            gridFull.union(getGridSite(row, col), getGridSite(row, col - 1));
        }
        if (col < n && isOpen(row, col + 1)) {
            grid.union(getGridSite(row, col), getGridSite(row, col + 1));
            gridFull.union(getGridSite(row, col), getGridSite(row, col + 1));
        }
    }

    private int getGridSite(int row, int col) {
        return ((row - 1) * n + col);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        try {
            if (opened[row - 1][col - 1])
                return true;
            else
                return false;
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        try {
            if (opened[row - 1][col - 1]
                    && gridFull
                            .connected(getGridSite(row, col), virtualTopSite))
                return true;
            else
                return false;
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (opened[i][j])
                    count++;
            }
        }
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.connected(virtualTopSite, virtualBottomSite);
    }
}
