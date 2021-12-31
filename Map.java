package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Map {
    public TETile[][] map;
    private final int HEIGHT;
    private final int WIDTH ;
    private final Random RANDOM;
    private static final int MIN_LAND_LENGTH = 7;

    Map(long seed, TETile[][] map) {
        this.map = map;
        this.WIDTH = map[0].length;
        this.HEIGHT = map.length;
        this.RANDOM = new Random(seed);
        generateRooms();
    }


    public void generateRooms() {
        for (int i = 0; i < HEIGHT; ++i)
            Arrays.fill(map[i], Tileset.NOTHING);
        boolean[] status = new boolean[2400];
        HashSet<Integer> leftUps = new HashSet<>(){{
            for (int row = 0; row < HEIGHT-MIN_LAND_LENGTH; ++row) {
                int index = row * WIDTH;
                for (int col = 0; col < WIDTH-MIN_LAND_LENGTH; ++col)
                    add(index+col);
            }
        }};
        ArrayList<int[]> rooms = new ArrayList<>();
        while (!leftUps.isEmpty()) {
            int leftUP = getLeftUp(leftUps);
            int[] size = getRoomSize(status, leftUP);
            setStatus(status, leftUP, size);
            setLeftUps(leftUps, leftUP, size);
            setRoom(leftUP, size, rooms);
        }
        for (int[] room : rooms) generateRoads(room);
    }

    private int getLeftUp(HashSet<Integer> leftUps) {
        Iterator<Integer> a = leftUps.iterator();
        if (leftUps.size() > 1) {
            int rd = RandomUtils.uniform(RANDOM, 1, leftUps.size());
            int leftUp = a.next();
            while (--rd > 0)
                leftUp = a.next();
            return leftUp;
        } else
            return a.next();
    }

    private int[] getRoomSize(boolean[] status, int leftUp) {
        int rightUp = leftUp + MIN_LAND_LENGTH-1;
        while (rightUp%WIDTH > 0 && !status[rightUp] && !status[rightUp + (MIN_LAND_LENGTH-1)*WIDTH] && !status[leftUp + (MIN_LAND_LENGTH-1)*WIDTH] && rightUp < leftUp+(MIN_LAND_LENGTH+3)) ++rightUp;
        rightUp = rightUp-leftUp > MIN_LAND_LENGTH-1 ? RandomUtils.uniform(RANDOM, leftUp+(MIN_LAND_LENGTH-1), rightUp) : rightUp;
        int rightBottom = rightUp + (MIN_LAND_LENGTH-1)*WIDTH;
        while (rightBottom/WIDTH < HEIGHT && !status[rightBottom] && !status[rightBottom - rightUp + leftUp ]  && rightBottom < rightUp+(MIN_LAND_LENGTH+3)*WIDTH) rightBottom += WIDTH;
        rightBottom = rightBottom - rightUp > (MIN_LAND_LENGTH-1)*WIDTH ?
                RandomUtils.uniform(RANDOM, rightUp + (MIN_LAND_LENGTH-1)*WIDTH, rightBottom) :
                rightUp + (MIN_LAND_LENGTH-1)*WIDTH;
        return new int[]{rightUp-leftUp, (rightBottom-rightUp)/WIDTH};
    }


    private void setStatus(boolean[] status, int leftUP, int[] size) {
        int w = size[0], h = size[1];
        for (int i = 0; i < w*h; ++i)
            status[leftUP+i/w*WIDTH+i%w] = true;
    }

    private void setLeftUps(HashSet<Integer> leftUps, int leftUP, int[] size) {
        int w = size[0], h = size[1];
        int row = leftUP / WIDTH, col = leftUP % WIDTH;
        int upBound = Math.min(row, MIN_LAND_LENGTH-1), leftBound = Math.min(col, MIN_LAND_LENGTH-1);
        int begin = leftUP - upBound*WIDTH - leftBound;
        w += leftBound;
        h += upBound;
        for (int i = 0; i < w*h; ++i)
            leftUps.remove(begin + i / w * WIDTH + i % w);
    }

    private void setRoom(int leftUP, int[] size, ArrayList<int[]> rooms) {
        int w = size[0], h = size[1];
        int rightUP = leftUP + w;
        int leftBottom = leftUP + h*WIDTH;
        int beginRow = RandomUtils.uniform(RANDOM, leftUP/WIDTH, leftUP/WIDTH+2);
        int endRow = RandomUtils.uniform(RANDOM, leftBottom/WIDTH-2, leftBottom/WIDTH);
        int beginCol = RandomUtils.uniform(RANDOM, leftUP%WIDTH, leftUP%WIDTH+2);
        int endCol = RandomUtils.uniform(RANDOM, rightUP%WIDTH-2, rightUP%WIDTH);
        for (int row = beginRow; row <= endRow; ++row) {
            map[row][beginCol] = Tileset.WALL;
            map[row][endCol] = Tileset.WALL;
        }
        for (int col = beginCol+1; col < endCol; ++col) {
            map[beginRow][col] = Tileset.WALL;
            map[endRow][col] = Tileset.WALL;
        }
        for (int row = beginRow+1; row < endRow; ++row) {
            for (int col = beginCol+1 ; col < endCol; ++col)
                map[row][col] = Tileset.FLOOR;
        }
        rooms.add(new int[]{beginRow, endRow, beginCol, endCol});
    }

    private void generateRoads(int[] room) {
        int leftUp = room[0]*WIDTH + room[2];
        int leftBottom = room[1]*WIDTH + room[2];
        int rightUp = room[0]*WIDTH + room[3];
        int rightBottom = room[1]*WIDTH + room[3];
        rowRoad(leftUp, rightUp, true);
        rowRoad(leftBottom, rightBottom, false);
        colRoad(leftUp, leftBottom, true);
        colRoad(rightUp, rightBottom, false);
    }

    private void rowRoad(int left, int right, boolean up) {
        for (int i = left+1; i < right; i++) {
            if (map[i/WIDTH][i%WIDTH] == Tileset.FLOOR) return;
        }
        int point = RandomUtils.uniform(RANDOM, left+1, right);
        ArrayList<Integer> points = new ArrayList<>();
        points.add(point);
        point = up ? point - WIDTH : point + WIDTH;
        while (true) {
            if (0 > point || point >= 2400) return;
            points.add(point);
            if (map[point / WIDTH][point % WIDTH] == Tileset.WALL) {
                points.add(up ? point - WIDTH : point + WIDTH);
                break;
            }
            point = up ? point - WIDTH : point + WIDTH;
        }
        roadWalls(points);
    }

    private void colRoad(int up, int bottom, boolean left) {
        for (int i = up+WIDTH; i < bottom; i+=WIDTH) {
            if (map[i/WIDTH][i%WIDTH] == Tileset.FLOOR) return;
        }
        int point = up + WIDTH +  RandomUtils.uniform(RANDOM, 0, (bottom-up)/WIDTH-1)*WIDTH;
        ArrayList<Integer> points = new ArrayList<>();
        points.add(point);
        point = left ? point - 1 : point + 1;
        while (true) {
            if (point%WIDTH == 0|| point%WIDTH == WIDTH-1) return;
            points.add(point);
            if (map[point / WIDTH][point % WIDTH] == Tileset.WALL) {
                points.add(left ? point - 1 : point + 1);
                break;
            }
            point = left ? point - 1 : point + 1;
        }
        roadWalls(points);
    }

    private void roadWalls(ArrayList<Integer> points) {
        for (int i : points) {
            map[i/WIDTH][i%WIDTH] = Tileset.FLOOR;
            for (int row = i/WIDTH-1; row <= i/WIDTH+1; ++row) {
                if (0 > row || row >= HEIGHT) continue;
                for (int col = i%WIDTH-1; col <= i%WIDTH+1; ++col) {
                    if (0 >= col || col >= WIDTH-1) continue;
                    if (map[row][col] != Tileset.FLOOR)
                        map[row][col] = Tileset.WALL;
                }
            }
        }
    }

}
