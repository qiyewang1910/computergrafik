package tools;

/**
 * Represents a 4x4 matrix for 3D transformations.
 */
public final class Mat44 {

    /** The identity matrix. */
    public static final Mat44 identity;  //静态常量 单位矩阵（无变换）

    static {
        identity = new Mat44();  //初始化为单位矩阵 静态代码块
    }

    /**
     * Returns a string representation of the matrix.
     *
     * @return A string representation of the matrix, with each row on a new line.
     */
    @Override
    public String toString() {
        String s = "";
        for (int r = 0; r < 4; r++) {
            s += String.format("(% .2f % .2f % .2f % .2f)\n", get(0, r), get(1, r), get(2, r), get(3, r));
        }
        return s;
    }

    /**
     * Checks if this matrix is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Mat44))
            return false;
        if (o == this)
            return true;
        Mat44 m = (Mat44) o;
        for (int i = 0; i != 16; i++)
            if (values[i] != m.values[i])
                return false;
        return true;
    }

    /**
     * Constructs a new Mat44 instance and initializes it as an identity matrix.
     */
    Mat44() {
        makeIdentity();
    }

    /**
     * Returns the internal array of matrix values.
     *
     * @return The array of matrix values.
     */
    double[] values() {
        return values;
    }

    void setValues(double[] values) {
        this.values = values;
    }

    /**
     * Gets the value at the specified column and row of the matrix.
     *
     * @param c The column index (0-3).
     * @param r The row index (0-3).
     * @return The value at the specified position.
     */
    double get(int c, int r) {
        return values[4 * c + r];    // 列优先存储 4*列 + 行
    }

    /**
     * Sets the value at the specified column and row of the matrix.
     *
     * @param c The column index (0-3).
     * @param r The row index (0-3).
     * @param v The value to set.
     */
    void set(int c, int r, double v) {
        values[4 * c + r] = v;
    }

    /**
     * Resets this matrix to the identity matrix.
     *
     * @return This matrix, reset to the identity matrix.
     */
    Mat44 makeIdentity() {
        values = new double[16];   // 初始化16个元素的数组
        set(0, 0, 1.0f);   // 列0，行0 = 1
        set(1, 1, 1.0f);   // 列1，行1 = 1
        set(2, 2, 1.0f);
        set(3, 3, 1.0f);
        return this;
    }

    /**
     * Multiplies this matrix with another matrix.
     *
     * @param m The matrix to multiply with.
     * @return A new Mat44 instance representing the result of the multiplication.
     */
    public Mat44 multiply(Mat44 m) {
        Mat44 n = new Mat44();   // 新建矩阵存储结果
        // 以下是硬编码的4x4矩阵乘法（逐列逐行计算）
        // 省略具体计算逻辑，核心规则：
        // 结果矩阵的 (列c, 行r) = 本矩阵的列c · 传入矩阵的行r（点积）
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 3] = v;
            }
        }
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 3] = v;
            }
        }
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 3] = v;
            }
        }
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 3] = v;
            }
        }
        return n;
    }

    public Mat44 invert() {
        Mat44 inv = new Mat44(); // 初始化为单位矩阵
    
        // 1. 提取原矩阵的缩放、旋转、平移分量
        double sx = get(0, 0);
        double sy = get(1, 1);
        double sz = get(2, 2);
        double tx = get(3, 0);
        double ty = get(3, 1);
        double tz = get(3, 2);
        double cos = get(0, 0); // 绕Y轴旋转的cos值
        double sin = get(2, 0); // 绕Y轴旋转的sin值
    
        // 2. 逆缩放：缩放比例取倒数
        inv.set(0, 0, 1.0 / sx);
        inv.set(1, 1, 1.0 / sy);
        inv.set(2, 2, 1.0 / sz);
    
        // 3. 逆旋转：绕Y轴旋转的逆是角度取反（cos不变，sin取反）
        inv.set(0, 2, sin / sx); // 原旋转项是 -sin，逆是 +sin
        inv.set(2, 0, -sin / sz); // 原旋转项是 +sin，逆是 -sin
    
        // 4. 逆平移：平移量取反 + 旋转逆变换
        double invTx = -tx / sx;
        double invTz = -tz / sz;
        inv.set(3, 0, invTx * cos - invTz * sin);
        inv.set(3, 1, -ty / sy);
        inv.set(3, 2, invTx * sin + invTz * cos);
    
        return inv;
    }


    /**
     * 创建缩放矩阵（列主序）
     */
    public static Mat44 scale(double sx, double sy, double sz) {
        Mat44 mat = new Mat44();
        // 缩放矩阵：对角线为缩放因子，其余为0
        mat.set(0, 0, sx);
        mat.set(1, 1, sy);
        mat.set(2, 2, sz);
        mat.set(3, 3, 1.0);
        return mat;
    }

    /**
     * 创建绕Y轴旋转矩阵（弧度制）
     */
    public static Mat44 rotateY(double angle) {
        Mat44 mat = new Mat44();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        // 绕Y轴旋转矩阵（列主序）
        mat.set(0, 0, cos);
        mat.set(0, 2, -sin);
        mat.set(2, 0, sin);
        mat.set(2, 2, cos);
        mat.set(1, 1, 1.0);
        mat.set(3, 3, 1.0);
        return mat;
    }

    /**
     * 创建绕X轴旋转矩阵（弧度制）
     */
    public static Mat44 rotateX(double angle) {
        Mat44 mat = new Mat44();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        // 绕X轴旋转矩阵（列主序）
        mat.set(1, 1, cos);
        mat.set(1, 2, sin);
        mat.set(2, 1, -sin);
        mat.set(2, 2, cos);
        mat.set(0, 0, 1.0);
        mat.set(3, 3, 1.0);
        return mat;
    }

    /**
     * 创建平移矩阵（支持int/double参数）
     */
    public static Mat44 translate(double tx, double ty, double tz) {
        Mat44 mat = new Mat44();
        // 平移矩阵：最后一列是平移量，对角线为1
        mat.set(0, 0, 1.0);
        mat.set(1, 1, 1.0);
        mat.set(2, 2, 1.0);
        mat.set(3, 0, tx);
        mat.set(3, 1, ty);
        mat.set(3, 2, tz);
        mat.set(3, 3, 1.0);
        return mat;
    }

    // 兼容int参数的translate（适配A04的int参数）
    public static Mat44 translate(int tx, int ty, int tz) {
        return translate((double) tx, (double) ty, (double) tz);
    }


    private double[] values;


    public static Mat44 rotateZ(double rz) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rotateZ'");
    }
}


