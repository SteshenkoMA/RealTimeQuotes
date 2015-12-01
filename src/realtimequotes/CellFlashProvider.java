package realtimequotes;

//Данный класс взят здесь: http://www.devx.com/DevX/10MinuteSolution/17167

/**
 * CellFlashProvider
 * 
 * Interface used by interested Renderer classes to determine whether a cell is flashed or not.
 * 
 * @author L.D'Abreo
 * @see TableFlasher
 * @see CellFlashColorRenderer
 * @see CellFlashBorderRenderer
 */
public interface CellFlashProvider
{
    
    /**
     * @param row 
     * @param column 
     * @return the flash state of a cell
     */
    public boolean isFlashOn(int row, int column);
    
}