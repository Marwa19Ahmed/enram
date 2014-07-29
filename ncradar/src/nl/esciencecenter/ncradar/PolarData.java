package nl.esciencecenter.ncradar;

public class PolarData {

    private final double azimuthScaleDeg;
    private final double azimuthScaleRad;
    private final double dataOffset;
    private final int[] dataRaw; // FIXME issue #55
    private final double dataScale;
    private int[][] faces;
    private final int iAzimFirstRay;
    private final int missingValueValue;
    private final int numberOfAzimuthBins;
    private final int numberOfRangeBins;
    private final double rangeOffset;
    private final double rangeScale;
    private double[][] vertices;



    public PolarData(int numberOfAzimuthBins, int numberOfRangeBins, int[] dataRaw,
            double dataOffset, double dataScale,
            double rangeOffset, double rangeScale,
            int missingValueValue, int iAzimFirstRay) {

        this.azimuthScaleDeg = 360 / numberOfAzimuthBins;
        this.azimuthScaleRad = 2 * Math.PI / numberOfAzimuthBins;
        this.dataOffset = dataOffset;
        this.dataRaw = dataRaw;
        this.dataScale = dataScale;
        this.iAzimFirstRay = iAzimFirstRay;
        this.missingValueValue = missingValueValue;
        this.numberOfAzimuthBins = numberOfAzimuthBins;
        this.numberOfRangeBins = numberOfRangeBins;
        this.rangeOffset = rangeOffset;
        this.rangeScale = rangeScale;

    }



    public void calcVerticesAndFaces() {

        int iVertex;
        int iFace;
        int nRang = getNumberOfRangeBins();
        int nAzim = getNumberOfAzimuthBins();

        int nVertices = (nRang + 1) * nAzim;
        int nFaces = nRang * nAzim * 2;
        float azimuthScale = 1.0f;
        float azimuthScaleRad = (float) (2 * Math.PI * azimuthScale / 360);

        vertices = new double[nVertices][2];
        for (int iRang = 0; iRang <= nRang; iRang++) {
            double s = rangeOffset + rangeScale * (iRang - 0.5);
            for (int iAzim = 0; iAzim < nAzim; iAzim++) {

                iVertex = iRang * nAzim + iAzim;
                double alpha = azimuthScaleRad * (iAzim - 0.5);
                vertices[iVertex] = new double[] { Math.sin(alpha) * s,
                        Math.cos(alpha) * s };

            } // for iAzim
        }// for iRang

        faces = new int[nFaces][3];
        iFace = 0;
        int delta;
        for (int iRang = 0; iRang < nRang; iRang++) {
            for (int iAzim = 0; iAzim < nAzim; iAzim++) {

                iVertex = iRang * nAzim + iAzim;

                if (iAzim == nAzim - 1) {
                    delta = -nAzim;
                } else {
                    delta = 0;
                }

                faces[iFace] = new int[] { iVertex, iVertex + 1 + delta,
                        iVertex + 1 + nAzim + delta };
                iFace = iFace + 1;
                faces[iFace] = new int[] { iVertex,
                        iVertex + 1 + nAzim + delta, iVertex + nAzim };
                iFace = iFace + 1;

            } // for iAzim
        }// for iRang

    }



    public double getAzimuthScaleDeg() {

        return azimuthScaleDeg;
    }



    public double getAzimuthScaleRad() {

        return azimuthScaleRad;
    }



    @Override
    public PolarData clone() {

        double dataOffset = this.getDataOffset();
        int[] dataRaw = this.getDataRaw();
        double dataScale = this.getDataScale();
        int iAzimFirstRay = this.getiAzimFirstRay();
        int missingValueValue = this.getMissingValueValue();
        int numberOfAzimuthBins = this.getNumberOfAzimuthBins();
        int numberOfRangeBins = this.getNumberOfRangeBins();
        double rangeOffset = this.getRangeOffset();
        double rangeScale = this.getRangeScale();

        PolarData polarData = new PolarData(numberOfAzimuthBins, numberOfRangeBins,
                dataRaw, dataOffset, dataScale,
                rangeOffset, rangeScale,
                missingValueValue, iAzimFirstRay);

        return polarData;

    }



    public double[] getData() {

        int nAzim = getNumberOfAzimuthBins();
        int nRang = getNumberOfRangeBins();
        int nGlobal = nAzim * nRang;

        double[] data = new double[nGlobal];

        for (int iGlobal = 0; iGlobal < nGlobal; iGlobal++) {
            Number number = dataRaw[iGlobal];
            data[iGlobal] = dataOffset + dataScale * number.doubleValue();
        }

        return data;

    }



    public double[][] getData2D() {

        int nAzim = getNumberOfAzimuthBins();
        int nRang = getNumberOfRangeBins();

        double[] data = getData();

        double[][] data2D = new double[nAzim][nRang];

        int iGlobal = 0;
        for (int iAzim = 0; iAzim < nAzim; iAzim++) {
            for (int iRang = 0; iRang < nRang; iRang++) {

                data2D[iAzim][iRang] = data[iGlobal];
                iGlobal++;
            }
        }
        return data2D;
    }



    public double getDataOffset() {

        return dataOffset;
    }



    public int[] getDataRaw() {

        return dataRaw.clone();
    }



    public int[][] getDataRaw2D() {

        int nAzim = getNumberOfAzimuthBins();
        int nRang = getNumberOfRangeBins();

        int[][] dataRaw2D = new int[nAzim][nRang];

        int iGlobal = 0;
        for (int iAzim = 0; iAzim < nAzim; iAzim++) {
            for (int iRang = 0; iRang < nRang; iRang++) {

                dataRaw2D[iAzim][iRang] = dataRaw[iGlobal];
                iGlobal++;
            }
        }
        return dataRaw2D;
    }



    public double getDataScale() {

        return dataScale;
    }



    public int[][] getFaces() {

        return faces.clone();
    }



    public double[] getFacesValues() {

        double[][] data = this.getData2D();
        int nAzim = this.getNumberOfAzimuthBins();
        int nRang = this.getNumberOfRangeBins();
        double[] facesValues = new double[2 * nAzim * nRang];
        int iFace = 0;

        for (int iRang = 0; iRang < nRang; iRang++) {
            for (int iAzim = 0; iAzim < nAzim; iAzim++) {

                double v = data[iAzim][iRang];
                facesValues[iFace] = v;
                facesValues[iFace + 1] = v;
                iFace = iFace + 2;
            }
        }
        return facesValues;
    }



    public int getiAzimFirstRay() {

        return iAzimFirstRay;
    }



    public int getMissingValueValue() {

        return missingValueValue;
    }



    public int getNumberOfAzimuthBins() {

        return numberOfAzimuthBins;
    }



    public int getNumberOfRangeBins() {

        return numberOfRangeBins;
    }



    public double getRangeOffset() {

        return rangeOffset;
    }



    public double getRangeScale() {

        return rangeScale;
    }



    public double[][] getVertices() {

        return vertices.clone();
    }

}
