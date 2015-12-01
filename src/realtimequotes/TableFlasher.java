package realtimequotes;

//Данный класс взят здесь: http://www.devx.com/DevX/10MinuteSolution/17167

/**
 * TableFlasher
 *
 * Simple Utility class for performing row and cell flashing in JTables.
 *
 * This class manages flashing rows and cells and implements the 
 * FlashProvider interface.
 * 
 * <p>
 * example usage: <pre>
 *
 * JTable table = new JTable();
 * TableFlasher flasher = new TableFlasher(table);
 *
 * To flash cell (1,1)...
 *
 * flasher.flashCell(1,1);
 *
 * To flash row 1...
 *
 * flasher.flashRow(1);
 *
 * </pre>
 *
 * @author L.D'Abreo
 * @see FlashProvider
 * @see JTable
 */


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;

public final class TableFlasher
    implements CellFlashProvider, FlashListener
{

    // Statics
    public final static int          MAX_FLASHES        = 2;
    public final static int          STD_FLASH_INTERVAL = 500;
    private final static int         MAX_FLASHER_POOL   = 1000;

    // Member attributes
    private final ReentrantLock      lock;
    private final Map<Long, Flasher> flashers;
    private final JTable             table;
    private final Deque<Flasher>     flasherPool;
    private int                      poolCurrentSize;

    /**
     * Construct a Table flasher for a table
     * 
     * @param table,
     *            the table to flash
     */
    public TableFlasher(final JTable table)
    {
        this.lock = new ReentrantLock();
        this.table = table;
        this.flashers = new HashMap<Long, Flasher>();
        this.flasherPool = new ArrayDeque<Flasher>(MAX_FLASHER_POOL);
        this.poolCurrentSize = 0;
    }

    /**
     * Determines the flash state of the cell Assumes the column model
     * co-ordinate system not the view model
     * 
     * @param row,
     *            the row identifier
     * @param column,
     *            the column identifier
     * @return true if the cell flash is on, false otherwise
     */
    public boolean isFlashOn(final int row, final int column)
    {
        try
        {
            lock.lock();
            // prempt checks
            if (this.flashers.isEmpty())
            {
                return false;
            }
            // check for any cell or row flashers
            return isFlasherOn(this.getCellFlasher(row, column)) || isFlasherOn(this.getRowFlasher(row));
        } finally
        {
            lock.unlock();
        }
    }

    /**
     * Flash a Cell in the Table using the default flash settings
     */
    public final void flashCell(final int row, final int col)
    {
        this.flashCell(row, col, MAX_FLASHES, STD_FLASH_INTERVAL);
    }

    /**
     * Flash a Cell in the Table
     */
    public final void flashCell(final int row, final int col, final int maxNumberOfFlashes, final int flashInterval)
    {
        if (isCellVisible(this.table, row, col))
        {
            final Long key = constructLongKey(row, col);
            // before creation, check if already exist for this pair row/col
            // final Flasher f = new CellFlasher(this.table, maxNumberOfFlashes,
            // flashInterval, new FlashListener() {
            // public void onFlashDone(Flasher flasher)
            // {
            // TableFlasher.this.removeFlasher(key);
            // }
            // }, row, col);
          //final CellFlasher f = new CellFlasher(this.table, maxNumberOfFlashes, flashInterval, this, row, col, key);
          final CellFlasher f = borrowFlasher();//new CellFlasher(this.table, maxNumberOfFlashes, flashInterval, this, row, col, key);
          f.init(this.table, maxNumberOfFlashes, flashInterval, row, col, key);
          this.addFlasher(key, f);
        }
    }

    /**
     * @param aTable
     * @param rowIndex
     * @param vColIndex
     * @return
     */
    private final static boolean isCellVisible(final JTable aTable, final int rowIndex, final int vColIndex)
    {
        if (aTable == null || !(aTable.getParent() instanceof JViewport))
        {
            return false;
        }
        final JViewport viewport = (JViewport) aTable.getParent();

        final Rectangle rect = aTable.getCellRect(rowIndex, vColIndex, true);
        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0)
        // if (aTable.getModel() != this || aTable.getModel() instanceof
        // TableModelAddon)
        // rect = aTable.getCellRect(((TableModelAddon)
        // aTable.getModel()).getModelToView(rowIndex), vColIndex, true);
        // else
        // rect = aTable.getCellRect(rowIndex, vColIndex, true);

        // The location of the viewport relative to the table
        final Point pt = viewport.getViewPosition();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);

        // Check if view completely contains cell
        return new Rectangle(viewport.getExtentSize()).contains(rect);
    }

    /**
     * Flash a Row in the Table using the default flash settings
     */
    public void flashRow(final int row)
    {
        this.flashRow(row, MAX_FLASHES, STD_FLASH_INTERVAL);
    }

    /**
     * Flash a Row in the Table
     */
    public void flashRow(final int row, final int maxNumberOfFlashes, final int flashInterval)
    {
        final Long key = constructLongKey(row);

        final Flasher f = new RowFlasher(this.table, maxNumberOfFlashes, flashInterval, this, row, key);
        // final Flasher f = new RowFlasher(this.table, maxNumberOfFlashes,
        // flashInterval, new FlashListener()
        // {
        // public void onFlashDone(Flasher flasher)
        // {
        // TableFlasher.this.removeFlasher(key);
        // }
        // }, row);
        this.addFlasher(key, f);
    }

    /**
     * Clear flash for cell
     */
    public void clearCellFlash(final int row, final int col)
    {
        final Flasher f = this.getCellFlasher(row, col);
        if (f != null)
        {
            f.stopFlash();
            System.out.println("clearCellFlash: " + Thread.currentThread().getName());
        }
    }

    /**
     * Clear flash for a row
     */
    public void clearRowFlash(final int row)
    {
        final Flasher f = this.getRowFlasher(row);
        if (f != null)
        {
            f.stopFlash();
        }
    }

    /**
     * Clear all flashers
     */
    public void clearAllFlashers()
    {
        try
        {
            lock.lock();
            // Stop all flashers
            final Iterator itr = this.flashers.entrySet().iterator();
            while (itr.hasNext())
            {
                final Map.Entry entry = (Map.Entry) itr.next();
                // Stop the flasher
                final Flasher f = (Flasher) entry.getValue();
                f.stopFlash();
            }
            // Clear the map for good measure
            this.flashers.clear();
        } finally
        {
            lock.unlock();
        }
    }

    /**
     * Add a flasher to the flash list
     */
    private void addFlasher(final Long key, final Flasher f)
    {
        try
        {

            lock.lock();
            final Flasher old = (Flasher) this.flashers.get(key);
            if (old != null)
            {
                old.stopFlash();
            }
            // System.out.println("create: " +
            // Thread.currentThread().getName());
            this.flashers.put(key, f);
        } finally
        {
            lock.unlock();
        }
        f.startFlash();
    }

    /**
     * Return the CellFlasher for a cell if it exists
     */
    private final CellFlasher getCellFlasher(final int row, final int col)
    {
        final Object key = constructLongKey(row, col);
        try
        {
            lock.lock();
            final CellFlasher f = (CellFlasher) this.flashers.get(key);
            return f;
        } finally
        {
            lock.unlock();
        }
    }

    /**
     * Return the RowFlasher for a row if it exists
     */
    protected RowFlasher getRowFlasher(final int row)
    {
        final Object key = constructLongKey(row);
        try
        {
            lock.lock();
            final RowFlasher f = (RowFlasher) this.flashers.get(key);
            return f;
        } finally
        {
            lock.unlock();
        }
    }

    /**
     * Remove a flasher
     */
    protected void removeFlasher(final Object key)
    {
        try
        {
            lock.lock();
//            releaseFlasher(this.flashers.remove(key));
            this.flashers.remove(key);
        } finally
        {
            lock.unlock();
        }
        // System.out.println("delete: " + Thread.currentThread().getName());

    }

    private final CellFlasher borrowFlasher()
    {
        CellFlasher flasher = (CellFlasher) this.flasherPool.pollLast();
        if (flasher == null)
        {
            flasher = new CellFlasher(this);
            //System.out.println("new ....");
        }

        return flasher;
    }

    private final void releaseFlasher(final Flasher aFlasher)
    {
        if (this.flasherPool.size() < MAX_FLASHER_POOL)
            this.flasherPool.addLast(aFlasher);
        // else let GC work !
    }

    /**
     * Logic to determine whether a flasher is on or not
     */
    protected static boolean isFlasherOn(final Flasher f)
    {
        return ((f != null) && !f.isDoneFlashing() && f.isFlashOn());
    }

    // /**
    // * Utility to construct a flasher key
    // */
    // protected static Object constructKey(final int row, final int col)
    // {
    // return "" + row + "." + col;
    // }
    //
    // /**
    // * Utility to construct a flasher key
    // */
    // protected static Object constructKey(final int row)
    // {
    // return "" + row + ".-1";
    // }

    public final static Long constructLongKey(final int row, final int col)
    {
        // final static long mixFullQuick(final int aFullLen, final int
        // aQuickLen)
        // {
        // // Warning, i don't know why (feel implicit int conversion)
        // // you need to affect variable into a long for shifting datas on long
        // 64b.
        // long lReturn = aFullLen;
        // lReturn <<= INT_LEN;
        // lReturn |= aQuickLen;
        //            
        // return lReturn;
        // }

        long lReturn = col;
        lReturn <<= 32;
        lReturn |= row;

        return Long.valueOf(lReturn);
        // return "" + row + "." + col;
    }

    /**
     * Utility to construct a flasher key
     */
    public final static Long constructLongKey(final int row)
    {
        return constructLongKey(row, -1);
    }

    public final static int getRow(final Long aValue)
    {
        return (int) aValue.longValue();
    }

    public final static int getCol(final Long aValue)
    {
        return (int) (aValue.longValue() >> 32);
    }

    public static void main(String[] args)
    {
        Long value = constructLongKey(50, 12);
        System.out.println("row:" + getRow(value) + " col:" + getCol(value));
        value = constructLongKey(Integer.MAX_VALUE, 12);
        System.out.println("row:" + getRow(value) + "=" + Integer.MAX_VALUE + " col:" + getCol(value));
        value = constructLongKey(Integer.MAX_VALUE, Integer.MAX_VALUE);
        System.out.println("row:" + getRow(value) + "=" + Integer.MAX_VALUE + " col:" + getCol(value) + "=" + Integer.MAX_VALUE);
        value = constructLongKey(60);
        System.out.println("row:" + getRow(value) + " col:" + getCol(value));
        value = constructLongKey(Integer.MAX_VALUE);
        System.out.println("row:" + getRow(value) + "=" + Integer.MAX_VALUE + " col:" + getCol(value));
    }

    @Override
    public void onFlashDone(Flasher flasher)
    {
        removeFlasher(flasher.key);
    }
}

/**
 * Listener interface. Flashers will notify listener when they are done
 * flashing.
 */
interface FlashListener
{
    public void onFlashDone(Flasher flasher);
}

/**
 * Basic Flasher class for managing the Flash timer.
 */
abstract class Flasher
    implements ActionListener
{
    protected int           numberOfFlashes;
    // protected int maxFlashes;
    protected boolean       isFlashOn;
    protected Timer         timer;
    protected FlashListener listener;
    protected JTable        table;
    protected Long          key;

    public Flasher(final JTable aTable, final int aMaxFlashes, final int aFlashInterval, final FlashListener aListener, final Long aKey)
    {
        this.table = aTable;
        // this.maxFlashes = maxFlashes;
        this.numberOfFlashes = aMaxFlashes + 0; // +1 for un-flash
        this.isFlashOn = false;
        this.listener = aListener;
        this.key = aKey;

        this.timer = new Timer(aFlashInterval, this);
        this.timer.setRepeats(true);
    }

    public Flasher(final FlashListener aListener)
    {
        this.isFlashOn = true;
        this.listener = aListener;
        this.timer = new Timer(1000, this);
        this.timer.setRepeats(true);
    }

    protected void init(final JTable aTable, final int aMaxFlashes, final int aFlashInterval, final Long aKey)
    {
        this.table = aTable;
        this.numberOfFlashes = aMaxFlashes +0; // +1 for un-flash
        this.isFlashOn = false;
        this.key = aKey;
        this.timer.setInitialDelay(0);
        this.timer.setDelay(aFlashInterval);
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent aE)
    {
        doFlash();
    }

    public boolean isDoneFlashing()
    {
        return (this.numberOfFlashes == 0);
    }

    public void startFlash()
    {
        // if (SwingUtilities.isEventDispatchThread())
        // System.out.println("startFlash");
        this.timer.start();
    }

    public void stopFlash()
    {
        // if (SwingUtilities.isEventDispatchThread())
        // System.out.println("stopFlash");
        this.timer.stop();
        // this.timer = null;
        this.listener.onFlashDone(this);
        // this.listener = null;
        // this.table = null;
    }

    public boolean isFlashOn()
    {
        return this.isFlashOn;
    }

    protected void flash(final TableModelEvent event)
    {
        // reverse flash state
        this.isFlashOn = !this.isFlashOn;
        // inc the number of flashes
        if (this.isFlashOn)
        {
            --this.numberOfFlashes;
        }
        // Notify table to refresh
        this.table.tableChanged(event);
        // Check to see if we are done and if so stop.
        if (this.isDoneFlashing())
        {
            this.stopFlash();
        }
    }

    public abstract void doFlash();

};

/**
 * Class for flashing a row
 */
final class RowFlasher
    extends Flasher
{

    int row;

    public RowFlasher(final JTable table,
            final int maxFlashes,
            final int flashInterval,
            final FlashListener listener,
            final int row,
            final Long aKey)
    {
        super(table, maxFlashes, flashInterval, listener, aKey);
        this.row = row;
    }

    @Override
    public void doFlash()
    {
        // Notify table that the row to be flashed should be redrawn
        final TableModelEvent event = new TableModelEvent(this.table.getModel(), this.row, this.row, TableModelEvent.ALL_COLUMNS,
                TableModelEvent.UPDATE);
        this.flash(event);
    }
};

/**
 * Class for flashing a cell
 */
final class CellFlasher
    extends Flasher
{

    int row;
    int col;

    public CellFlasher(final JTable table,
            final int maxFlashes,
            final int flashInterval,
            final FlashListener listener,
            final int row,
            final int col,
            final Long aKey)
    {
        super(table, maxFlashes, flashInterval, listener, aKey);
        this.row = row;
        this.col = col;
    }

    public CellFlasher(FlashListener listener)
    {
        super(listener);
        // TODO Auto-generated constructor stub
    }

    protected void init(JTable table, int maxFlashes, int flashInterval, final int row, final int col, Long key)
    {
        super.init(table, maxFlashes, flashInterval, key);
        this.row = row;
        this.col = col;
    }

    @Override
    public void doFlash()
    {
        // Notify table that the cell to be flashed should be redrawn
        final TableModelEvent event = new TableModelEvent(this.table.getModel(), this.row, this.row, this.col);
        this.flash(event);
    }

}
