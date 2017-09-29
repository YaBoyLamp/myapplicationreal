package twiddledumber.myapplicationreal;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Thomas on 12/2/2015.
 *
 *  mContext: instance of the game that this piece adapter belongs to
 *  cells: array that holds ids for the image resources of each cell in a piece
 *  cellWidth: the width of the cell defined by the screen
 *  parent: the gridview that this adapter corresponds to
 */
public class PieceAdapter extends BaseAdapter {

    // Maximum dimensions of pieces
    public static final int MAX_WIDTH = 3;
    public static final int MAX_HEIGHT = 3;

    // Integer indicator for each piece
    public static final int ONE_PIECE = 0;
    public static final int TWO_ROW = 1;
    public static final int THREE_ROW = 2;
    public static final int TWO_COL = 3;
    public static final int THREE_COL = 4;
    public static final int L_PIECE = 5;
    public static final int TWO_SQUARE = 6;
    public static final int S_PIECE = 7;
    public static final int VERT_S_PIECE = 8;
    public static final int Z_PIECE = 9;
    public static final int ELBOW = 10;
    public static final int ELBOW1 = 11;
    public static final int ELBOW2 = 12;
    public static final int ELBOW3 = 13;
    public static final int VERT_Z_PIECE = 14;
    public static final int INVERT_L_PIECE = 15;

    private Context mContext;
    private int cells[][];
    private int cellWidth;
    private GridView parent;

    /**
     * Constructor for initialization of fields
     */
    public PieceAdapter(Context c, int width, int template, GridView p) {
        mContext = c;
        cellWidth = width;
        setTemplate(template);
        parent = p;
    }

    /**
     *  Returns array that represents layout of a piece
     */
    public int[][] getCells(){
        return cells;
    }

    /**
     *  Initializes a piece with a random color for each assigned cell
     */
    public void setTemplate(int template) {

        cells = new int[MAX_HEIGHT][MAX_WIDTH];

        switch (template) {
            // fill in squares for a horizontal piece of varying length
            case THREE_ROW:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();

            case TWO_ROW:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 + 1] = PlayGameActivity.getRandomColor();

            case ONE_PIECE:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                break;

            // fill in squared for a vertical piece of varying length
            case THREE_COL:
                cells[MAX_HEIGHT - 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();

            case TWO_COL:
                cells[MAX_HEIGHT / 2 - 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                break;

            // template for L shpaed tetrimino
            case L_PIECE:
                for (int i = 0; i < MAX_HEIGHT; i++)
                    cells[i][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT - 1][MAX_WIDTH / 2 + 1] = PlayGameActivity.getRandomColor();
                break;

            // template for square of dimension 2
            case TWO_SQUARE:
                cells[MAX_HEIGHT / 2 - 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 - 1][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();
                break;

            case VERT_S_PIECE:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 - 1][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                break;

            case VERT_Z_PIECE:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 + 1] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 - 1][MAX_WIDTH / 2 + 1] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                break;

            case Z_PIECE:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2 + 1] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                break;

            case S_PIECE:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 + 1] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();
                break;


            case ELBOW:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 + 1] = PlayGameActivity.getRandomColor();
                break;

            case ELBOW1:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 - 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 + 1] = PlayGameActivity.getRandomColor();
                break;

            case ELBOW2:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 - 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();
                break;

            case ELBOW3:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();
                break;

            case INVERT_L_PIECE:
                cells[MAX_HEIGHT / 2][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 - 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2] = PlayGameActivity.getRandomColor();
                cells[MAX_HEIGHT / 2 + 1][MAX_WIDTH / 2 - 1] = PlayGameActivity.getRandomColor();

        }
        // update piece images
        notifyDataSetChanged();
    }

    /**
     *  get number of ImageViews in piece
     */

    @Override
    public int getCount() {
        return MAX_HEIGHT * MAX_WIDTH;
    }

    /**
     * get image resource id of ImageView at specififed position
     */
    @Override
    public Object getItem(int position) {
        return (Integer)cells[position / MAX_WIDTH][position % MAX_WIDTH];
    }

    /**
     * get id of image at specified position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *  Create new View for a element in the gridview
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cellWidth));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);

            // identify image by position
            imageView.setId(position);

            // tag ImageView with what gridview it belongs to
            imageView.setTag(this.parent);

            // set listener for when a user tries to make a move
            imageView.setOnTouchListener(touch);
        } else {
            imageView = (ImageView) convertView;
        }

        // Sets image resource for each cell and hides unused cells
        if (cells[position / MAX_WIDTH][position % MAX_HEIGHT] == 0) {
            imageView.setImageResource(0);
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setImageResource(cells[position / MAX_WIDTH][position % MAX_HEIGHT]);
            imageView.setVisibility(View.VISIBLE);
        }
        return imageView;
    }

    /**
     *  Listener for a cell when it is touched for a drag
     */
    View.OnTouchListener touch = new View.OnTouchListener() {

        // Create a string for the gridview label
        private static final String GRIDVIEW_TAG = "gridview";

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // Creates a new ImageView
            ImageView imageView = (ImageView) v;

            // Create a new ClipData.Item from the ImageView object's tag
            ClipData.Item item = new ClipData.Item(GRIDVIEW_TAG);

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            ClipData dragData = new ClipData((CharSequence) GRIDVIEW_TAG,
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

            // Specify drag location to the cell the user touched
            Point dragLocation = new Point(imageView.getId() % MAX_HEIGHT, imageView.getId() / MAX_WIDTH);

            // Store base shadow location to gridview that is dragged
            parent.setTag(dragLocation);

            // Instantiates the drag shadow builder.
            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(parent);

            // hide piece that is being dragged
            parent.setAlpha(0);

            // Starts drag
            v.startDrag(dragData, myShadow, v, 0);
            return false;
        }
    };

    /**
     *  Class for drag shadow
     */
    private class MyDragShadowBuilder extends View.DragShadowBuilder {

        /**
         *  Makes MyDragShadowBuilder based on touched gridView
         */
        public MyDragShadowBuilder(View view) {
            super(view);
        }

        /**
         *  Draws the drag shadow
         */
        @Override
        public void onDrawShadow(Canvas canvas) {
            super.onDrawShadow(canvas);
        }

        /*
         *  Sets the shape of the shadow that is to be dragged and the dimensions (aka properties) of it
         */
        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            View v = getView();

            int height = (int) v.getHeight();
            int width = (int) v.getWidth();

            shadowSize.set(width, height);

            Point dragLocation = (Point) v.getTag();
            shadowTouchPoint.set(dragLocation.x * (width / 3) + (width / 6), dragLocation.y * (width /3) + (width / 6));
        }
    }
}
