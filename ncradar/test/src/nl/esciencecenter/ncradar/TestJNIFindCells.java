package nl.esciencecenter.ncradar;

import static org.junit.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.Test;


public class TestJNIFindCells extends JNIMethodsVol2Bird {

    private int[] readDataFromFile(String filename) throws IOException {

        List<Integer> arrList = new ArrayList<Integer>();

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line;

        while ((line = reader.readLine()) != null) {

            Scanner scanner = new Scanner(line);
            while (scanner.hasNext()) {
                int nextInt = scanner.nextInt();
                arrList.add(nextInt);
            }

        }
        reader.close();

        int nElems = arrList.size();
        int[] outputArray = new int[nElems];
        for (int iElem = 0; iElem < nElems; iElem++) {
            outputArray[iElem] = arrList.get(iElem).intValue();
        }

        return outputArray;
    }



    private int[][] reshapeTo2D(int[] inputArray, int nRows, int nCols) {

        int[][] outputArray = new int[nRows][nCols];
        int iGlobal = 0;
        for (int iRow = 0; iRow < nRows; iRow++) {
            for (int iCol = 0; iCol < nCols; iCol++) {

                outputArray[iRow][iCol] = inputArray[iGlobal];
                iGlobal += 1;

            }
        }

        return outputArray;

    }



    @Test
    public void testNativeFindCells1() throws Exception {

        // test a big array with 3 cells in it
        int nAzim = 12;
        int nRang = 5;

        int missing = 255;

        int[] texImage = new int[] { 0, 0, 0, 0, 0,
                0, 222, 222, 222, 0,
                0, 222, 222, 222, 0,
                0, 222, 222, 222, 0,
                0, 0, 0, 0, 0,
                0, 222, 222, 222, 0,
                0, 222, 222, 222, 0,
                0, 222, 222, 222, 0,
                0, 0, 0, 0, 0,
                0, 222, 222, 222, 0,
                0, 222, 222, 222, 0,
                0, 222, 222, 222, 0 };
        int[] rhoImage = null;
        int[] zdrImage = null;
        int[] cellImage = new int[] { 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0 };
        int texMissing = missing;
        int texnAzim = nAzim;
        int texnRang = nRang;
        float texValueOffset = 0.0f;
        float texValueScale = 1.0f;
        float texRangeScale = 10.0f;
        float texThresMin = 130.0f;
        int rhoMissing = missing;
        int rhonAzim = nAzim;
        int rhonRang = nRang;
        float rhoValueOffset = 0.0f;
        float rhoValueScale = 1.0f;
        float rhoThresMin = 0.0f;
        int zdrMissing = missing;
        int zdrnAzim = nAzim;
        int zdrnRang = nRang;
        float zdrValueOffset = 0.0f;
        float zdrValueScale = 1.0f;
        float zdrThresMin = 0.0f;
        float dbzThresMin = 0.0f;
        int rCellMax = (int) (nRang * texRangeScale);
        int sign = -1;
        int nCellsActual;

        nCellsActual = findCells(texImage, rhoImage, zdrImage, cellImage,
                texMissing, texnAzim, texnRang, texValueOffset, texValueScale, texRangeScale, texThresMin,
                rhoMissing, rhonAzim, rhonRang, rhoValueOffset, rhoValueScale, rhoThresMin,
                zdrMissing, zdrnAzim, zdrnRang, zdrValueOffset, zdrValueScale, zdrThresMin,
                dbzThresMin, rCellMax, sign);

        // first test the number of detected cells:
        int nCellsExpected = 3;
        assertEquals(nCellsActual, nCellsExpected);

        // then test the contents of 'cellImage':
        int[][] cellImageActual = reshapeTo2D(cellImage, nAzim, nRang);
        int[][] cellImageExpected = { { -1, -1, -1, -1, -1 },
                { -1, -1, 0, -1, -1 },
                { -1, 0, 0, 0, -1 },
                { -1, -1, 0, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, 1, -1, -1 },
                { -1, 1, 1, 1, -1 },
                { -1, -1, 1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, 2, -1, -1 },
                { -1, 2, 2, 2, -1 },
                { -1, -1, 2, -1, -1 } };
        for (int iAzim = 0; iAzim < nAzim; iAzim++) {
            for (int iRang = 0; iRang < nRang; iRang++) {
                assertEquals(cellImageActual[iAzim][iRang], cellImageExpected[iAzim][iRang]);
            }
        }
    }



    @Test
    public void testNativeFindCells2() throws Exception {

        int nAzim = 4;
        int nRang = 5;

        int missing = 255;

        int[] texImage = new int[] { 222, 222, 222, 0, 0,
                222, 222, 222, 0, 0,
                222, 222, 222, 0, 0,
                0, 0, 0, 0, 0 };
        int[] rhoImage = null;
        int[] zdrImage = null;
        int[] cellImage = new int[] { 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0 };
        int texMissing = missing;
        int texnAzim = nAzim;
        int texnRang = nRang;
        float texValueOffset = 0.0f;
        float texValueScale = 1.0f;
        float texRangeScale = 10.0f;
        float texThresMin = 130.0f;
        int rhoMissing = missing;
        int rhonAzim = nAzim;
        int rhonRang = nRang;
        float rhoValueOffset = 0.0f;
        float rhoValueScale = 1.0f;
        float rhoThresMin = 0.0f;
        int zdrMissing = missing;
        int zdrnAzim = nAzim;
        int zdrnRang = nRang;
        float zdrValueOffset = 0.0f;
        float zdrValueScale = 1.0f;
        float zdrThresMin = 0.0f;
        float dbzThresMin = 0.0f;
        int rCellMax = (int) (nRang * texRangeScale);
        int sign = -1;
        int nCellsActual;

        nCellsActual = findCells(texImage, rhoImage, zdrImage, cellImage,
                texMissing, texnAzim, texnRang, texValueOffset, texValueScale, texRangeScale, texThresMin,
                rhoMissing, rhonAzim, rhonRang, rhoValueOffset, rhoValueScale, rhoThresMin,
                zdrMissing, zdrnAzim, zdrnRang, zdrValueOffset, zdrValueScale, zdrThresMin,
                dbzThresMin, rCellMax, sign);

        // first test the number of detected cells:
        int nCellsExpected = 1;
        assertEquals(nCellsActual, nCellsExpected);

        // then test the contents of 'cellImage':
        int[][] cellImageActual = reshapeTo2D(cellImage, nAzim, nRang);
        int[][] cellImageExpected = { { -1, 0, -1, -1, -1 },
                { 0, 0, 0, -1, -1 },
                { -1, 0, -1, -1, -1 },
                { -1, -1, -1, -1, -1 } };
        for (int iAzim = 0; iAzim < nAzim; iAzim++) {
            for (int iRang = 0; iRang < nRang; iRang++) {
                assertEquals(cellImageActual[iAzim][iRang], cellImageExpected[iAzim][iRang]);
            }
        }
    }



    @Test
    public void testNativeFindCells3() throws Exception {

        // test the wrap-around nature of texImage in the azimuth direction

        int nAzim = 4;
        int nRang = 5;

        int missing = 255;

        int[] texImage = new int[] { 222, 222, 222, 0, 0,
                222, 222, 222, 0, 0,
                222, 222, 222, 0, 0,
                222, 222, 222, 0, 0 };
        int[] rhoImage = null;
        int[] zdrImage = null;
        int[] cellImage = new int[] { 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0 };
        int texMissing = missing;
        int texnAzim = nAzim;
        int texnRang = nRang;
        float texValueOffset = 0.0f;
        float texValueScale = 1.0f;
        float texRangeScale = 10.0f;
        float texThresMin = 130.0f;
        int rhoMissing = missing;
        int rhonAzim = nAzim;
        int rhonRang = nRang;
        float rhoValueOffset = 0.0f;
        float rhoValueScale = 1.0f;
        float rhoThresMin = 0.0f;
        int zdrMissing = missing;
        int zdrnAzim = nAzim;
        int zdrnRang = nRang;
        float zdrValueOffset = 0.0f;
        float zdrValueScale = 1.0f;
        float zdrThresMin = 0.0f;
        float dbzThresMin = 0.0f;
        int rCellMax = (int) (nRang * texRangeScale);
        int sign = -1;
        int nCellsActual;

        nCellsActual = findCells(texImage, rhoImage, zdrImage, cellImage,
                texMissing, texnAzim, texnRang, texValueOffset, texValueScale, texRangeScale, texThresMin,
                rhoMissing, rhonAzim, rhonRang, rhoValueOffset, rhoValueScale, rhoThresMin,
                zdrMissing, zdrnAzim, zdrnRang, zdrValueOffset, zdrValueScale, zdrThresMin,
                dbzThresMin, rCellMax, sign);

        // first test the number of detected cells:
        int nCellsExpected = 1;
        assertEquals(nCellsActual, nCellsExpected);

        // then test the contents of 'cellImage':
        int[][] cellImageActual = reshapeTo2D(cellImage, nAzim, nRang);
        int[][] cellImageExpected = { { 0, 0, 0, -1, -1 },
                { 0, 0, 0, -1, -1 },
                { 0, 0, 0, -1, -1 },
                { 0, 0, 0, -1, -1 } };
        for (int iAzim = 0; iAzim < nAzim; iAzim++) {
            for (int iRang = 0; iRang < nRang; iRang++) {
                assertEquals(cellImageActual[iAzim][iRang], cellImageExpected[iAzim][iRang]);
            }
        }
    }



    @Test
    public void testNativeFindCells4() throws Exception {

        // test the wrap-around nature of texImage in the range direction

        int nAzim = 4;
        int nRang = 5;

        int missing = 255;

        int[] texImage = new int[] { 222, 222, 222, 222, 222,
                222, 222, 222, 222, 222,
                222, 222, 222, 222, 222,
                0, 0, 0, 0, 0 };
        int[] rhoImage = null;
        int[] zdrImage = null;
        int[] cellImage = new int[] { 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0 };
        int texMissing = missing;
        int texnAzim = nAzim;
        int texnRang = nRang;
        float texValueOffset = 0.0f;
        float texValueScale = 1.0f;
        float texRangeScale = 10.0f;
        float texThresMin = 130.0f;
        int rhoMissing = missing;
        int rhonAzim = nAzim;
        int rhonRang = nRang;
        float rhoValueOffset = 0.0f;
        float rhoValueScale = 1.0f;
        float rhoThresMin = 0.0f;
        int zdrMissing = missing;
        int zdrnAzim = nAzim;
        int zdrnRang = nRang;
        float zdrValueOffset = 0.0f;
        float zdrValueScale = 1.0f;
        float zdrThresMin = 0.0f;
        float dbzThresMin = 0.0f;
        int rCellMax = (int) (nRang * texRangeScale);
        int sign = -1;
        int nCellsActual;

        nCellsActual = findCells(texImage, rhoImage, zdrImage, cellImage,
                texMissing, texnAzim, texnRang, texValueOffset, texValueScale, texRangeScale, texThresMin,
                rhoMissing, rhonAzim, rhonRang, rhoValueOffset, rhoValueScale, rhoThresMin,
                zdrMissing, zdrnAzim, zdrnRang, zdrValueOffset, zdrValueScale, zdrThresMin,
                dbzThresMin, rCellMax, sign);

        // first test the number of detected cells:
        int nCellsExpected = 1;
        assertEquals(nCellsActual, nCellsExpected);

        // then test the contents of 'cellImage':
        int[][] cellImageActual = reshapeTo2D(cellImage, nAzim, nRang);
        int[][] cellImageExpected = { { -1, 0, 0, 0, -1 },
                { 0, 0, 0, 0, 0 },
                { -1, 0, 0, 0, -1 },
                { -1, -1, -1, -1, -1 } };
        for (int iAzim = 0; iAzim < nAzim; iAzim++) {
            for (int iRang = 0; iRang < nRang; iRang++) {
                assertEquals(cellImageActual[iAzim][iRang], cellImageExpected[iAzim][iRang]);
            }
        }
    }



    @Test
    public void testNativeFindCells5() throws Exception {

        // tests findcells result when reading from a data file
        int nAzim = 12;
        int nRang = 11;

        int missing = 255;

        int[] texImage = readDataFromFile("/home/wbouten/enram/ncradar/test/data/case1/testdata-12x11-pattern-tex.txt");
        int[] rhoImage = null;
        int[] zdrImage = null;
        int[] cellImage = readDataFromFile("/home/wbouten/enram/ncradar/test/data/case1/testdata-12x11-pattern0.txt");
        int texMissing = missing;
        int texnAzim = nAzim;
        int texnRang = nRang;
        float texValueOffset = 0.0f;
        float texValueScale = 1.0f;
        float texRangeScale = 1.0f;
        float texThresMin = 32.0f;
        int rhoMissing = missing;
        int rhonAzim = nAzim;
        int rhonRang = nRang;
        float rhoValueOffset = 0.0f;
        float rhoValueScale = 1.0f;
        float rhoThresMin = 0.0f;
        int zdrMissing = missing;
        int zdrnAzim = nAzim;
        int zdrnRang = nRang;
        float zdrValueOffset = 0.0f;
        float zdrValueScale = 1.0f;
        float zdrThresMin = 0.0f;
        float dbzThresMin = 0.0f;
        int rCellMax = (int) (nRang * texRangeScale);
        int sign = -1;
        int nCellsActual;

        nCellsActual = findCells(texImage, rhoImage, zdrImage, cellImage,
                texMissing, texnAzim, texnRang, texValueOffset, texValueScale, texRangeScale, texThresMin,
                rhoMissing, rhonAzim, rhonRang, rhoValueOffset, rhoValueScale, rhoThresMin,
                zdrMissing, zdrnAzim, zdrnRang, zdrValueOffset, zdrValueScale, zdrThresMin,
                dbzThresMin, rCellMax, sign);

        // TODO complete this once issue #36 has been resolved

        // // first test the number of detected cells:
        // int nCellsExpected = 3;
        // assertEquals(nCellsActual, nCellsExpected);
        //
        // // then test the contents of 'cellImage':
        // int[][] cellImageActual = reshapeTo2D(cellImage, nAzim, nRang);
        // int[][] cellImageExpected = { { -1, -1, -1, -1, -1 },
        // { -1, -1, 0, -1, -1 },
        // { -1, 0, 0, 0, -1 },
        // { -1, -1, 0, -1, -1 },
        // { -1, -1, -1, -1, -1 },
        // { -1, -1, 1, -1, -1 },
        // { -1, 1, 1, 1, -1 },
        // { -1, -1, 1, -1, -1 },
        // { -1, -1, -1, -1, -1 },
        // { -1, -1, 2, -1, -1 },
        // { -1, 2, 2, 2, -1 },
        // { -1, -1, 2, -1, -1 } };
        // for (int iAzim = 0; iAzim < nAzim; iAzim++) {
        // for (int iRang = 0; iRang < nRang; iRang++) {
        // assertEquals(cellImageActual[iAzim][iRang],
        // cellImageExpected[iAzim][iRang]);
        // }
        // }
    }

}