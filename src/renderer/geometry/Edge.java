package renderer.geometry;

public class Edge {
    // the int start and end refer to the specific points in the mesh array.
    public int start;
    public int end;

    public Edge(int start, int end) {
        this.start = start;
        this.end = end;
    }
}